//
//  AddTreatmentSheetView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 12/2/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AddTreatmentSheetView: View {
    
    var isPresented: Binding<Bool>
    var horseId: String
    var activeHorseTreatment: HorseTreatment? = nil
    
    @EnvironmentObject var viewModel: HorseDetailViewModel
    

    @State private var date = Date()
    @State private var occasionType = ""
    @State private var injuredPart = ""
    @State private var treatmentDetail = ""
    @State private var vetName = ""
    @State private var drugName = ""
    @State private var notes = ""
    
    
    @State var showImagePicker: Bool = false
    
    @State var showErrorAlert: Bool = false
    @State var errorAlertBody: String = ""
    
//    @State var selectedImage: UIImage? = nil

    @State var attachedFiles: [AttachedFile] = []
    
    init(isPresented: Binding<Bool>, horseId: String, activeHorseTreatment: HorseTreatment? = nil){
        self.isPresented = isPresented
        self.horseId = horseId
        self.activeHorseTreatment = activeHorseTreatment
        
    }
    
    var body: some View {
        
        let addHorseTreatmentResult = Binding<Bool>(
            get: { self.viewModel.othersAlert != nil },
            set: { _ in self.viewModel.othersAlert?.title = "" }
        )
        
        
        
        ScrollView(showsIndicators: false) {
            VStack(spacing: 25){
                HStack {
                    Spacer()
                        .frame(maxWidth: .infinity)
                    Text("治療内容")
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
                    DatePicker("日付*", selection: $date, displayedComponents: [.date])
                        .datePickerStyle(.automatic)
                    
                    TextDropDownView(dropDownData: ["故障・状態異常","獣医診察","治療","装蹄","飼い葉","その他"], labelText: "分類*", placeHolder: "分類*", text: $occasionType)
                    
                    TextView(text: $notes, labelText: "メモを書く...", placeHolder: "メモを書く...", isEditable: true)

                    TextView(text: $injuredPart, labelText: "故障箇所", placeHolder: "故障箇所", isEditable: true)
                    
                    TextView(text: $treatmentDetail, labelText: "治療内容", placeHolder: "治療内容")
                    
                    
                    TextView(text: $vetName, labelText: "獣医名", placeHolder: "獣医名")

                    
                    TextView(text: $drugName, labelText: "薬品名", placeHolder: "薬品名")

                
                }
                
                
               
                
                
                
                
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
                
//            attachedFiles: attachedFiles,
//            dailyAttachedIds: self.attachedFiles.filter({ $0.id != "0" }).map({ $0.id }),
//            dailyReportId: String(activeDailyReport?.id ?? 0)
//
                Button(action: {
                    
//                    if(self.$date.isEmpty){
////                        print("horse name is empty")
//                        errorAlertBody = "日付を入力してください。"
//                        showErrorAlert.toggle()
//                        return
//                    }
//                    viewModel.addHorseTreatment(
//                        horseId: horseId,
//                        date: date,
//                        bodyTemperature: bodyTemp,
//                        horseWeight: weight,
//                        conditionGroup: condition,
//                        riderName: riderName,
//                        trainingType: trainingType,
//                        trainingAmount: trainingAmount,
//                        time5f: time_5f,
//                        time4f: time_4f,
//                        time3f: time_3f,
//                        memo: notes,
//
//                    )
                    if(self.occasionType.isEmpty){
                        errorAlertBody = "すべての項目を入力してください。"
                                                showErrorAlert.toggle()
                                                return
                    }
                    
//                    print(attached)
                    

                    
                    viewModel.addTreatment(
                        horseId: horseId,
                        date: date,
                        vetName: vetName,
                        treatmentDetail: treatmentDetail,
                        injuredPart: injuredPart,
                        occasionType: occasionType,
                        medicineName: drugName,
                        memo: notes,
                        attachedFiles: attachedFiles,
                        treatmentAttachedIds: self.attachedFiles.filter({ $0.id != "0" }).map({ $0.id }),
                        horseTreatmentId: String(activeHorseTreatment?.id ?? 0)
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
                }).alert(isPresented: addHorseTreatmentResult){
                    Alert(
                        title: Text(viewModel.othersAlert!.title),
                        message: Text(viewModel.othersAlert!.body),
                        dismissButton: .default(Text("OK"), action: {
                            if(viewModel.othersAlert?.type == AlertContent.AlertType.success){
                                self.isPresented.wrappedValue = false
                            }
                            viewModel.othersAlert = nil
                        })
                    )
                    
                    
                }
                    
                
                
            }//:VSTACK
            
        }//:SCROLLVIEW
        .padding(.trailing,18)
        .padding(.leading,18)
        
        .onAppear{
            if(activeHorseTreatment != nil){
//                date = activeDailyReport?.date
                
                let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd"
                let dateTime = formatter.date(from: activeHorseTreatment?.date ?? "")
                date = dateTime!
                occasionType = activeHorseTreatment?.occasionType ?? ""
                injuredPart = activeHorseTreatment?.injuredPart ?? ""
                treatmentDetail = activeHorseTreatment!.treatmentDetail ?? ""
                vetName = activeHorseTreatment?.doctorName ?? ""
                drugName = activeHorseTreatment?.medicineName ?? ""
                notes = activeHorseTreatment?.memo ?? ""
                
                let files = activeHorseTreatment?.attachedFiles as! [AttachedFileDto]
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
                occasionType = ""
                injuredPart = ""
                treatmentDetail = ""
                vetName = ""
                drugName = ""
                notes = ""
            }
        }
        .sheet(isPresented: $showImagePicker){
            
            ImagePicker(sourceType: .photoLibrary, onImagePicked: { attachedFile in
                attachedFiles.append(attachedFile)
            })
        }

        
    }
    
    
}
