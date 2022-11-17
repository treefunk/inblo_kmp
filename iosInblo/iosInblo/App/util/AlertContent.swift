//
//  AlertContent.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/27/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation

class AlertContent {
    var title: String
    var body: String
    var type: AlertType
    
    init(title: String,body: String,type: AlertType){
        self.title = title
        self.body = body
        self.type = type
    }
    
    enum AlertType{
        case success
        case error
    }
}

func getAlertTypeByCode(_ code: Int32) -> AlertContent.AlertType {
    switch(code){
        case 200,201:
        return AlertContent.AlertType.success
        default:
        return AlertContent.AlertType.error
    }
}

