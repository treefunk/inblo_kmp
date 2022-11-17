//
//  SignInView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/18/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared
import KeyboardObserving

struct SignInView: View {
    
    @State private var username: String = ""
    @State private var password: String = ""

    @SceneStorage("loginTabIndex") var loginTab = LoginView.LoginTab.signInTab
    
    @ObservedObject var viewModel: LoginViewModel
    
    private let getUserModule = UserModule(networkModule: NetworkModule())

    
    init() {
        self.viewModel = LoginViewModel(
            loginUser: getUserModule.loginUser,
            logoutUser: getUserModule.logoutUser,
            registerUser: getUserModule.registerUser,
            getHorseStables: getUserModule.getHorseStables
        )
    }

    var body: some View {
        
        let signInResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )

            VStack(alignment: .center, spacing: 45) {
                Text("Login Account")
                    .foregroundColor(.black)
                    .font(.system(size: 27, weight: .semibold))
                VStack(alignment: .center, spacing: 20) {
                    TextView(text: $username, labelText: "ユーザー名", placeHolder: "").frame(width: .infinity, height: 65)
                    
                    TextView(text: $password, labelText: "パスワード", placeHolder: "",isSecureTextEntry: true).frame(width: .infinity, height: 65)
                        
//                    InbloTextField(placeHolder: "Username", bindingString: $username)
//                    InbloSecureField(placeHolder: "Password", bindingString: $password)
//                    
                    HStack {
                        Spacer()
                        Button(action: {
                            loginTab = LoginView.LoginTab.forgotPasswordTab
                        }){
                            Text("パスワードをお忘れですか？")
                                .foregroundColor(.black)
                                .fontWeight(.bold)
                        }
                        
                    }
                    
                    Button(action: {
                        viewModel.signInUser(loginName: username, password: password)
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
                            Text("サインイン")
                                .font(.system(size: 21, weight: .bold))
                                .foregroundColor(.white)
                        }.frame(width: .infinity, height: 70)
                    })
                        .padding(.top,35)
                    
                    HStack(alignment: .center, spacing: 2) {
//                        Text("アカウントを新規作成する")
//                            .foregroundColor(.secondary)
                        Button(action: {
                            loginTab = LoginView.LoginTab.signUpTab
                        }){
                            Text("アカウントを新規作成する")
                                .fontWeight(.bold)
                                .foregroundColor(.black)
                        }

                        
                    }
                    
                    
                } //:VSTACK
            }
            .keyboardObserving()
            .padding(.all)
            .alert(isPresented: signInResult ){
                Alert(
                    title: Text(viewModel.alert!.title),
                    message: Text(viewModel.alert!.body),
                    dismissButton: .default(Text("OK"), action: {
                        if(viewModel.alert?.type == AlertContent.AlertType.success){
                            
                        }
                        viewModel.alert = nil
                    })
                )
            }
    }
}

struct SignInView_Previews: PreviewProvider {
    static var previews: some View {
        SignInView()
    }
}








