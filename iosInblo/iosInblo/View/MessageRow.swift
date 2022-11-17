//
//  MessageRow.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 2/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct MessageRow: View {
    
    var userId: Int
    var message: Message
    var onClickEdit: (Message) -> Void
    var onClickRemove: (Message) -> Void

    
    
    var body: some View {
        HStack(alignment:.center , spacing:14) {
            Group {
//                Spacer()
//                    .frame(height:10)
                HStack(alignment: .center,spacing:0) {
                    Spacer()
                    if(userId == message.senderId) {
                        Button(action: {
                            onClickEdit(message)
                        }, label: {
                            Image(systemName: "square.and.pencil")
                                .foregroundColor(Color("ColorPrimaryDark"))
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        })
                            .buttonStyle(.plain)
                        
                    
                        
                        Button(action: {
                            onClickRemove(message)
                        }, label: {
                            Image(systemName: "trash")
                                .foregroundColor(Color("ColorDarkRedButton"))
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                        })
                        .buttonStyle(.plain)
                    }else{
                        Spacer(minLength: 40)
                    }
                   Spacer()
                    
                }
//                .padding(.leading,5)
                

                Text(message.formattedDate)//label date
                    .inbloTableValue()

                
                Text(message.formattedTime)//label time
                    .inbloTableValue()


                Text(message.sender?.username ?? "")//label weight
                    .lineLimit(1)
                    .inbloTableValue()

                Text(message.recipient?.username ?? "全員")//label rider name
                    .inbloTableValue()
            }
            Group {
                Text(message.title)//label training type
                    .inbloTableValue()

                Text(message.notificationType)//label trainign amount
                    .inbloTableValue()

                Text(message.horseName)//label 5f
                    .inbloTableValue()

                Text(message.memo)//label 4f
                    .font(.system(size: 12, weight: .regular))
                    .foregroundColor(.black)
                    .multilineTextAlignment(.leading)
                    .frame(alignment: .leading)
                    .frame(width:150)

            }
            Spacer()
        }
        .padding(.top,16)
        .padding(.bottom,16)
        .background(Color.white)
        .padding(.trailing,16)
        .padding(.leading,16)
        .padding(.bottom,0.5)
    }
}
