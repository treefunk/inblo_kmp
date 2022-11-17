//
//  AddHorseSheetViewModel.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/9/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import SwiftUI

class AddHorseSheetViewModel: ObservableObject {
    
    @AppStorage("stableId") var stableId: Int = 0
    @Published var alert: AlertContent?
    
    let addHorse: AddHorse
    
    init(
        addHorse: AddHorse
    ){
        self.addHorse = addHorse
    }
    
    func addHorse(
        name: String,
        userId: Int,
        ownerName: String,
        farmName: String,
        trainingFarmName: String,
        sex: String,
        color: String,
        _class: String,
        birthDate: Date,
        father: String,
        mother: String,
        motherFatherName: String,
        totalStake: String,
        notes: String,
        horseId: Int? = nil
    ){
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        
        let formattedBirthDate = dateFormatterPrint.string(from: birthDate)
        
//
        
        addHorse.invoke(name: name, userId: Int32(userId), stableId: Int32(stableId), ownerName: ownerName, farmName: farmName, trainingFarmName: trainingFarmName, sex: sex, color: color, _class: _class, birthDate: formattedBirthDate, father: father, mother: mother, motherFatherName: motherFatherName, totalStake: Double(totalStake) ?? 0.0, notes: notes, horseId: nil)
            .collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    print("data: \(dataState)")
                    let data = dataState?.value(forKey: "data") as! Horse
                    self.alert = AlertContent(title: "Add Horse", body: "管理馬の登録が完了しました！", type: getAlertTypeByCode(200))
                }else{
                    print("error: \(dataState)")
                }
            })
        
    
    }
}
