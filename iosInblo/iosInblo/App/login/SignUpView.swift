//
//  SignUpView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/21/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared
import KeyboardObserving

struct SignUpView: View {
    
    @State private var selected: Int32 = 2
    @State private var firstName = ""
    @State private var lastName = ""
    @State private var raceTrackName = ""
    @State private var username = ""
    @State private var password = ""
    @State private var email = ""
//    @State private var stableName = ""
//    @State private var stableId = 0
    @State private var stableCode = ""
    
    @State private var showAlert = false
    @State private var alertTitle = ""
    @State private var alertContent = ""
    
    @State private var showStableModal = false
    
    
    
    
    private let getUserModule = UserModule(networkModule: NetworkModule())
    
    
    
    @SceneStorage("loginTabIndex") var loginTab = LoginView.LoginTab.signUpTab
    

    @ObservedObject var viewModel: LoginViewModel
    
    init() {
        self.viewModel = LoginViewModel(
            loginUser: getUserModule.loginUser,
            logoutUser: getUserModule.logoutUser,
            registerUser: getUserModule.registerUser,
            getHorseStables: getUserModule.getHorseStables
        )
    }
    
    var body: some View {
            
        let signUpResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        
                
                ScrollView {

                    VStack(alignment: .center, spacing: 18) {
                        Text("Register")
                            .foregroundColor(.black)
                            .font(.system(size: 27, weight: .semibold))
                        
                        Group {
                            
                        
                        HStack {
                            Text("役割は？")
                                .foregroundColor(.black)
                            .padding(.trailing,15)
                            Spacer()
                        }
                        HStack {
                            
                            
                            RadioButtonField(
                                id: 2,
                                label: "調教師",
                                color: .black,
                                isMarked: $selected.wrappedValue == 2,
                                callback: { self.selected = Int32(Int($0)) }
                            )
                            
                            RadioButtonField(
                                id: 1,
                                label: "担当者",
                                color: .black,
                                isMarked: $selected.wrappedValue == 1,
                                callback: { self.selected = Int32(Int($0)) }
                            )

                            Spacer()
                        }
                        
//                            TextView(text: $raceTrackName,labelText: "競馬場名", placeHolder: "").frame(width: .infinity, height: 65)
                            
                            
                        TextView(text: $username,labelText: "ユーザー名", placeHolder: "").frame(width: .infinity, height: 65)
                        
                        TextView(text: $password, labelText: "パスワードを設定", placeHolder: "",isSecureTextEntry: true).frame(width: .infinity, height: 65)
                        
                        VStack {
                            HStack {
                                TextView(text: $firstName, labelText: "姓", placeHolder: "").frame(width: .infinity, height: 65)
                                
                                TextView(text: $lastName, labelText: "名", placeHolder: "").frame(width: .infinity, height: 65)
                            }
                        }
                        
                        TextView(text: $email, labelText: "Eメール", placeHolder: "").frame(width: .infinity, height: 65)
                            
                            TextView(text: $stableCode, labelText: "厩舎コード", placeHolder: "厩舎コード").frame(width: .infinity, height: 65)
                        }
                        
                        
                        
                        
                        
//                        Menu(content: {
//                                ForEach(viewModel.stables.indices, id: \.self){ index in
//
//                                        Button(action: {
//                                            stableName = viewModel.stables[index].name
//                                            stableId = Int(viewModel.stables[index].id)
//                                        }, label: {
//                                            Text(viewModel.stables[index].name)
//                                        })
//
//                                }
//                        }, label: {
//                            TextView(text: $stableName, labelText: "厩舎名", placeHolder: "",isEditable: false).frame(maxWidth: UIScreen.main.bounds.width).frame(height:65)
//                        })


                        
                        Button(action: {
                            print("username is \(username)")
                            viewModel.signUpUser(
                                firstName: firstName,
                                lastName: lastName,
                                email: email,
                                username: username,
                                password: password,
                                userType: Int32(selected),
//                                stableId: Int32(stableId)
                                stableCode: stableCode
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
                                Text("登録")
                                    .font(.system(size: 21, weight: .bold))
                                    .foregroundColor(.white)
                            }.frame(width: .infinity, height: 70)
                        })
                        
                        HStack(alignment: .center, spacing: 2) {
                            Text("既にアカウントをお持ちの方？")
                                .foregroundColor(.secondary)
                            Button(action: {
                                loginTab = LoginView.LoginTab.signInTab
                            }){
                                Text("ログイン")
                                    .fontWeight(.bold)
                                    .foregroundColor(.black)
                            }
                            
                        }
                        
                    
                        
                        
                    }//:VSTACK
                    .keyboardObserving()
                    .padding(.all)
                }//:SCROLLVIEW
                .alert(isPresented: signUpResult ){
                    Alert(
                        title: Text(viewModel.alert!.title),
                        message: Text(viewModel.alert!.body),
                        dismissButton: .default(Text("OK"), action: {
                            if(viewModel.alert?.type == AlertContent.AlertType.success){
                                loginTab = LoginView.LoginTab.signInTab
                            }
                            viewModel.alert = nil
                        })
                    )
                }
    }
}

struct SignUpView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}
