//
//  MessagesView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/3/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct MessagesView: View {
    
    private var messagesModule =
        MessagesModule(networkModule: NetworkModule())
    private var horseListModule =
        GetHorseListModule(networkModule: NetworkModule())
    
    @AppStorage("userId")
    private var userId: Int = 0
    
    
    @ObservedObject
    var viewModel: MessagesViewModel
    
    @State
    private var activeMessage: Message? = nil
    @State private var showAddMessagesSheet = false
    @State private var showDeleteConfirmation = false
    @State private var pendingId = "0"
    
    init () {
        self.viewModel = MessagesViewModel(fetchMessages: messagesModule.getMessages, sendMessage: messagesModule.sendMessageUser, removeMessage: messagesModule.removeMessage, getUsernames: horseListModule.getUsernames, searchHorses: horseListModule.searchHorses)
    }
        
    
    var body: some View {
        let messagesViewResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        VStack(alignment: .trailing, spacing: 0){
        
            
            HStack(spacing: 8){
                ZStack{
                    
                    Rectangle()
                        .fill(Color("ColorPrimaryDark"))
                        .frame(width:.infinity,height: 50)
                    
                    HStack {
                        Text("メッセージ")
                            .foregroundColor(.white)
                            .padding(.leading,12)
//
//                        Image("IconHorse")
//                            .scaledToFit()
//                            .frame(width: 50, height: 40)
                        
                        Spacer()
                        
                        Button(action: {
                            showAddMessagesSheet = true
                            activeMessage = nil
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
                                Text("新規メッセージ")
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(.white)
                                    .padding(.top,10)
                                    .padding(.bottom,10)
                                    .padding(.trailing,24)
                                    .padding(.leading,24)
                                    .layoutPriority(1)
                                
                            }.frame(width: .infinity, height: 40)
                        })
                            .padding(.trailing,12)
                    }//:HSTACK
                }//:ZSTACK
                
            }//:HSTACK
        
        
        
        ScrollView {
            
            VStack(spacing: 0) {
                
//                Button(action: {
//                    activeMessage = nil
//                    showAddMessagesSheet = true
//                }, label: {
//                    ZStack {
//                        RoundedRectangle(cornerRadius: 12)
//                            .foregroundColor(.white)
//
//                        HStack {
//                            Image(systemName: "plus")
//                                .resizable()
//                                .scaledToFit()
//                                .foregroundColor(Color("ColorPrimary"))
//                                .frame(width: 12, height: 12)
//                            Text("状態を記録する")
//                                .font(.system(size: 13, weight: .regular))
//                                .padding(.top,8)
//                            .padding(.bottom,8)
//                        }
//                    }
//                    .padding(.leading,16)
//                    .padding(.trailing,16)
//                    .padding(.bottom,8)
//                })
                
                
                if(self.viewModel.messages.count > 0){
                    ScrollView(.horizontal,showsIndicators: false) {
                        

                        VStack(spacing: 0){
                            HStack(spacing:14) {
                                Group {
                                    Spacer(minLength: 55)
           
                                    Text("日付")//label date
                                        .font(.system(size: 12, weight: .semibold))
                                        .foregroundColor(.white)
                                        .frame(width:70)

                                    Text("時間")//label time
                                        .inbloTableLabel()

                                    
                                    Text("From")//label from
                                        .inbloTableLabel()
                                    
                                    Text("To")//label to
                                        .inbloTableLabel()
                                }
                                Group {
                                    Text("通知タイトル")//label title
                                        .inbloTableLabel()
                                    
                                    Text("通知タイプ")//label type
                                        .inbloTableLabel()
                                    
                                    Text("馬名")//label horse name
                                        .inbloTableLabel()
                                
                                }
                                Text("メモ")//label note
                                    .font(.system(size: 14, weight: .semibold))
                                    .foregroundColor(.white)
                                    .frame(width:150)
                                Spacer()
                            }
                            .padding(.top,4)
                            .padding(.bottom,4)
                            .background(Color("ColorPrimaryDark"))
                            .cornerRadius(8, corners: [.topLeft,.topRight])
                            .padding(.leading,16)
                            .padding(.trailing,16)
                            
                            
                                ScrollView {
                                    LazyVStack(spacing:0) {
                                        ForEach(self.viewModel.messages,id: \.self){ message in
                                            Text("\(activeMessage?.createdAt ?? "")").isHidden(true,remove: true) //WORKAROUND BUG STATE NOT ASSIGNING https://developer.apple.com/forums/thread/652080
                                            
                                            MessageRow(userId: userId, message: message, onClickEdit: { message in
                                                activeMessage = message
                                                if(activeMessage != nil){
                                                    showAddMessagesSheet = true
                                                }
                                            }, onClickRemove: { message in
                                                pendingId = String(message.id) ?? "0"
                                                showDeleteConfirmation = true
                                            })
                                            
                                        }
                                    }
                                }.frame(maxHeight:.infinity)
                            
                                    
                            
                        }// :VSTACK
                       
                        
                    }//:SCROLLVIEW (horizontal)
                }
                
                
                

            }//:VSTACK
            .padding(.top,12)
            }
        }//:SCROLLVIEW
        .background(Color("ColorDarkBackground"))
        .fullScreenCover(isPresented: $showAddMessagesSheet){
            AddMessageSheetView(isPresented: $showAddMessagesSheet, activeMessage: activeMessage)
                .environmentObject(viewModel)
        }.alert(isPresented: messagesViewResult){
            Alert(
                title: Text(viewModel.alert!.title),
                message: Text(viewModel.alert!.body),
                dismissButton: .default(Text("OK"), action: {
                    viewModel.alert = nil
                })
            )
        }
        .onAppear(perform: {
            viewModel.loadMessages(userId: userId)
        }).alert(isPresented: $showDeleteConfirmation){
            
            Alert(title: Text("Are you sure you want to delete this?"),
                        message: Text(""),
                        primaryButton: .default(
                            Text("Cancel"),
                            action: {
                                
                            }
                        ),
                        secondaryButton: .destructive(
                            Text("Delete"),
                            action: {
                                print("pending ID is -> \(pendingId)")
                                viewModel.removeMessage(messageId: Int(pendingId) ?? 0, userId: userId)
                            }
                        )
                    )
//            viewModel.removeDailyReport(dailyReportId: String(daily.id),horseId: horse.id?.stringValue ?? "")

        }
    }
        
}

struct MessagesView_Previews: PreviewProvider {
    static var previews: some View {
        MessagesView()
    }
}
