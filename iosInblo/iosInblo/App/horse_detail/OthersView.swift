//
//  OthersView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 12/1/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct OthersView: View {
    var horse: Horse
    
    private let horseDetailModule = HorseDetailModule(networkModule: NetworkModule())
    @EnvironmentObject var viewModel: HorseDetailViewModel
    
    @State private var offset = CGFloat.zero
    
    
    @State private var activeTreatment: HorseTreatment? = nil
    @State private var showAddTreatmentSheet = false
    @State private var showDeleteConfirmation = false
    @State private var pendingId = "0"
    
    @State private var showViewAttachmentSheet = false


    
    init(horse: Horse) {
        self.horse = horse
        
    }
    
    var body: some View {
        
        let othersViewResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        ScrollView {
            
            VStack(spacing: 0) {
                
                Button(action: {
                    activeTreatment = nil
                    showAddTreatmentSheet = true
                }, label: {
                    ZStack {
                        RoundedRectangle(cornerRadius: 12)
                            .foregroundColor(.white)
                        
                        HStack {
                            Image(systemName: "plus")
                                .resizable()
                                .scaledToFit()
                                .foregroundColor(Color("ColorPrimary"))
                                .frame(width: 12, height: 12)
                            Text("内容を記録する")
                                .font(.system(size: 13, weight: .regular))
                                .padding(.top,8)
                            .padding(.bottom,8)
                        }
                    }
                    .padding(.leading,16)
                    .padding(.trailing,16)
                    .padding(.bottom,8)
                })
                
                
                if(self.viewModel.horseTreatments.count > 0){
                    ScrollView(.horizontal,showsIndicators: false) {
                        

                        VStack(spacing: 0){
                            HStack(spacing:14) {
                                Group {
                                    Spacer(minLength: 55)
           
                                    Text("日付")//label date
                                        .font(.system(size: 12, weight: .semibold))
                                        .foregroundColor(.white)
                                        .frame(width:70)
                                    
                                    Text("分類")//label occasion type
                                        .inbloTableLabel()
                                    
                                    Text("故障箇所")//label injured part
                                        .inbloTableLabel()
                                    
                                    Text("治療内容")//label treatment detail
                                        .inbloTableLabel()
                                    
                                    
                                    
                                }
                                Group {
                                    
                                    Text("獣医名")//label vet name
                                        .inbloTableLabel()
                                    
                                    Text("投与薬品名")//label drug name
                                        .inbloTableLabel()
                                    
 
                                }
                                Text("メモ")//label note
                                    .font(.system(size: 14, weight: .semibold))
                                    .foregroundColor(.white)
                                    .frame(width:150)
                                
                                
                                Text("添付ファイル")//label attachments
                                    .inbloTableLabel()
                                    
                                Spacer()
                            }
                            .padding(.top,4)
                            .padding(.bottom,4)
                            .background(Color("ColorPrimaryDark"))
                            .cornerRadius(8, corners: [.topLeft,.topRight])
                            .padding(.leading,16)
                            .padding(.trailing,16)
                            
                            
                                ScrollView {
                                    LazyVStack(spacing:0) {
                                        ForEach(self.viewModel.horseTreatments,id: \.self){ horseTreatment in
                                            Text("\(activeTreatment?.createdAt ?? "")").hidden().frame(width: 0, height: 0) //WORKAROUND BUG STATE NOT ASSIGNING https://developer.apple.com/forums/thread/652080
                                            InbloTreatmentRow(
                                                horseTreatment: horseTreatment,
                                                onClickEdit: { horseTreatment in
                                                   
                                                    activeTreatment = horseTreatment
                                                    if(activeTreatment != nil){
                                                        showAddTreatmentSheet = true
                                                    }
                                                },
                                                onClickRemove: { horseTreatment in
                                                    print("remove \(horseTreatment.id) is clicked")
//                                                    viewModel.removeTreatment(treatmentId: String(horseTreatment.id),horseId: horse.id?.stringValue ?? "")
                                                    
                                                    self.pendingId = String(horseTreatment.id)
                                                    showDeleteConfirmation = true
                                                },
                                                onClickViewAttachments: { horseTreatment in
                                                    activeTreatment = horseTreatment
                                                    if(activeTreatment != nil){
                                                        showViewAttachmentSheet = true
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                                .frame(height:300)
                                
                            
                                    
                            
                        }// :VSTACK
                       
                        
                    }//:SCROLLVIEW (horizontal)
                }
                
                
                

            }//:VSTACK
            .padding(.top,12)
        }//:SCROLLVIEW
        .background(Color("ColorDarkBackground"))
        .fullScreenCover(isPresented: $showAddTreatmentSheet){
            AddTreatmentSheetView(isPresented: $showAddTreatmentSheet, horseId: (horse.id?.stringValue ?? ""), activeHorseTreatment: activeTreatment)
                .environmentObject(viewModel)
        }
        .fullScreenCover(isPresented: $showViewAttachmentSheet){
            ViewAttachmentSheet(isPresented: $showViewAttachmentSheet, attachedFiles: activeTreatment!.attachedFiles!, dirString: "/treatments-file/")
        }
        .alert(isPresented: othersViewResult){
            Alert(
                title: Text(viewModel.alert!.title),
                message: Text(viewModel.alert!.body),
                dismissButton: .default(Text("OK"), action: {
                    viewModel.alert = nil
                })
            )
        }
        .alert(isPresented: $showDeleteConfirmation){
            
            Alert(title: Text("Are you sure you want to delete this?"),
                        message: Text(""),
                        primaryButton: .default(
                            Text("Cancel"),
                            action: {
                                
                            }
                        ),
                        secondaryButton: .destructive(
                            Text("Delete"),
                            action: {
                                print("pending ID is -> \(pendingId)")
//                                viewModel.removeDailyReport(dailyReportId: pendingId, horseId: horse.id?.stringValue ?? "")
                                viewModel.removeTreatment(treatmentId: pendingId,horseId: horse.id?.stringValue ?? "")

                            }
                        )
                    )
//            viewModel.removeDailyReport(dailyReportId: String(daily.id),horseId: horse.id?.stringValue ?? "")

        }
    }
}

//struct OthersView_Previews: PreviewProvider {
//    static var previews: some View {
//        OthersView()
//    }
//}
