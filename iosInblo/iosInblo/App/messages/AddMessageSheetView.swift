//
//  AddMessageSheetView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 2/11/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AddMessageSheetView: View {
    
    var isPresented: Binding<Bool>
    var activeMessage: Message? = nil
    
    @EnvironmentObject var viewModel: MessagesViewModel
    @AppStorage("userId") var userId: Int = 0
    @AppStorage("stableId") var stableId: Int = 0

    
    @State var showErrorAlert: Bool = false
    @State var errorAlertBody: String = ""
    
    @State var title: String = ""
    @State var type: String = ""
    @State var recipientType: Int = 0
    @State var recipient: String = ""
    @State var recipientId: String? = "0"
    @State var horseName: String = ""
    @State var horseId: String? = nil
    @State var memo: String = ""
    
    
    init(isPresented: Binding<Bool>, activeMessage: Message? = nil){
        self.isPresented = isPresented
        self.activeMessage = activeMessage
    }
    
    var body: some View {
        
        
        
        let addResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        ScrollView(showsIndicators: false) {
            VStack(spacing: 25){
                HStack {
                    Spacer()
                        .frame(maxWidth: .infinity)
                    Text("メッセージを作成する")
                        .frame(maxWidth: .infinity)
                    HStack {
                        Spacer()
                        Button(action: {
                            self.isPresented.wrappedValue = false
                        }, label: {
                            Image("IconCloseRound")
                        })
                        .buttonStyle(PlainButtonStyle())
                    }
                }//:HSTACK
                
                Group {
                 
                    TextView(text: $title, labelText: "通知タイトル*", placeHolder: "通知タイトル*")
                    
                    
                    TextDropDownView(dropDownData: ["関係者連絡","状態関連","出走予定","調教相談","厩舎行事","その他"], labelText: "通知タイプ*", placeHolder: "通知タイプ*", text: $type)

                }
                
                Group {
                    
                    HStack {
                        
                        
                        RadioButtonField(
                            id: 0,
                            label: "全員",
                            color: .black,
                            isMarked: $recipientType.wrappedValue == 0,
                            callback: { self.recipientType = Int(Int32(Int($0))) }
                        )
                        
                        RadioButtonField(
                            id: -1,
                            label: "ユーザー名",
                            color: .black,
                            isMarked: $recipientType.wrappedValue == -1,
                            callback: { self.recipientType = Int(Int32(Int($0))) }
                        )

                        Spacer()
                    }.alert(isPresented: $showErrorAlert ){
                        Alert(
                            title: Text(""),
                            message: Text(errorAlertBody),
                            dismissButton: .default(Text("OK"), action: {
                                self.showErrorAlert = false
                            })
                        )
                    }
                    
                    if(recipientType == -1){
                        
                        Menu(content: {
                            ForEach(viewModel.users.indices, id: \.self){ index in

                                    Button(action: {
                                        recipient = viewModel.users[index].username!
                                        recipientId = String(Int(viewModel.users[index].id!))
                                    }, label: {
                                        Text(viewModel.users[index].username!)
                                    })
                                
                            }
                        }, label: {
                            TextView(text: $recipient, labelText: "ユーザー名*", placeHolder: "ユーザー名*",isEditable: false)
                        })
                        
                    }
                    
                    Menu(content: {
                        ForEach(viewModel.horses.indices, id: \.self){ index in

                                Button(action: {
                                    horseName = viewModel.horses[index].name!
                                    horseId = String(Int(viewModel.horses[index].id!))
                                }, label: {
                                    Text(viewModel.horses[index].name!)
                                })
                            
                        }
                    }, label: {
                        TextView(text: $horseName, labelText: "管理馬名", placeHolder: "管理馬名",isEditable: false)
                    })
                    
                    
                    
                
                    
//                    TextView(text: $trainingType, labelText: "", placeHolder: "調教内容", isEditable: true)
                    

                }
                
               
                
                TextView(text: $memo, labelText: "メモを書く...", placeHolder: "メモを書く...", isEditable: true)
                
                
                
              
                
                
                
                Button(action: {
                    var messageId = String(activeMessage?.id ?? 0)
                    
//                    すべての項目を入力してください。
                    if(title.isEmpty || type.isEmpty || (recipientType == -1 && recipient.isEmpty)){
                        errorAlertBody = "すべての項目を入力してください。"
                        showErrorAlert.toggle()
                                                return
                    }


                    viewModel.addMessage(userId: userId, stableId: stableId, senderId: userId, recipientId: recipientId, horseId: horseId, horseName: horseName, notificationType: type, title: title, memo: memo, isRead: "", messageId: messageId)
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
                        Text("＋ 追加")
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(.white)
                    }.frame(width: .infinity, height: 60)
                })
                    .alert(isPresented: addResult){
                        Alert(
                            title: Text(viewModel.alert!.title),
                            message: Text(viewModel.alert!.body),
                            dismissButton: .default(Text("OK"), action: {
                                if(viewModel.alert?.type == AlertContent.AlertType.success){
                                    self.isPresented.wrappedValue = false
                                }
                                viewModel.alert = nil
                            })
                        )
                    }
                        
                
                
            }//:VSTACK
            
        }//:SCROLLVIEW
        .padding(.trailing,18)
        .padding(.leading,18)
        .onAppear{
            if(activeMessage != nil){
//                date = activeDailyReport?.date
                
//                @State var title: String = ""
//                @State var type: String = ""
//                @State var recipientType: Int = 0
//                @State var recipient: String = ""
//                @State var recipientId: String? = "0"
//                @State var horseName: String = ""
//                @State var horseId: String? = nil
//                @State var memo: String = ""
                
                title = String(activeMessage?.title ?? "")
                type = String(activeMessage?.notificationType ?? "")
                if(activeMessage?.recipient == nil){
                    recipientType = 0
                }else{
                    recipientType = -1
                    recipient = String(activeMessage?.recipient?.username ?? "")
                    recipientId = String(Int(activeMessage?.recipient?.id ?? 0))
                }
                
                if(activeMessage?.horseName != ""){
                    horseId = String(Int(activeMessage?.horseId ?? 0))
                    horseName = activeMessage?.horseName ?? ""
                }
                
                memo = activeMessage?.memo ?? ""
            }else {
                title = ""
                type = ""
                recipientType = 0
                recipient = ""
                recipientId = "0"
                horseName = ""
                horseId = nil
                memo = ""
            }
        }

        
    }
}

//struct AddMessageSheetView_Previews: PreviewProvider {
//    static var previews: some View {
//        AddMessageSheetView()
//    }
//}
