//
//  DailyTabView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/14/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared


struct DailyTabView: View {
    
    
    var horse: Horse
    
    private let horseDetailModule = HorseDetailModule(networkModule: NetworkModule())
    @EnvironmentObject var viewModel: HorseDetailViewModel
    
    @State private var offset = CGFloat.zero
    
    @State private var activeDailyReport: HorseDailyDto? = nil
    @State private var showAddDailyReportSheet = false
    @State private var showDeleteConfirmation = false
    @State private var pendingId = "0"
    
    @State private var showViewAttachmentSheet = false
    


    
    init(horse: Horse) {
        self.horse = horse
        
    }
    
    var body: some View {
        
        let dailyTabViewResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        ScrollView {
            
            VStack(spacing: 0) {
                
                Button(action: {
                    activeDailyReport = nil
                    showAddDailyReportSheet = true
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
                            Text("状態を記録する")
                                .font(.system(size: 13, weight: .regular))
                                .padding(.top,8)
                            .padding(.bottom,8)
                        }
                    }
                    .padding(.leading,16)
                    .padding(.trailing,16)
                    .padding(.bottom,8)
                })
                
                
                if(self.viewModel.dailyReports.count > 0){
                    ScrollView(.horizontal,showsIndicators: false) {
                        

                        VStack(spacing: 0){
                            HStack(spacing:14) {
                                Spacer(minLength: 55)

                                Group {
           
                                    if !self.viewModel.hiddenColumns.contains("date") {
                                        Text("日付")//label date
                                            .font(.system(size: 12, weight: .semibold))
                                            .foregroundColor(.white)
                                            .frame(width:70)
                                            .hidden()
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("body_temperature"){
                                    Text("体温")//label temperature
                                        .inbloTableLabel()
                                        
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("horse_weight") {
                                    Text("馬体重")//label weight
                                        .inbloTableLabel()
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("rider") {
                                    Text("乗り手")//label rider name
                                        .inbloTableLabel()
                                    }
                                }
                                Group {
                                    if !self.viewModel.hiddenColumns.contains("training_type") {
                                    Text("調教内容")//label training type
                                        .inbloTableLabel()
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("training_amount") {
                                    Text("調教量")//label trainign amount
                                        .inbloTableLabel()
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("5f_time") {
                                    Text("5F")//label 5f
                                        .inbloTableLabel()
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("4f_time") {
                                    Text("4F")//label 4f
                                        .inbloTableLabel()
                                    }
                                    
                                    if !self.viewModel.hiddenColumns.contains("3f_time") {
                                    Text("3F")//label 3f
                                        .inbloTableLabel()
                                    }
                                }
                                if !self.viewModel.hiddenColumns.contains("memo"){
                                Text("メモ")//label note
                                    .font(.system(size: 14, weight: .semibold))
                                    .foregroundColor(.white)
                                    .frame(width:150)
                                }
                                
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
                                        ForEach(self.viewModel.dailyReports,id: \.self){ dailyReport in
                                            Text("\(activeDailyReport?.createdAt ?? "")").isHidden(true,remove: true) //WORKAROUND BUG STATE NOT ASSIGNING https://developer.apple.com/forums/thread/652080
                                            
                                            InbloDailyRow(
                                                horseDaily: dailyReport,
                                                hiddenColumns: self.viewModel.hiddenColumns,
                                                onClickEdit: { daily in
                                                   
                                                    activeDailyReport = daily
                                                    if(activeDailyReport != nil){
                                                        showAddDailyReportSheet = true
                                                    }
                                                },
                                                onClickRemove: { daily in
                                                    print("remove \(daily.id) is clicked")
                                                    self.pendingId = String(daily.id)
                                                    showDeleteConfirmation = true
                                                },
                                                onClickViewAttachments: { daily in
                                                    activeDailyReport = daily
                                                    if(activeDailyReport != nil){
                                                        showViewAttachmentSheet = true
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }.frame(height:300)
                            
                                    
                            
                        }// :VSTACK
                       
                        
                    }//:SCROLLVIEW (horizontal)
                }
                
                
                

            }//:VSTACK
            .padding(.top,12)
        }//:SCROLLVIEW
        .background(Color("ColorDarkBackground"))
        .fullScreenCover(isPresented: $showAddDailyReportSheet){
            AddDailyReportSheetView(isPresented: $showAddDailyReportSheet, horseId: (horse.id?.stringValue ?? ""), activeDailyReport: activeDailyReport)
                .environmentObject(viewModel)
        }
        .fullScreenCover(isPresented: $showViewAttachmentSheet){
            ViewAttachmentSheet(isPresented: $showViewAttachmentSheet, attachedFiles: activeDailyReport!.attachedFiles!, dirString: "/daily-reports-file/")
        }
        .alert(isPresented: dailyTabViewResult){
            Alert(
                title: Text(viewModel.alert!.title),
                message: Text(viewModel.alert!.body),
                dismissButton: .default(Text("OK"), action: {
                    viewModel.alert = nil
                })
            )
        }.alert(isPresented: $showDeleteConfirmation){
            
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
                                viewModel.removeDailyReport(dailyReportId: pendingId, horseId: horse.id?.stringValue ?? "")
                            }
                        )
                    )
//            viewModel.removeDailyReport(dailyReportId: String(daily.id),horseId: horse.id?.stringValue ?? "")

        }
        
    }
    
    func showConfirmation(_ callback: () -> Void){
        showDeleteConfirmation = true
        
    }
    
}

struct DailyTabView_Previews: PreviewProvider {
    static var previews: some View {
        DailyTabView(horse: Horse(id: 1, stableId: 1, farmId: 1, trainingFarmId: 1, user: nil, stable: nil, ownerName: nil, farmName: nil, trainingFarmName: nil, birthDate: nil, sex: nil, age: nil, name: "TestHorse", color: nil, class_: nil, fatherName: nil, motherName: nil, motherFatherName: nil, totalStake: nil, notes: nil, createdAt: nil, updatedAt: nil))
    }
}

extension View {
    
    /// Hide or show the view based on a boolean value.
    ///
    /// Example for visibility:
    ///
    ///     Text("Label")
    ///         .isHidden(true)
    ///
    /// Example for complete removal:
    ///
    ///     Text("Label")
    ///         .isHidden(true, remove: true)
    ///
    /// - Parameters:
    ///   - hidden: Set to `false` to show the view. Set to `true` to hide the view.
    ///   - remove: Boolean value indicating whether or not to remove the view.
    @ViewBuilder func isHidden(_ hidden: Bool, remove: Bool = false) -> some View {
        if hidden {
            if !remove {
                self.hidden()
            }
        } else {
            self
        }
    }
}
