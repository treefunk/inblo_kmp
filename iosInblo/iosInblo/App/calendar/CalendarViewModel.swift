//
//  CalendarViewModel.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 1/17/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class CalendarViewModel: ObservableObject {
    
    @AppStorage("stableId") var stableId: Int = 0

    
    let getAllEventsByMonthYear: GetAllEventsByMonthYear
    let addEvent: AddEventUseCase
    let deleteEvent: DeleteEventUseCase
    let getHorses: SearchHorses
    

    
    @Published var alert: AlertContent?

    
    @Published var events = [Event]()
    @Published var horses = [Horse]()
    
    @Published var activeEvents = [Event]()

    
    init(
        getAllEventsByMonthYear: GetAllEventsByMonthYear,
        addEvent: AddEventUseCase,
        deleteEvent: DeleteEventUseCase,
        getHorses: SearchHorses
    ){
        self.getAllEventsByMonthYear = getAllEventsByMonthYear
        self.addEvent = addEvent
        self.deleteEvent = deleteEvent
        self.getHorses = getHorses
        
        loadHorses()
    }
    
    
    func getAllEvents(
        stableId: Int,
        month: String,
        year: String,
        horseId: Int,
        onEndCall: (([Event]) -> Void)? = nil
    ) -> [Event]{
        getAllEventsByMonthYear.invoke(stableId: Int32(stableId), month: month, year: year, horseId: Int32(horseId)).collectCommon(coroutineScope: nil, callback: { dataState in
            
        if(dataState != nil) {
//                print("calendar datastate -> \(dataState)")
                if(dataState is DataStateData){
                    let events = dataState?.value(forKey: "data") as! [Event]
                    self.events = events
                    if(onEndCall != nil){
                        onEndCall!(events)
                    }
                    print("getAllEvents -> m:\(month) y:\(year) -> \(events.count)")
                }

            }
        })
        return self.events
    }
    
    func loadHorses(
        isArchived: Bool = false
    ){
        do {
            getHorses.invoke(
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
    
    func addCalendarEvent(
        horseId: Int? = nil,
        userId: Int,
        stableId: Int,
        dateStart: String,
        dateEnd: String,
        title: String,
        eventType: String,
        start: String,
        end: String,
        notes: String,
        eventId: String? = nil,
        cMonth: String,
        cYear: String,
        cHorseId: Int
    ){
        do {
            print("dateStart -> \(dateStart)")
            print("dateEnd -> \(dateEnd)")
            print("horseId -> \(horseId)")
            let horseId = horseId == nil || horseId == 0 ? nil : String(horseId!)

            addEvent.invoke(horseId: horseId, userId: Int32(userId), stableId: Int32(stableId), date: dateStart, dateEnd: dateEnd, title: title, eventType: eventType, start: start, end: end, memo: notes, eventId: eventId)
                .collectCommon(coroutineScope: nil, callback: { dataState in
                    if(dataState is DataStateData){
                        print("data: \(dataState)")
                        let data = dataState?.value(forKey: "data") as! Event
                        self.getAllEvents(stableId: stableId, month: cMonth, year: cYear, horseId: cHorseId)
                        
                        if(eventId != nil){
                            self.activeEvents = []
                        }
                        
                        let message: String = {
                            if eventId == nil {
                                return "Event Successfully Created!"
                            }else{
                                return "Event Successfully Updated!"
                            }
                        }()
                       
                        self.alert = AlertContent(title: "Event Added Successfully!", body: "", type: getAlertTypeByCode(200))
                        }
                    else{
                            print("error: \(dataState)")
                    }
                })
        }
    }
    
    func deleteCalendarEvent(
        eventId: Int,
        stableId: Int,
        cMonth: String,
        cYear: String,
        cHorseId: Int
    ){
        do {

//            let id = eventId == nil ? nil : Int32(eventId!)
            deleteEvent.invoke(eventId: Int32(eventId))
                .collectCommon(coroutineScope: nil, callback: { dataState in
                    if(dataState is DataStateData){
                        print("data: \(dataState)")
                        let data = dataState?.value(forKey: "data") as! BooleanResponse
                        
//                        self.alert = AlertContent(title: "Event Deleted Successfully", body: "", type: getAlertTypeByCode(200))
                        self.activeEvents = self.activeEvents.filter({ activeEvent in
                            if(activeEvent.id != eventId){
                                return true
                            }
                            return false
                        })
                        
                        self.getAllEvents(stableId: stableId, month: cMonth, year: cYear, horseId: cHorseId)
                        }else{
                            print("error: \(dataState)")
                    }
                })
        }
    }
}


//addHorse.invoke(name: name, userId: Int32(userId), stableId: Int32(stableId), ownerName: ownerName, farmName: farmName, trainingFarmName: trainingFarmName, sex: sex, color: color, _class: _class, birthDate: formattedBirthDate, father: father, mother: mother, motherFatherName: motherFatherName, totalStake: Double(totalStake) ?? 0.0, notes: notes, horseId: nil)
//    .collectCommon(coroutineScope: nil, callback: { dataState in
//        if(dataState is DataStateData){
//            print("data: \(dataState)")
//            let data = dataState?.value(forKey: "data") as! Horse
//            self.alert = AlertContent(title: "Add Horse", body: "管理馬の登録が完了しました！", type: getAlertTypeByCode(200))
//        }else{
//            print("error: \(dataState)")
//        }
//    })
