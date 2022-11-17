//
//  InbloTreatmentRowq.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 12/1/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct InbloTreatmentRow: View {
    var horseTreatment: HorseTreatment
    var onClickEdit: (HorseTreatment) -> Void
    var onClickRemove: (HorseTreatment) -> Void
    var onClickViewAttachments: (HorseTreatment) -> Void

    
    
    var body: some View {
//        AF.upload(
        HStack(alignment:.center , spacing:14) {
            Group {
                HStack(alignment: .center,spacing:0) {
                    
                    Spacer()
                    Button(action: {
                        onClickEdit(horseTreatment)
                    }, label: {
                        Image(systemName: "square.and.pencil")
                            .foregroundColor(Color("ColorPrimaryDark"))
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                    })
                        .buttonStyle(.plain)
                    
                    
                    
                    Button(action: {
                        onClickRemove(horseTreatment)
                    }, label: {
                        Image(systemName: "trash")
                            .foregroundColor(Color("ColorDarkRedButton"))
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                    })
                    .buttonStyle(.plain)
                    
                    Spacer()
                    
                }
                

                Text(horseTreatment.date.replace(target: "-",withString: ""))//label date
                    .font(.system(size: 13, weight: .regular))
                    .lineLimit(1)
                    .foregroundColor(.black)
                    .frame(alignment: .leading)
                    .frame(width:70)
                
                Text(String(horseTreatment.occasionType))//label occasion type
                    .inbloTableValue()

                Text(String(horseTreatment.injuredPart))//label injured part
                    .inbloTableValue()

                Text(horseTreatment.treatmentDetail)//label treatment detail
                    .inbloTableValue()
            }
            Group {
                Text(horseTreatment.doctorName)//label vet name
                    .inbloTableValue()

                Text(horseTreatment.medicineName)//label drug name
                    .inbloTableValue()
            }
            Text(horseTreatment.memo)//label note
                .font(.system(size: 12, weight: .regular))
                .foregroundColor(.black)
                .multilineTextAlignment(.leading)
                .frame(alignment: .leading)
                .frame(width:150)

            
            if(horseTreatment.attachedFiles?.count ?? 0 > 0) {
                Button(action: {
                    onClickViewAttachments(horseTreatment)
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
