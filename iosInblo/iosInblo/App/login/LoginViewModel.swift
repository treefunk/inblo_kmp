//
//  LoginViewModel.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/18/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation

import shared
import SwiftUI


class LoginViewModel: ObservableObject {
    
    let loginUser: LoginUser
    let logoutUser: LogoutUser
    let registerUser: RegisterUser
    let getHorseStables: GetHorseStables
    
    @Published var alert: AlertContent?
    @Published var stables: [DropdownData] = []
        


    init(
        loginUser: LoginUser,
        logoutUser: LogoutUser,
        registerUser: RegisterUser,
        getHorseStables: GetHorseStables
    ){
        self.loginUser = loginUser
        self.logoutUser = logoutUser
        self.registerUser = registerUser
        self.getHorseStables = getHorseStables
        
//        self.getStables()
    }
    
    func signUpUser(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        userType: Int32,
//        stableId: Int32
        stableCode: String
    ){
        do {
            registerUser.invoke(firstName: firstName, lastName: lastName, email: email, username: username, password: password, userType: userType, stableCode: stableCode)
                .collectCommon(coroutineScope: nil, callback: { dataState in
                    if(dataState is DataStateData){
                        let loginRes = dataState?.value(forKey: "data") as! LoginResponse
//                        if(loginRes.meta)
//                        self.loginResponse = loginRes
//                        if(login)
                        self.alert = AlertContent(title: "Sign Up", body: loginRes.meta.message, type: getAlertTypeByCode(loginRes.meta.code) )                        
                    }
                })
        }
    }
    
    func getStables(){
        do {
            getHorseStables.invoke()
                .collectCommon(coroutineScope: nil, callback: { dataState in
                    if(dataState is DataStateData){
                        let stables = (dataState?.value(forKey: "data") as! DropDownDataResponse).data
                        self.stables = stables
                    }
                })
        }
    }
    
    func signInUser(
        loginName: String,
        password: String
    ){
        do {
            loginUser.invoke(loginName: loginName, password: password)
                .collectCommon(coroutineScope: nil, callback: { dataState in
                    if(dataState is DataStateData){
                        let loginRes = dataState?.value(forKey: "data") as! LoginResponse
//                        if(loginRes.meta)
//                        self.loginResponse = loginRes
                        self.alert = AlertContent(title: "Sign In", body: loginRes.meta.message, type: getAlertTypeByCode(loginRes.meta.code) )
                        if(loginRes.meta.code == 200){
                            UserDefaults.standard.set(loginRes.data.id, forKey: "userId")
                            UserDefaults.standard.set(loginRes.data.stableId, forKey: "stableId")
                            UserDefaults.standard.set(loginRes.data.username, forKey: "username")
                            UserDefaults.standard.set(loginRes.data.roleId, forKey: "roleId")
                        }
                    }
                })
        }
    }
}
