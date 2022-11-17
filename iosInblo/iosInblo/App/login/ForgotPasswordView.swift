//
//  ForgotPasswordView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/21/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ForgotPasswordView: View {
    
    @State private var email = ""
    
    @SceneStorage("loginTabIndex") var loginTab = LoginView.LoginTab.forgotPasswordTab
    
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
            VStack(alignment: .center, spacing: 45) {
                Text("Reset Password")
                    .foregroundColor(.black)
                    .font(.system(size: 27, weight: .semibold))
                
                VStack(alignment: .center, spacing: 6){
                    Text("アカウントで使用しているメールを")
                        .foregroundColor(.secondary)
                        .fontWeight(.semibold)
                    Text("以下に入力してください。")
                        .foregroundColor(.secondary)
                        .fontWeight(.semibold)
                }
                
                VStack(alignment: .center, spacing: 20) {

                    TextView(text: $email, labelText: "Eメール", placeHolder: "").frame(width: .infinity, height: 65)
                        
                    
                    Button(action: {
                        
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
                            Text("パスワードをリセット")
                                .font(.system(size: 21, weight: .bold))
                                .foregroundColor(.white)
                        }.frame(width: .infinity, height: 70)
                    })
                        .padding(.top,10)
                    
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

                } //:VSTACK
            } //:VSTACK
            .padding(.all)
    }
}

struct ForgotPasswordView_Previews: PreviewProvider {
    static var previews: some View {
        ForgotPasswordView()
    }
}
