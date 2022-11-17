//
//  MessagesViewModel.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 2/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class MessagesViewModel: ObservableObject {
    
    @AppStorage("stableId") var stableId: Int = 0
    @AppStorage("userId") var userId: Int = 0
    
    let fetchMessages: GetAllMessages
    let sendMessage: SendMessageToUser
    let removeMessage: RemoveMessage
    let getUsernames: GetUsernames
    let searchHorses: SearchHorses
    
    @Published var alert: AlertContent?

    @Published var messages = [Message]()
    @Published var users    = [UserDto]()
    @Published var horses   = [Horse]()
    
    init(
        fetchMessages: GetAllMessages,
        sendMessage: SendMessageToUser,
        removeMessage: RemoveMessage,
        getUsernames: GetUsernames,
        searchHorses: SearchHorses
    ){
        self.fetchMessages = fetchMessages
        self.sendMessage = sendMessage
        self.removeMessage = removeMessage
        self.getUsernames = getUsernames
        self.searchHorses = searchHorses
        
        self.loadUserNames(stabId: String(stableId), exId: String(userId))
        self.loadHorses()
        
    }
    
    
    func loadMessages(userId: Int){
        do {
            fetchMessages.invoke(userId: Int32(userId)).collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    let messages = dataState?.value(forKey: "data") as! [Message]
                    self.messages = messages
                    print(messages)
                }
            })
        }
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
    
    func loadUserNames(
        stabId: String? = nil,
        exId: String? = nil
    ){
        do {
            getUsernames.invoke(stableId: stabId, excludeId: exId).collectCommon(coroutineScope: nil, callback: { dataState in
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
    
    func removeMessage(
        messageId: Int,
        userId: Int
    ){
        do{
            removeMessage.invoke(messageId: Int32(messageId)).collectCommon(coroutineScope: nil, callback: { dataState in
                if dataState != nil {
                    if(dataState is DataStateData){
                        print(dataState)
                        let response = dataState?.value(forKey: "data") as! BooleanResponse
                        print(response)

                        self.alert = AlertContent(title: "", body: "message deleted successfully!", type: getAlertTypeByCode(200))
                        self.loadMessages(userId: userId)

                    }
                }
                
            })
        }
    }
    
    func addMessage(
        userId: Int,
        stableId: Int,
        senderId: Int,
        recipientId: String? = nil,
        horseId: String? = nil,
        horseName: String,
        notificationType: String,
        title: String,
        memo: String,
        isRead: String,
        messageId: String? = nil
    ){
        do {
            sendMessage.invoke(stableId: Int32(stableId), senderId: Int32(senderId), recipientId: recipientId, horseId: horseId, horseName: horseName, notificationType: notificationType, title: title, memo: memo, isRead: isRead, messageId: messageId).collectCommon(coroutineScope: nil, callback: { dataState in
                    if(dataState is DataStateData){
                        self.alert = AlertContent(title: "Add Message", body: "Message Added Successfully!", type: getAlertTypeByCode(200))
                        print(dataState)
                        let message = dataState?.value(forKey: "data") as! Message
                        
                        self.loadMessages(userId: userId)
                        print("alert is -> \(self.alert)")
                    }
            })
        }
    }
}
