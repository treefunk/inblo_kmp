//
//  HorseArchivesList.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 3/22/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HorseArchivesList: View {
    @AppStorage("userId") var userId: Int = 0
    @AppStorage("stableId") var stableId: Int = 0
    
    @AppStorage("horseArchiveMode") var horseArchiveMode: Bool = false

    @ObservedObject var viewModel: HorseListViewModel
    @State private var selection: String? = nil
    @State private var isEditMode: Bool? = nil

    private let getHorseModule = GetHorseListModule(networkModule: NetworkModule())

    
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
        NavigationView {
            VStack(alignment: .trailing, spacing: 0){
            
                HStack(spacing: 8){
                    ZStack{
                        
                        Rectangle()
                            .fill(Color("ColorPrimaryDark"))
                            .frame(width:.infinity,height: 50)
                        
                        HStack {
                            Text("馬のアーカイブ")
                                .foregroundColor(.white)
                                .padding(.leading,12)
                        
                            
                            Spacer()
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
                                        
                                        Image(systemName: "arrowshape.turn.up.backward")
                                            .foregroundColor(Color.white)
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: 25, height: 20)

                                        
                                    }.frame(width: .infinity, height: 40)
                                })
                                    .padding(.trailing,12)
                            
                        }//:HSTACK
                    }//:ZSTACK
                    
                }//:HSTACK

                ScrollView(showsIndicators: false) {
                    

                    LazyVStack(spacing:0) {
                        
                                ForEach(0..<viewModel.horses.count, id : \.self){ index in
//                                    if(!horseArchiveMode){
//                                        HorseItemRow(position: index + 1, for: viewModel.horses[index],horseListViewModel: viewModel,
//                                                     isShowingDetailView: $selection, isEditMode: $isEditMode,
//                                                     onClickHorse: { horse in
//                                                        let stringId = String(Int(horse.id ?? 0))
//                                                        selection = stringId == "0" ? nil : stringId
//                                                        isEditMode = true
//                                                        print("selected is -> \(selection)")
//                                                    },
//                                                     onArchiveHorse: { horse in
//                                                        viewModel.archiveHorse(horseId: String(Int(horse.id!)))
//                                                    })
//                                    }else{
                                        HorseItemRowArchive(position: index + 1, for: viewModel.horses[index], horseListViewModel: viewModel, onRestore: { horse in
                                            viewModel.restoreArchiveHorse(horseId: String(Int(horse.id!)))
                                        })
//                                    }
                                   
                                }//:FOREACH
                                
                        }
                }
                        

            }//: VSTACK
//
//            .onAppear{
//                horseArchiveMode = false
//            }
            .navigationBarTitle("")
            .navigationBarHidden(true)
            
        }//:navigation bar
        
        
        
        
    }
    
}

struct HorseArchivesList_Previews: PreviewProvider {
    static var previews: some View {
        HorseArchivesList()
    }
}
