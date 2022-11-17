//
//  AddHorseSheetView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/5/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AddHorseSheetView: View {
    
    var isPresented: Binding<Bool>
    var users: [UserDto]
    var updateList: Binding<Bool>?
    
    @State var showErrorAlert: Bool = false
    @State var errorAlertBody: String = ""
    
    var activeHorse: Horse? = nil
    
    private let getHorseModule = GetHorseListModule(networkModule: NetworkModule())
    
//    @ObservedObject private var viewModel: AddHorseSheetViewModel
    @EnvironmentObject var viewModel: HorseListViewModel



    @State private var horseName = ""
    @State private var personInChargeId = 0
    @State private var personInCharge = ""
    @State private var ownerName = ""
    @State private var farmName = ""
    @State private var trainingFarmName = ""
    @State private var birthDate = ""
    @State private var bDate = Date()
    @State private var sex = ""
    @State private var color = ""
    @State private var class_ = ""
    @State private var father = ""
    @State private var mother = ""
    @State private var fatherAndMother = ""
    @State private var totalStake = ""
    @State private var notes = ""
    
    let dateAndTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()
    
    init (isPresented: Binding<Bool>,users: [UserDto],updateList: Binding<Bool>?,activeHorse: Horse? = nil){
        self.isPresented = isPresented
        self.users = users
        self.updateList = updateList
        self.activeHorse = activeHorse
//        self.viewModel = AddHorseSheetViewModel(addHorse: getHorseModule.addHorse)
    }
    
    
    var body: some View {
        
        let addHorseResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        
        ScrollView {
            VStack(spacing: 25) {
                
                HStack {
                    Spacer()
                        .frame(maxWidth: .infinity)
                    Text("管理馬の詳細")
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
                }
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
                    TextView(text: $horseName, labelText: "馬名*", placeHolder: "馬名*", maxCharacters: 9)
                    
                    Menu(content: {
                        ForEach(self.users.indices, id: \.self){ index in

                                Button(action: {
                                    personInCharge = self.users[index].username!
                                    personInChargeId = Int(self.users[index].id!)
                                }, label: {
                                    Text(self.users[index].username!)
                                })
                            
                        }
                    }, label: {
                        TextView(text: $personInCharge, labelText: "担当者", placeHolder: "担当者",isEditable: false)
                    })
                    
                    
                    TextView(text: $ownerName, labelText: "馬主名", placeHolder: "馬主名")
                    TextView(text: $farmName, labelText: "生産牧場名", placeHolder: "生産牧場名")
                }
                Group {
                    TextView(text: $trainingFarmName, labelText: "育成場名", placeHolder: "育成場名")
                    
                        DatePicker(
                            "生年月日",
                            selection: $bDate,
                            displayedComponents: [.date]
                        ).datePickerStyle(.automatic)
                    
//                    TextView(text: $birthDate, labelText: "生年月日", placeHolder: "生年月日")
                    
                    Menu(content: {
                        ForEach(AddHorseDropDownData.genders, id: \.self ){ gender in
                            Button(action: {
                                sex = gender
                            }, label: {
                                Text(gender)
                            })
                        }
                    }, label: {
                        TextView(text: $sex, labelText: "性", placeHolder: "性",isEditable: false)
                    })
                    
                    
                    Menu(content: {
                        ForEach(AddHorseDropDownData.colors, id: \.self ){ c in
                            Button(action: {
                                color = c
                            }, label: {
                                Text(c)
                            })
                        }
                    }, label: {
                        TextView(text: $color, labelText: "毛色", placeHolder: "毛色", isEditable: false)
                    })
                    
                    TextView(text: $class_, labelText: "クラス", placeHolder: "クラス")
                }
                
                Group {
                    TextView(text: $father, labelText: "父" , placeHolder: "父")
                    TextView(text: $mother, labelText: "母", placeHolder: "母")
                    TextView(text: $fatherAndMother, labelText: "母父", placeHolder: "母父")
                    TextView(text: $totalStake, labelText: "総賞金", placeHolder: "総賞金")
                }
                Group {
                    TextView(text: $notes, labelText: "メモを書く...", placeHolder: "メモを書く...")
                }
                
                Button(action: {
                    if(horseName.isEmpty){
                        print("horse name is empty")
                        errorAlertBody = "馬名を入力してください。"
                        showErrorAlert.toggle()
                        return
                    }
                    let activeHorseId = Int(activeHorse?.id ?? -1)
                    let horseId = activeHorseId == -1 ? nil : String(activeHorseId)
//                    print(activeHorseId)
                    viewModel.addHorse(name: horseName, userId: personInChargeId, ownerName: ownerName, farmName: farmName, trainingFarmName: trainingFarmName, sex: sex, color: color, _class: class_, birthDate: bDate, father: father, mother: mother, motherFatherName: fatherAndMother, totalStake: totalStake, notes: notes, horseId: horseId)
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
                    .alert(isPresented: addHorseResult ){
                        Alert(
                            title: Text(viewModel.alert!.title),
                            message: Text(viewModel.alert!.body),
                            dismissButton: .default(Text("OK"), action: {
                                if(viewModel.alert?.type == AlertContent.AlertType.success){
                                    self.updateList?.wrappedValue = true
                                    self.isPresented.wrappedValue = false
                                }
                                viewModel.alert = nil
                            })
                        )
                    }
                    
            }
            .padding(.trailing,18)
            .padding(.leading,18)
        }
        
        .onAppear(perform: {
            if(activeHorse != nil){
                horseName = activeHorse?.name ?? ""
                personInCharge = activeHorse?.user?.username ?? ""
                personInChargeId = Int(activeHorse?.user?.id ?? 0)
                ownerName = activeHorse?.ownerName ?? ""
                farmName = activeHorse?.farmName ?? ""
                trainingFarmName = activeHorse?.trainingFarmName ?? ""
            
                
                let birthDate = dateAndTimeFormatter.date(from: activeHorse?.birthDate ?? "")
                if(birthDate != nil){
                    bDate = birthDate!
                }
                
                sex = activeHorse?.sex ?? ""
                color = activeHorse?.color ?? ""
                class_ = activeHorse?.class_ ?? ""
                father = activeHorse?.fatherName ?? ""
                mother = activeHorse?.motherName ?? ""
                fatherAndMother = activeHorse?.motherFatherName ?? ""
                totalStake = String(Float(activeHorse?.totalStake ?? 0.0))
                notes = activeHorse?.notes ?? ""
                
                
            }else{
                print("create mode")
            }
        })
        
    }
    
    struct AddHorseDropDownData {
        static var genders = ["牡","牝","騙"]
        static var colors = ["鹿毛","黒鹿毛","青鹿毛","青毛","芦毛","栗毛","栃栗毛","白毛"]
    }

}

struct AddHorseSheetView_Previews: PreviewProvider {
    static var previews: some View {
        AddHorseSheetView(isPresented: .constant(true),users: [],updateList: .constant(false))
    }
}

