//
//  HorseListViewModel.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/22/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

class HorseListViewModel: ObservableObject {
    
    @AppStorage("stableId") var stableId: Int = 0
    @AppStorage("userId") var userId: Int = 0
    @AppStorage("horseArchiveMode") var horseArchiveMode: Bool = false
    @Published var alert: AlertContent?

    
    let searchHorses: SearchHorses
    let getUserNames: GetUsernames
    let addHorse: AddHorse
    let archiveHorse: ArchiveHorse
    let restoreArchiveHorse: RestoreArchivedHorse
    
    @Published var horses = [Horse]()
    @Published var users = [UserDto]()
    
    init(
        searchHorses: SearchHorses,
        getUserNames: GetUsernames,
        addHorse: AddHorse,
        archiveHorse: ArchiveHorse,
        restoreArchiveHorse: RestoreArchivedHorse
    ){
        self.searchHorses = searchHorses
        self.getUserNames = getUserNames
        self.addHorse = addHorse
        self.archiveHorse = archiveHorse
        self.restoreArchiveHorse = restoreArchiveHorse

//        loadHorses()
        loadUserNames(stabId: String(stableId))
        
    }
    
    func loadHorses(
        isArchived: Bool = false
    ){
        do {
            searchHorses.invoke(
                offset: 0,
                query: nil,
                isArchived: isArchived,
                stableId: Int32(stableId)
            ).collectCommon(
                coroutineScope: nil,
                callback: { dataState in
                if dataState != nil {
                    
                    if(dataState is DataStateData){
                        
                        let horses = dataState?.value(forKey: "data") as! [Horse]
                        self.horses = horses
                    }
                
                }
            })
        }
    }
    
    func loadUserNames(stabId: String? = nil,
                       exId: String? = nil){
        do {
            getUserNames.invoke(stableId: stabId, excludeId: exId).collectCommon(coroutineScope: nil, callback: { dataState in
                if dataState != nil {
                    if(dataState is DataStateData){
                        print(dataState)
                        let getUserResponse = dataState?.value(forKey: "data") as! GetUsersResponse
                        self.users = getUserResponse.data
                    }
                }
                
            })
        }
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
        horseId: String? = nil
    ){
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        
        let formattedBirthDate = dateFormatterPrint.string(from: birthDate)
        let id = horseId == nil ? nil : horseId
//
        addHorse.invoke(name: name, userId: Int32(userId), stableId: Int32(stableId), ownerName: ownerName, farmName: farmName, trainingFarmName: trainingFarmName, sex: sex, color: color, _class: _class, birthDate: formattedBirthDate, father: father, mother: mother, motherFatherName: motherFatherName, totalStake: Double(totalStake) ?? 0.0, notes: notes, horseId: horseId)
            .collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    print("data: \(dataState)")
                    let data = dataState?.value(forKey: "data") as! Horse
                    
                    let message: String = {
                        if horseId == nil {
                            return "管理馬の登録が完了しました！"
                        }else{
                            return "管理馬の修正が完了しました！"
                        }
                    }()
                    
                    self.alert = AlertContent(title: "", body: message, type: getAlertTypeByCode(200))
                    self.loadHorses()
                }else{
                    print("error: \(dataState)")
                }
            })
    }
    
    func archiveHorse(
        horseId: String
    ){
        archiveHorse.invoke(horseId: horseId)
            .collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    print("data: \(dataState)")
                    let data = dataState?.value(forKey: "data") as! BooleanResponse
                    
                    let message = data.meta.message
                    
                    self.alert = AlertContent(title: "", body: message, type: getAlertTypeByCode(200))
                    self.loadHorses()
                }
            })
    }
    
    func restoreArchiveHorse(
        horseId: String
    ){
        restoreArchiveHorse.invoke(horseId: horseId)
            .collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    print("data: \(dataState)")
                    let data = dataState?.value(forKey: "data") as! BooleanResponse
                    
                    let message = data.meta.message
                    
                    self.alert = AlertContent(title: "", body: message, type: getAlertTypeByCode(200))
                    
                    self.loadHorses(isArchived: true)
                }
            })
    }
}
