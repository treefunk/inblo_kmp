//
//  InbloTableValueRow.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/17/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct InbloDailyRow: View {
    
    var horseDaily: HorseDailyDto
    var hiddenColumns: String
    var onClickEdit: (HorseDailyDto) -> Void
    var onClickRemove: (HorseDailyDto) -> Void
    var onClickViewAttachments: (HorseDailyDto) -> Void

    
    
    var body: some View {
//        AF.upload(
        HStack(alignment:.center , spacing:14) {
            Group {
                HStack(alignment: .center,spacing:0) {
                    
                    Spacer()
                    Button(action: {
                        onClickEdit(horseDaily)
                    }, label: {
                        Image(systemName: "square.and.pencil")
                            .foregroundColor(Color("ColorPrimaryDark"))
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                    })
                        .buttonStyle(.plain)
                    
                                        
                    Button(action: {
                        onClickRemove(horseDaily)
                    }, label: {
                        Image(systemName: "trash")
                            .foregroundColor(Color("ColorDarkRedButton"))
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                    })
                    .buttonStyle(.plain)
                    Spacer()
                    
                }
                
                if !hiddenColumns.contains("date") {
                    Text(horseDaily.date.replace(target: "-",withString: ""))//label date
                        .font(.system(size: 13, weight: .regular))
                        .lineLimit(1)
                        .foregroundColor(.black)
                        .frame(alignment: .leading)
                        .frame(width:70)
                }

                
                    
                if !hiddenColumns.contains("body_temperature") {

                Text(String(horseDaily.bodyTemperature))//label temperature
                    .inbloTableValue()
                }
                
                if !hiddenColumns.contains("horse_weight") {
                Text(String(horseDaily.horseWeight))//label weight
                    .inbloTableValue()
                }

                if !hiddenColumns.contains("rider") {
                Text(horseDaily.rider?.name ?? "")//label rider name
                    .inbloTableValue()
                }
            }
            Group {
                if !hiddenColumns.contains("training_type") {
                Text(horseDaily.trainingType?.name ?? "")//label training type
                    .inbloTableValue()
                }

                if !hiddenColumns.contains("training_amount") {
                Text(horseDaily.trainingAmount)//label trainign amount
                    .inbloTableValue()
                }

                if !hiddenColumns.contains("5f_time") {
                Text(String(horseDaily.time5f))//label 5f
                    .inbloTableValue()
                }

                if !hiddenColumns.contains("4f_time") {
                Text(String(horseDaily.time4f))//label 4f
                    .inbloTableValue()
                }

                if !hiddenColumns.contains("3f_time") {
                Text(String(horseDaily.time3f))//label 3f
                    .inbloTableValue()
                }
            }
            if !hiddenColumns.contains("memo") {
            Text(horseDaily.memo)//label note
                .font(.system(size: 12, weight: .regular))
                .foregroundColor(.black)
                .multilineTextAlignment(.leading)
                .frame(alignment: .leading)
                .frame(width:150)
            }

            if(horseDaily.attachedFiles?.count ?? 0 > 0) {
                Button(action: {
                    onClickViewAttachments(horseDaily)
                }){
                    Text("ファイル")//label attachments
                        .inbloTableValue()
                }
            }else{
                Text(" ")
                    .inbloTableValue()
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

//struct InbloTableValueRow_Previews: PreviewProvider {
//    static var previews: some View {
//        InbloDailyRow()
//    }
//}
