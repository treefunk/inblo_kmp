//
//  LoginView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/27/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct LoginView: View {
    
    @SceneStorage("loginTabIndex") var loginTab = LoginTab.signInTab
    
    enum LoginTab: String {
        case signUpTab
        case signInTab
        case forgotPasswordTab
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .center, spacing: 10) {
                Image("img_login_banner")
                    .resizable()
                    .scaledToFit()
                
                if(loginTab == .signInTab){
                    SignInView()
                }else if(loginTab == .signUpTab){
                    SignUpView()
                }else if(loginTab == .forgotPasswordTab){
                    ForgotPasswordView()
                }
                Spacer()
            }//:VSTACK
            
        }.ignoresSafeArea()
        
    }
    
    

}


struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
