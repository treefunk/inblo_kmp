//
//  AddDailyReportSheetView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/18/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Alamofire
import shared

struct AddDailyReportSheetView: View {
    
    var isPresented: Binding<Bool>
    var horseId: String
    var activeDailyReport: HorseDailyDto? = nil
    
    @EnvironmentObject var viewModel: HorseDetailViewModel
    

    @State private var date = Date()
    @State private var bodyTemp = ""
    @State private var weight = ""
    @State private var condition = ""
    @State private var riderId = 0
    @State private var riderName = ""
    @State private var trainingTypeId = 0
    @State private var trainingTypeName = ""
    @State private var trainingAmount = ""
    @State private var time_5f = ""
    @State private var time_4f = ""
    @State private var time_3f = ""
    @State private var notes = ""
    
    @State var showImagePicker: Bool = false
    
    @State var showErrorAlert: Bool = false
    @State var errorAlertBody: String = ""
    
    @State var tempShow: Bool = false
    @State var tempInt: [Int] = [35,0]
    
    @State var weightShow: Bool = false
    @State var weightInt: Int = 0
    
    @State var trainAmountShow: Bool = false
    @State var trainAmountInt: Int = 0


    //        @State var selection: [String] = [20, 100].map { "\($0)" }

    
//    @State var selectedImage: UIImage? = nil

    @State var attachedFiles: [AttachedFile] = []
    
    init(isPresented: Binding<Bool>, horseId: String, activeDailyReport: HorseDailyDto? = nil){
        self.isPresented = isPresented
        self.horseId = horseId
        self.activeDailyReport = activeDailyReport
        
    }
    
