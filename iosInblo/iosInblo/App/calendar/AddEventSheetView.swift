//
//  AddEventSheetView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 1/29/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AddEventSheetView: View {
    var isPresented: Binding<Bool>
    var activeMonth: Binding<String>
    var activeYear: Binding<String>
    var activeHorseId: Binding<Int>
    var activeHorseName: Binding<String>
    var activeEvent: Binding<Event?>
    
    @EnvironmentObject var viewModel: CalendarViewModel
    @AppStorage("stableId") var stableId: Int = 0
    @AppStorage("userId") var userId: Int = 0
    
    let dateAndTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm"
        return formatter
    }()
    
    let formatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()
    
    let timeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter
    }()

    
    @State private var eventTitle = ""
    @State private var eventType = "" // dropdown
    @State private var dateStart = Date()
    @State private var dateEnd = Date()
    @State private var timeStart = ""
    @State private var timeEnd = ""
    @State private var horseName = "" // dropdown
    @State private var horseId = 0
    @State private var notes = ""

    @State var showErrorAlert: Bool = false
    @State var errorAlertBody: String = ""
    
//    @State var data: [(String, [String])] = [
//            ("Two", Array(20...40).map { "\($0)" }),
//            ("Three", Array(100...200).map { "\($0)" })
//        ]
//        @State var selection: [String] = [20, 100].map { "\($0)" }
    

    
    init(isPresented: Binding<Bool>, activeEvent: Binding<Event?>, activeMonth: Binding<String>, activeYear: Binding<String>, activeHorseId: Binding<Int>, activeHorseName: Binding<String>){
        self.isPresented = isPresented
        self.activeEvent = activeEvent
        self.activeMonth = activeMonth
        self.activeYear = activeYear
        self.activeHorseId = activeHorseId
        self.activeHorseName = activeHorseName
    }
    
    var body: some View {
        
        let addEventResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        
        
        ScrollView(showsIndicators: false) {
            VStack(spacing: 25){
                HStack {
                    Spacer()
                        .frame(maxWidth: .infinity)
                    Text("状態入力 ")
                        .frame(maxWidth: .infinity)
                    HStack {
                        Spacer()
                        Button(action: {
                            self.isPresented.wrappedValue = false
                        }, label: {
                            Image("IconCloseRound")
                        })
                        .buttonStyle(PlainButtonStyle())
                    }
                }//:HSTACK
                .alert(isPresented: $showErrorAlert ){
                    Alert(
                        title: Text(""),
                        message: Text(errorAlertBody),
                        dismissButton: .default(Text("OK"), action: {
                            self.showErrorAlert = false
                        })
                    )
                }
                
                Group {
//                    DatePicker("日付*", selection: $date, displayedComponents: [.date])
//                        .datePickerStyle(.automatic)
                    
                    TextView(text: $eventTitle, labelText: "タイトル", placeHolder: "タイトル", isEditable: true)
                    
                    
                    TextDropDownView(dropDownData: EventOptions.allCases.map({ $0.info.name }), labelText: "イベントType", placeHolder: "イベントType", text: $eventType)
//
                    
                }
                
                Group {
                    
                    
                    DatePicker("開始日*", selection: $dateStart, displayedComponents: [.date, .hourAndMinute])
                        .onChange(of: dateStart) {
                        
                            if($0 > self.dateEnd){
                                self.dateEnd = $0
                            }
                        }
                                            .datePickerStyle(.automatic)
                                            
                    
                    DatePicker("終了日", selection: $dateEnd, displayedComponents: [.date, .hourAndMinute])
                        .onChange(of: dateEnd){
                            if($0 < self.dateStart){
                                self.dateStart = $0
                            }
                        }
                                            .datePickerStyle(.automatic)
//
//                    HStack {
//                        TextView(text: $timeStart, labelText: "開始時間", placeHolder: "開始時間", isEditable: true)
//
//                        TextView(text: $timeEnd, labelText: "終了時間", placeHolder: "終了時間", isEditable: true)
//
//                    }
                    
//                    TextDropDownView(dropDownData:["1","2","3","4"], labelText: "管理馬名", placeHolder: "管理馬名", text: $horseName)
//

//                    if activeHorseId.wrappedValue == 0 {
                    ZStack {
                        
                        if horseName != "" && horseId != 0 {
                            HStack {
                                Spacer()
                                Button(action: {
                                    horseName = ""
                                    horseId = 0
                                }, label: {
                                    Image(systemName: "xmark")
                                        .foregroundColor(Color("ColorPrimaryDark"))
                                        .aspectRatio(contentMode: .fill)
                                        .frame(minWidth: 0, maxWidth: 25, minHeight: 0, maxHeight: .infinity)
                                        .padding(.all,8)
                                        
                                })
                            }
                            
                            .zIndex(10)
                        }
                        
                        
                        
                        Menu(content: {

                            Button(action: {
                                horseName = ""
                                horseId = 0
                            }, label: {
                                Text("-")
                            })
                            
                            ForEach(viewModel.horses.indices, id: \.self){ index in

                                    Button(action: {
                                        horseName = viewModel.horses[index].name!
                                        horseId = Int(viewModel.horses[index].id!)
                                    }, label: {
                                        Text(viewModel.horses[index].name!)
                                    })
                            }
                        }, label: {
                            ZStack {
                                HStack {
                                    TextView(text: $horseName, labelText: "管理馬名", placeHolder: "管理馬名",isEditable: false,trailingPadding: 30)
                                    
                                }
                                
                                
                                            
                            }
                            
                        })
                        
                        
                    }
                        
//                    }
//                    else {
//                        TextView(text: self.$horseName, labelText: "管理馬名", placeHolder: "管理馬名", isSecureTextEntry: false, isEditable: false)
//                    }
                    

                    
                }
                
               
                
                TextView(text: $notes, labelText: "メモを書く...", placeHolder: "メモを書く...", isEditable: true)
//
//                VStack() {
//                            Text(verbatim: "Selection: \(selection)")
//                    HStack {
//                        Spacer()
//                        MultiPicker(data: data, selection: $selection).frame(height: 300)
//
//                    }
//                }
                
                

            
                
                
                
                Button(action: {
                    
//                    if(self.$eventTitle.isEmpty){
////                        print("horse name is empty")
//                        errorAlertBody = "日付を入力してください。"
//                        showErrorAlert.toggle()
//                        return
//                    }
                    
                    var dateStartStr = formatter.string(from: dateStart)
                    var timeStartStr = timeFormatter.string(from: dateStart)
                    var dateEndStr = formatter.string(from: dateEnd)
                    var timeEndStr = timeFormatter.string(from: dateEnd)
                    
                    
//                    viewModel.addEvent(horseId: horseId, userId: userId, stableId: stableId, dateStart: dateStartStr, dateEnd: dateEnd, title: eventTitle, start:timeStartStr, end: timeEndStr, notes: notes, eventId: nil)
                    let activeId = String(activeEvent.wrappedValue?.id ?? 0)
        
//
                    if(self.eventType.isEmpty){
                        errorAlertBody = "すべての項目を入力してください。"
                        showErrorAlert.toggle()
                        return
                    }
//                    すべての項目を入力してください。
                    viewModel.addCalendarEvent(horseId: horseId, userId: userId, stableId: stableId, dateStart: dateStartStr, dateEnd: dateEndStr, title: eventTitle, eventType: eventType, start: timeStartStr, end: timeEndStr, notes: notes, eventId: activeId, cMonth: activeMonth.wrappedValue, cYear: activeYear.wrappedValue, cHorseId: activeHorseId.wrappedValue)
                }, label: {
                    ZStack {
                        Capsule()
                            .fill(Color("ColorPrimary"))
                        
                        Capsule()
                            .fill(Color("ColorBlueButton"))
                            .padding(.bottom,5)
                        Capsule()
                            .strokeBorder(Color("ColorBlueButtonBorder"), lineWidth: 1.5)
                            .padding(.bottom,5)
                        Text("＋ 追加")
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(.white)
                    }.frame(width: .infinity, height: 60)
                })
                    .alert(isPresented: addEventResult){
                        Alert(
                            title: Text(viewModel.alert!.title),
                            message: Text(viewModel.alert!.body),
                            dismissButton: .default(Text("OK"), action: {
                                if(viewModel.alert?.type == AlertContent.AlertType.success){
                                    self.isPresented.wrappedValue = false
                                }
                                viewModel.alert = nil
                            })
                        )
                    }
                
                
            }//:VSTACK
            
        }//:SCROLLVIEW
        .padding(.trailing,18)
        .padding(.leading,18)
        
        .onAppear{
            if(activeHorseId.wrappedValue != 0){
                self.horseId = activeHorseId.wrappedValue
                self.horseName = activeHorseName.wrappedValue
//                self.horseName = e!.horse?.name ?? ""
            }
                
            if(activeEvent.wrappedValue != nil){
                let e = activeEvent.wrappedValue
                let dStart = dateAndTimeFormatter.date(from: "\(e!.date) \(e!.start)")
                let dEnd = dateAndTimeFormatter.date(from: "\(e!.dateEnd!) \(e!.end)")

                if(dStart != nil){
                    self.dateStart = dStart!
                }
                if(dEnd != nil){
                    self.dateEnd = dEnd!
                }else{
                    self.dateEnd = dStart!
                }
                
                self.eventTitle = e!.title
                self.eventType = e!.eventType
                if(activeHorseId.wrappedValue != 0){
                    self.horseId = Int(e!.horse?.id ?? 0)
                    self.horseName = e!.horse?.name ?? ""
                }
               
                self.notes = e!.notes ?? ""
            }
//            if(activeDailyReport != nil){
////                date = activeDailyReport?.date
//
//                let formatter = DateFormatter()
//                formatter.dateFormat = "yyyy-MM-dd"
//                let dateTime = formatter.date(from: activeDailyReport?.date ?? "")
//                date = dateTime!
//                bodyTemp = String(activeDailyReport?.bodyTemperature ?? 0.0)
//                weight = String(activeDailyReport?.horseWeight ?? 0.0)
//                condition = activeDailyReport?.conditionGroup ?? ""
//                riderName = activeDailyReport?.riderName ?? ""
//                trainingType = activeDailyReport?.trainingType ?? ""
//                trainingAmount = activeDailyReport?.trainingAmount ?? ""
//                time_5f = String(activeDailyReport?.time5f ?? 0.0)
//                time_4f = String(activeDailyReport?.time4f ?? 0.0)
//                time_3f = String(activeDailyReport?.time3f ?? 0.0)
//                notes = activeDailyReport?.memo ?? ""
//
//                let files = activeDailyReport?.attachedFiles as! [AttachedFileDto]
//                files.forEach({ file in
//                    attachedFiles.append(AttachedFile(
//                        id: String(file.id),
//                        fileName: file.name,
//                        fileExtension: "" )
//                    )
//                })
//                print(attachedFiles)
////                print(activeDailyReport?.attachedFiles.map{ attachedFile in
////
////                } ?? "")
//            }else {
//                bodyTemp = ""
//                weight = ""
//                condition = ""
//                riderName = ""
//                trainingType = ""
//                trainingAmount = ""
//                time_5f = ""
//                time_4f = ""
//                time_3f = ""
//                notes = ""
//            }
        }
    }
}

//struct AddEventSheetView_Previews: PreviewProvider {
//    static var previews: some View {
//        AddEventSheetView(constant(true),activeEvent: )
//    }
//}
