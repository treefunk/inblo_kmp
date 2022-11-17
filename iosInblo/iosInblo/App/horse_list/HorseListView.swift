//
//  HorseListView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/27/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HorseListView: View {
    
    @AppStorage("userId") var userId: Int = 0
    @AppStorage("stableId") var stableId: Int = 0
    
    @AppStorage("horseArchiveMode") var horseArchiveMode: Bool = false

    @ObservedObject var viewModel: HorseListViewModel
    @State private var selection: String? = nil
    @State private var isEditMode: Bool? = nil
    @State private var showArchiveConfirmation = false
    @State private var pendingId = "0"



    private let getHorseModule = GetHorseListModule(networkModule: NetworkModule())
    
    @State private var showAddHorseSheet = false
    @State var updater: Bool = false {
        didSet {
            print("old value is \(oldValue) new value is \(updater)")
            if(updater){
            
                viewModel.loadHorses()

            }
        }
    }
    
//    @State var showEditHorseDetail: Bool = false
    




    init() {
        self.viewModel = HorseListViewModel(
            searchHorses: getHorseModule.searchHorses,
            getUserNames: getHorseModule.getUsernames,
            addHorse: getHorseModule.addHorse,
            archiveHorse: getHorseModule.archiveHorse,
            restoreArchiveHorse: getHorseModule.restoreArchivedHorses
        )
    }
    
    var body: some View {
        
        let horseListResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        NavigationView {
            VStack(alignment: .trailing, spacing: 0){
            
                HStack(spacing: 8){
                    ZStack{
                        
                        Rectangle()
                            .fill(Color("ColorPrimaryDark"))
                            .frame(width:.infinity,height: 50)
                        
                        HStack {
                            Text(!horseArchiveMode ? "管理馬一覧" : "削除済管理馬一覧")
                                .foregroundColor(.white)
                                .padding(.leading,12)
                            
                            if(!horseArchiveMode){
                                Image("IconHorse")
                                    .scaledToFit()
                                    .frame(width: 50, height: 40)
                            }
                            
                            
                            Spacer()
                            if(!horseArchiveMode){
                                Button(action: {
                                    showAddHorseSheet = true
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
                                        Text("管理馬の追加")
                                            .font(.system(size: 14, weight: .bold))
                                            .foregroundColor(.white)
                                            .padding(.top,10)
                                            .padding(.bottom,10)
                                            .padding(.trailing,24)
                                            .padding(.leading,24)
                                            .layoutPriority(1)
                                        
                                    }.frame(width: .infinity, height: 40)
                                })
                                    .padding(.trailing,12)
                            }else{
                                Button(action: {
                                    horseArchiveMode = false
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
                                        Text("リストに戻る")
                                            .font(.system(size: 14, weight: .bold))
                                            .foregroundColor(.white)
                                            .padding(.top,10)
                                            .padding(.bottom,10)
                                            .padding(.trailing,24)
                                            .padding(.leading,24)
                                            .layoutPriority(1)
                                        
//                                        Image(systemName: "arrowshape.turn.up.backward")
//                                            .foregroundColor(Color.white)
//                                            .aspectRatio(contentMode: .fit)
//                                            .frame(width: 25, height: 20)

                                        
                                    }.frame(width: .infinity, height: 40)
                                })
                                    .padding(.trailing,12)
                            }
                            
                        }//:HSTACK
                    }//:ZSTACK
                    
                }//:HSTACK
                .alert(isPresented: horseListResult){
                    Alert(
                        title: Text(viewModel.alert!.title),
                          message: Text(viewModel.alert!.body),
                        dismissButton: .default(Text("OK"),
                                                action: {
                                                    viewModel.alert = nil
                                                    
                                                })
                    )
                    
                }

                ScrollView(showsIndicators: false) {
                    

                    LazyVStack(spacing:0) {
                        
                                ForEach(0..<viewModel.horses.count, id : \.self){ index in
                                    if(!horseArchiveMode){
                                        HorseItemRow(position: index + 1, for: viewModel.horses[index],horseListViewModel: viewModel,
                                                     isShowingDetailView: $selection, isEditMode: $isEditMode,
                                                     onClickHorse: { horse in
                                                        let stringId = String(Int(horse.id ?? 0))
                                                        selection = stringId == "0" ? nil : stringId
                                                        isEditMode = true
                                                        print("selected is -> \(selection)")
                                                    },
                                                     onArchiveHorse: { horse in
                                                        self.pendingId = String(Int(horse.id!))
                                                        showArchiveConfirmation = true
                                                    })
                                            .alert(isPresented: $showArchiveConfirmation){
                                                    
                                                    Alert(title: Text("Archive this horse?"),
                                                                message: Text(""),
                                                                primaryButton: .default(
                                                                    Text("Cancel"),
                                                                    action: {
                                                                       
                                                                    }
                                                                ),
                                                                secondaryButton: .destructive(
                                                                    Text("Confirm"),
                                                                    action: {
                                                                        viewModel.archiveHorse(horseId: self.pendingId)
                                                                    }
                                                                )
                                                            )
                                            }
                                    }else{
                                        HorseItemRowArchive(position: index + 1, for: viewModel.horses[index], horseListViewModel: viewModel, onRestore: { horse in
                                            self.pendingId = String(Int(horse.id!))
                                            showArchiveConfirmation = true
                                        }).alert(isPresented: $showArchiveConfirmation){
                                                
                                                Alert(title: Text("Restore this horse?"),
                                                            message: Text(""),
                                                            primaryButton: .default(
                                                                Text("Cancel"),
                                                                action: {
                                                                    
                                                                }
                                                            ),
                                                            secondaryButton: .destructive(
                                                                Text("Confirm"),
                                                                action: {
                                                                    viewModel.restoreArchiveHorse(horseId: pendingId)
                                                                }
                                                            )
                                                        )
                                        }
                                    }
                                   
                                }//:FOREACH
                                
                        }

                }
                        

            }//: VSTACK
            .background(Color.white)
            .onChange(of: horseArchiveMode){ horseArchive in
                print("horse archive is \(horseArchive)")
                viewModel.loadHorses(isArchived: self.horseArchiveMode)
            }
            .onAppear{
                if(horseArchiveMode != true){
                    horseArchiveMode = false
                    viewModel.loadHorses()
                }
            }
            
            
            .navigationBarTitle("")
            .navigationBarHidden(true)
            .fullScreenCover(isPresented: $showAddHorseSheet) {
                AddHorseSheetView(isPresented: $showAddHorseSheet,users: viewModel.users, updateList: $updater)
                    .environmentObject(viewModel)
            }
            
        }//:navigation bar
        
        
        
        
    }
    
}

struct HorseListView_Previews: PreviewProvider {
    static var previews: some View {
        HorseListView()
    }
}

//TODO: refresh horse list after creating a horse