    var body: some View {
        
        let addDailyReportResult = Binding<Bool>(
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
                
                Group {
                    DatePicker("日付*", selection: $date, displayedComponents: [.date])
                        .datePickerStyle(.automatic)
                    
                    Button(action: {
                        tempShow = true
                    }, label: {
                        TextView(text: $bodyTemp, labelText: "体温", placeHolder: "体温", isEditable: false)
                    })
                    
    //                    Picker(selection: $weight,label: Text("pick")){
    //                        Text("one")
    //                        Text("two")
    //                        Text("three")
    //                    }.pickerStyle(.wheel)
//                    Multip

                    Button(action: {
                        weightShow = true
                    }, label: {
                        TextView(text: $weight, labelText: "馬体重", placeHolder: "馬体重",isEditable: false)
                    })
                    
//                    Menu(content: {
//
//                    }, label {
//
//                    })
                    
                    TextDropDownView(dropDownData: ["良","稍重","重","不良"], labelText: "馬場状態", placeHolder: "馬場状態", text: $condition)
//
                    
//
//                    TextView(text: $condition, labelText: "馬場状態", placeHolder: "馬場状態", isEditable: false)
                    
                }
                

                    
                    Menu(content: {
                        Button(action: {
                            riderName = ""
                            riderId = 0
                        }, label: {
                            Text("-")
                        })
                        ForEach(viewModel.riders.indices, id: \.self){ index in
                                Button(action: {
                                    riderName = viewModel.riders[index].name
                                    riderId = Int(viewModel.riders[index].id)
                                }, label: {
                                    Text(viewModel.riders[index].name)
                                })
                        }
                    }, label: {
                        ZStack {
                            HStack {
                                TextView(text: $riderName, labelText: "乗り手", placeHolder: "乗り手",isEditable: false,trailingPadding: 30)
                            }
                        }
                    })
                    
                        
                        Menu(content: {
                            Button(action: {
                                trainingTypeName = ""
                                trainingTypeId = 0
                            }, label: {
                                Text("-")
                            })
                            ForEach(viewModel.trainingTypes.indices, id: \.self){ index in
                                    Button(action: {
                                        trainingTypeName = viewModel.trainingTypes[index].name
                                        trainingTypeId = Int(viewModel.trainingTypes[index].id)
                                    }, label: {
                                        Text(viewModel.trainingTypes[index].name)
                                    })
                            }
                        }, label: {
                            ZStack {
                                HStack {
                                    TextView(text: $trainingTypeName, labelText: "調教内容", placeHolder: "調教内容",isEditable: false,trailingPadding: 30)
                                }
                            }
                        })
            
                
                
                Group {
                    Button(action: {
                        trainAmountShow = true
                    }, label: {
                        TextView(text: $trainingAmount, labelText: "調教量", placeHolder: "調教量", isEditable: false)
                    })

                    HStack {
                        TextView(text: $time_5f, labelText: "5F", placeHolder: "5F", isEditable: true)
                        
                        TextView(text: $time_4f, labelText: "4F", placeHolder: "4F", isEditable: true)
                        
                        TextView(text: $time_3f, labelText: "3F", placeHolder: "3F", isEditable: true)
                    }
                }
                
               
                
                TextView(text: $notes, labelText: "メモを書く...", placeHolder: "メモを書く...", isEditable: true)
                
                
                
                VStack{
                    ForEach(Array(self.attachedFiles.enumerated()), id: \.element){ i, attachedFile in
                        let attachedFile = self.attachedFiles[i]
                        AttachedFileView(attachedFile.fileName){
                            self.attachedFiles.remove(at: i)
                        }
                    }
                }
                
                HStack {
                    Button(action: {
                        self.showImagePicker.toggle()
                    }, label: {
                        ZStack {
                            Capsule()
                                .fill(Color("ColorPrimaryDark"))
                            
                            Text("ファイルを追加")
                                .font(.system(size: 15, weight: .bold))
                                .foregroundColor(.white)
                        }.frame(width: .infinity, height: 50)
                    })
                    Spacer()
                }
                
                
                
                Button(action: {
                    
//                    if(self.$date.isEmpty){
////                        print("horse name is empty")
//                        errorAlertBody = "日付を入力してください。"
//                        showErrorAlert.toggle()
//                        return
//                    }
                    viewModel.addHorseDailyReport(
                        horseId: horseId,
                        date: date,
                        bodyTemperature: bodyTemp,
                        horseWeight: weight,
                        conditionGroup: condition,
                        riderId: String(riderId),
                        trainingTypeId: String(trainingTypeId),
                        trainingAmount: trainingAmount,
                        time5f: time_5f,
                        time4f: time_4f,
                        time3f: time_3f,
                        memo: notes,
                        attachedFiles: attachedFiles,
                        dailyAttachedIds: self.attachedFiles.filter({ $0.id != "0" }).map({ $0.id }),
                        dailyReportId: String(activeDailyReport?.id ?? 0)
                    )
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
                
                
            }//:VSTACK
            
        }//:SCROLLVIEW
        .padding(.trailing,18)
        .padding(.leading,18)
        .alert(isPresented: addDailyReportResult){
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
        .onAppear{
            if(activeDailyReport != nil){
//                date = activeDailyReport?.date
                
                let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd"
                let dateTime = formatter.date(from: activeDailyReport?.date ?? "")
                date = dateTime!
                bodyTemp = String(activeDailyReport?.bodyTemperature ?? 0.0)
                weight = String(activeDailyReport?.horseWeight ?? 0.0)
                condition = activeDailyReport?.conditionGroup ?? ""
                
                riderName = activeDailyReport?.rider?.name ?? ""
                riderId = Int(activeDailyReport?.rider?.id ?? 0)
                
                trainingTypeName = activeDailyReport?.trainingType?.name ?? ""
                trainingTypeId = Int(activeDailyReport?.trainingType?.id ?? 0)

                trainingAmount = activeDailyReport?.trainingAmount ?? ""
                time_5f = String(activeDailyReport?.time5f ?? 0.0)
                time_4f = String(activeDailyReport?.time4f ?? 0.0)
                time_3f = String(activeDailyReport?.time3f ?? 0.0)
                notes = activeDailyReport?.memo ?? ""
                
                let files = activeDailyReport?.attachedFiles as! [AttachedFileDto]
                files.forEach({ file in
                    attachedFiles.append(AttachedFile(
                        id: String(file.id),
                        fileName: file.name,
                        fileExtension: "" )
                    )
                })
                print(attachedFiles)
//                print(activeDailyReport?.attachedFiles.map{ attachedFile in
//
//                } ?? "")
            }else {
                bodyTemp = ""
                weight = ""
                condition = ""
                riderName = ""
                trainingTypeName = ""
                trainingTypeId = 0
                trainingAmount = ""
                time_5f = ""
                time_4f = ""
                time_3f = ""
                notes = ""
            }
        }
        .sheet(isPresented: $showImagePicker){
            
            ImagePicker(sourceType: .photoLibrary, onImagePicked: { attachedFile in
                attachedFiles.append(attachedFile)
            })
        }
        .sheet(isPresented: $tempShow){
//            MultiPicker(data: tempData, selection: $tempSelection)
            VStack(spacing:0) {
                HStack {
                    Button(action: {
                        tempShow = false
                    }, label: {
                        Text("Cancel")
                            .fontWeight(.semibold)
                    })
                    
                    Spacer()
                    
                    Button(action: {
                        bodyTemp = "\(tempInt[0]).\(tempInt[1])"
                        tempShow = false
                    }, label: {
                        Text("Save")
                            .fontWeight(.semibold)
                    })
                }
                .foregroundColor(Color("ColorPrimaryDark"))
                .padding(.top,16)
                .padding(.bottom,30)
                .padding(.horizontal,16)
                
                Spacer()
                
                Text("体温")
                    .font(Font.custom("roboto",size: 24))
                    .padding(.bottom,30)
                
                Spacer()
                
                HorseTempPicker(selection: $tempInt)
                
                Spacer()
            }
            .onAppear(perform: {
                if(bodyTemp.isEmpty){
                    tempInt = [35,0]
                }else{
                    tempInt = bodyTemp.components(separatedBy: ".").map{ Int($0)! }
                }
            })
            
        }
        .sheet(isPresented: $weightShow){
//            MultiPicker(data: tempData, selection: $tempSelection)
            GeometryReader { geometry in
                VStack(spacing:0) {
                    HStack {
                        Button(action: {
                            weightShow = false
                        }, label: {
                            Text("Cancel")
                                .fontWeight(.semibold)
                        })
                        
                        Spacer()
                        
                        Button(action: {
                            weight = "\(weightInt)"
                            weightShow = false
                        }, label: {
                            Text("Save")
                                .fontWeight(.semibold)
                        })
                    }
                    .foregroundColor(Color("ColorPrimaryDark"))
                    .padding(.top,16)
                    .padding(.bottom,30)
                    .padding(.horizontal,16)
                                        
                    Text("馬体重")
                        .font(Font.custom("roboto",size: 24))
                        .padding(.bottom,30)
                                        
                    Picker("Body temp", selection: self.$weightInt) {
                        ForEach(1..<1000){ num in
                            Text(verbatim: String(num))
                                .tag(num)
                        }
                    }
                    .pickerStyle(WheelPickerStyle())
                    .frame(width: (geometry.size.width / CGFloat(2)))
                    .clipped()
                    
                    Spacer()
                }
            }
            .onAppear(perform: {
                if(weight.isEmpty){
                    weightInt = 450
                }else{
                    weightInt = Int(weight) ?? 0
                }
            })
        }
            .sheet(isPresented: $trainAmountShow){
    //            MultiPicker(data: tempData, selection: $tempSelection)
                GeometryReader { geometry in
                    VStack(spacing:0) {
                        HStack {
                            Button(action: {
                                trainAmountShow = false
                            }, label: {
                                Text("Cancel")
                                    .fontWeight(.semibold)
                            })
                            
                            Spacer()
                            
                            Button(action: {
                                trainingAmount = "\(trainAmountInt)"
                                trainAmountShow = false
                            }, label: {
                                Text("Save")
                                    .fontWeight(.semibold)
                            })
                        }
                        .foregroundColor(Color("ColorPrimaryDark"))
                        .padding(.top,16)
                        .padding(.bottom,30)
                        .padding(.horizontal,16)
                                            
                        Text("調教量")
                            .font(Font.custom("roboto",size: 24))
                            .padding(.bottom,30)
                                            
                        Picker("Body temp", selection: self.$trainAmountInt) {
     
                            ForEach(Array(stride(from: 0, to: 10200, by: 200)), id: \.self) { num in
                                Text(verbatim: String(num))
                                    .tag(num)
                            }
                        }
                        .pickerStyle(WheelPickerStyle())
                        .frame(width: (geometry.size.width / CGFloat(2)))
                        .clipped()
                        
                        Spacer()
                    }
                }
                .onAppear(perform: {
                    if(trainingAmount.isEmpty){
                        trainAmountInt = 0
                    }else{
                        trainAmountInt = Int(trainingAmount) ?? 0
                    }
                })
            }
    }
    
    
}

struct AddDailyReportSheetView_Previews: PreviewProvider {
    static var previews: some View {
        AddDailyReportSheetView(isPresented: .constant(true), horseId: "")
    }
}
