//
//  HorseDetailView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/10/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HorseDetailView: View {
    
    
    @AppStorage("stableId") var stableId: Int = 0
    
    var horse: Horse
    var isEditMode: Binding<Bool?>
    
    @State
    var horseId: Int = 0
    
    @State
    var horseName: String = ""
    
    @ObservedObject var viewModel: HorseDetailViewModel
    @EnvironmentObject var horseListViewModel: HorseListViewModel
    
    @State private var showAddHorseSheet = false
    @State private var showHorseDetailSheet = false
    
    private let horseDetailModule = HorseDetailModule(networkModule: NetworkModule())
    private let getHorseModule = GetHorseListModule(networkModule: NetworkModule())

    
    var formattedAge: String {
        var age = horse.age == -1 ? 0 : (horse.age ?? 0)
        if(Int(age) < 1){
            return ""
        }
        return String(Int(age))
    }

    
    init(horse: Horse, isEditMode: Binding<Bool?>){
        self.horse = horse
        self.isEditMode = isEditMode
        let stringHorseId = horse.id?.stringValue ?? ""
       
        self.viewModel = HorseDetailViewModel(
            horseId:stringHorseId,
            getDailyReportForm: horseDetailModule.getDailyReportForm,
            getHorseDailyReports: horseDetailModule.getHorseDailyReports,
            getHorseTreatments: horseDetailModule.getHorseTreatments,
            addDailyReport: horseDetailModule.addHorseDailyReport,
            addHorseTreatment: horseDetailModule.addHorseTreatment,
            deleteHorseDailyReport: horseDetailModule.deleteHorseDailyReport,
            deleteHorseTreatment: horseDetailModule.deleteHorseTreatment,
            activeUrl: ApiSettings.companion.activeUrl
        )
        
//        self.horseListViewModel = HorseListViewModel(
//            searchHorses: getHorseModule.searchHorses,
//            getUserNames: getHorseModule.getUsernames,
//            addHorse: getHorseModule.addHorse
//        )
        
    }
    
    
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
//    var horse: Horse
    let horseTabs = [
        
        MaterialTabs.MaterialTabItem(UITabBarItem(title: "日報", image: UIImage(named: "ic-condition"), selectedImage: UIImage(named: "ic-condition-active")), { selected in
//                    selectedTab = selected.title ?? ""
        }),
        
        MaterialTabs.MaterialTabItem(UITabBarItem(title: "健康管理", image: UIImage(named: "ic-treatment"), selectedImage: UIImage(named: "ic-treatment-active")), { selected in
//                    selectedTab = selected.title ?? ""
        }),
        MaterialTabs.MaterialTabItem(UITabBarItem(title: "カレンダー", image: UIImage(named: "ic-calendar"), selectedImage: UIImage(named: "ic-calendar-active")), { selected in
//                    selectedTab = selected.title ?? ""
        })
    ]
    @State var selectedTab: UITabBarItem?
    
    
    var body: some View {
        
        
        VStack(alignment: .leading, spacing: 0) {
            

        
            HStack(spacing: 0){
                ZStack{
                    Rectangle()
                        .fill(Color("ColorPrimaryDark"))
                        .frame(width:.infinity,height: 50)
                    HStack {
                        
                        Button(action: {
                            presentationMode.wrappedValue.dismiss()
                        }){
                            Image(systemName: "arrow.left")
                                .scaledToFit()
                                .foregroundColor(.white)
                                .frame(width: 50, height: 40)
                        }
                        Spacer()
                        
                    }//:HSTACK
                    Text("詳細情報")
                        .foregroundColor(.white)
                        .multilineTextAlignment(.center)
                }//:ZSTACK
            }//:HSTACK
            
           
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(.white)
                        .frame(width:.infinity)
                        .shadow(color: Color(red:0,green:0,blue:0,opacity:0.10), radius: 12, x: 6, y: 6)
                    
                    
                    HStack {
                        VStack(alignment: .leading, spacing: 4) {
                            HStack(alignment:.top, spacing:0) {
                                let horseName = horse.name!.count > 9 ? (horse.name?.substring(to: 9) ?? "") : (horse.name ?? "")
                                Text(horse.name ?? "")
                                    .lineLimit(1)
                                    .font(.system(size: UIScreen.main.bounds.width*0.049, weight: .bold))
                                    .frame(minWidth:0,maxWidth:.infinity)
                                    .padding(.leading,0)
                                
                                if(!horse.sex!.isEmpty && !formattedAge.isEmpty){
                                    Text("\(horse.sex ?? "")\(formattedAge)")
                                        .font(.system(size: 14, weight: .bold))
                                        .padding(.leading,4)
                                        .padding(.trailing,4)
                                        .padding(.top,2)
                                        .padding(.bottom,2)
                                        .foregroundColor(.white)
                                        .background(
                                            RoundedRectangle(cornerRadius: 8)
                                                .fill(Color("ColorButtonSmallAge"))
                                        )
                                        .multilineTextAlignment(.leading)
                                }
                                
                               
                                    Button(action: {

                                        showAddHorseSheet = true
                                    }, label: {
                                        Image(systemName: "square.and.pencil")
                                            .resizable()
                                            .scaledToFit()
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                            .frame(width: 15, height: 15)
                                    })
                                        .padding(.leading,4)
                                        .padding(.bottom,10)

                                
                                
                            }
                            
        
                            
//                            Button(action: {
//                                showHorseDetailSheet = true
//                            }, label: {
//                                ZStack {
//                                    Capsule()
//                                        .fill(Color("ColorButtonSmallGray"))
//                                    HStack {
//                                        Text("出走履歴")
//                                            .foregroundColor(.white)
//                                            .font(.system(size: 14, weight: .bold))
//                                            .lineLimit(1)
//                                            .fixedSize(horizontal: true, vertical: false)
//
//                                        Image(systemName: "square.and.pencil")
//                                            .resizable()
//                                            .scaledToFit()
//                                            .foregroundColor(.white)
//                                            .frame(width: 13, height: 13)
//                                    }
//                                    .padding(.leading,18)
//                                    .padding(.trailing,18)
//                                    .padding(.top,4)
//                                    .padding(.bottom,4)
//                                    .layoutPriority(1)
//                                }//:ZSTACK
//                            })
                            
                        }//:VSTACK
                        
                        Spacer()
                        
                        Button(action: {
                            showHorseDetailSheet = true
                        }, label: {
                            ZStack {
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(lineWidth: 0.7)
                                    .foregroundColor(Color("ColorPrimaryDark"))
                                    
                                Text("詳細を見る")
                                    .font(.system(size: 11, weight: .semibold))
                                    .padding(.top,11)
                                    .padding(.bottom,11)
                                    .padding(.leading,6)
                                    .padding(.trailing,6)
                                    .layoutPriority(1)
                            }
                        })
                        
//                        Spacer()
                        
                    }//:HSTACK
                    .padding(.all,16)
                    .layoutPriority(1)
                }//:ZSTACK
                .padding(.all,12)
                .layoutPriority(1)
                
        
            MaterialTabs(tabItems: horseTabs,selected: $selectedTab)
            .frame(height:70)
                
            
            
            
            if(selectedTab == nil || selectedTab?.title == "日報"){
                DailyTabView(horse: horse)
                    .onAppear{
                        let horseId = horse.id?.stringValue ?? ""
                        self.viewModel.loadDailyReports(horseId: horseId)
                    }
                    .environmentObject(viewModel)
            }else if(selectedTab?.title == "健康管理"){
                OthersView(horse: horse)
                    .onAppear {
                        let horseId = horse.id?.stringValue ?? ""
                        self.viewModel.loadHorseTreatments(horseId: horseId)
                    }
                    .environmentObject(viewModel)
            }else if(selectedTab?.title == "カレンダー"){
                CalendarView(calendar: Calendar(identifier: .gregorian),horseId: self.$horseId, horseName: self.$horseName)
            }
            Spacer()
           
                
                
                    
            
                
            
        }//:VSTACK
        .navigationBarTitle("")
        .navigationBarBackButtonHidden(true)
        .navigationBarHidden(true)
        .background(Color("ColorDarkBackground"))
        .fullScreenCover(isPresented: $showAddHorseSheet) {
            AddHorseSheetView(isPresented: $showAddHorseSheet,users: horseListViewModel.users, updateList: nil,activeHorse: horse)
                .environmentObject(horseListViewModel)
        }
        .fullScreenCover(isPresented:$showHorseDetailSheet ){
            ScrollView {
                VStack {
                HStack {
                    Spacer()
            
                    Button(action: {
                        showHorseDetailSheet = false
                    }, label: {
                        Image(systemName: "xmark")
                            .foregroundColor(Color("ColorPrimaryDark"))
                    })
                }
                .padding(.vertical,16)
                .padding(.trailing,16)
                
                Spacer()
                
                HStack {
                    Spacer()
                    Text("管理馬の詳細")
                        .font(Font.custom("roboto_bold", size: 16))
                    Spacer()
                }
                .padding(.bottom,12)
                GroupBox {
                    VStack {
                
                        Group {
                            HStack {
                                Text("馬名:") //HORSE NAME
                                Spacer()
                                Text(horse.name ?? "")
                            }
                            Divider().padding(.vertical,4)
                            HStack {
                                Text("厩舎名:") // STABLE NAME
                                Spacer()
                                Text(horse.stable?.name ?? "")
                            }
                            Divider().padding(.vertical,4)
                            HStack {
                                Text("担当者:") // PERSON IN CHARGE
                                Spacer()
                                Text(horse.user?.username ?? "")
                            }
                            Divider().padding(.vertical,4)
                            HStack {
                                Text("馬主名:") // OWNER NAME
                                Spacer()
                                Text(horse.ownerName ?? "")
                            }
                            Divider().padding(.vertical,4)
                            HStack {
                                Text("生産牧場名:") // FARM NAME
                                Spacer()
                                Text(horse.farmName ?? "")
                            }
                            Divider().padding(.vertical,4)
                        }
                            
                        Group {
                            HStack {
                                Text("育成場名:") // TRAINING FARM name
                                Spacer()
                                Text(horse.trainingFarmName ?? "")
                            }
                            Divider().padding(.vertical,4)
                            HStack {
                                Text("性齢:") // SEXUAL AGE
                                Spacer()
                                Text("\(horse.sex ?? "")\(formattedAge)")
                            }
                            Divider().padding(.vertical,4)

                            HStack {
                                Text("毛色:") // COLOR
                                Spacer()
                                Text(horse.color ?? "")
                            }
                            Divider().padding(.vertical,4)

                            HStack {
                                Text("クラス:") // CLASS
                                Spacer()
                                Text(horse.class_ ?? "")
                            }
                            Divider().padding(.vertical,4)

                        }
                        
                        Group {
                            HStack {
                                Text("父:") //FATHER
                                Spacer()
                                Text(horse.fatherName ?? "")
                            }
                            
                            Divider().padding(.vertical,4)
                            
                            HStack {
                                Text("母:")//MOTHER
                                Spacer()
                                Text(horse.motherName ?? "")
                            }
                            Divider().padding(.vertical,4)

                            HStack {
                                Text("母父:") //mofa
                                Spacer()
                                Text(horse.motherFatherName ?? "")
                            }
                            Divider().padding(.vertical,4)

                            HStack {
                                Text("総賞金:") //TOTAL STAKE
                                Spacer()
                                Text(String(Float(horse.totalStake ?? 0.0)))
                            }
                            Divider().padding(.vertical,4)

                        }
                        
                        
                        HStack {
                            Text("メモ:") //MEMO
                            Spacer()
                            Text(horse.notes ?? "")
                        }
                        
                    }//:VSTACK
                    
                }//: GROUPBOX
                .padding(.horizontal,16)
                
                Spacer()
                }//:VStack
            }//: ScrollView
        }
        .onAppear(perform: {
            if(isEditMode.wrappedValue != nil){
                print("edittag is \(isEditMode.wrappedValue)")
                showAddHorseSheet = true
                isEditMode.wrappedValue = nil
            }
            self.horseId = horse.id?.intValue ?? 0
            self.horseName = horse.name ?? ""
            self.viewModel.loadDailyReportForm(stableId: String(stableId))
        })
        
       
    }
        

}

struct HorseDetailView_Previews: PreviewProvider {
    static var previews: some View {
        HorseDetailView(horse: Horse(id: 1, stableId: 1, farmId: 1, trainingFarmId: 1, user: nil, stable: nil, ownerName: nil, farmName: nil, trainingFarmName: nil, birthDate: nil, sex: nil, age: nil, name: "TestHorse", color: nil, class_: nil, fatherName: nil, motherName: nil, motherFatherName: nil, totalStake: nil, notes: nil, createdAt: nil, updatedAt: nil),isEditMode: .constant(false))
    }
}
