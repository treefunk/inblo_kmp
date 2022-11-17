//
//  HorseItemRowArchive.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 3/22/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HorseItemRowArchive: View {
    var position: Int
    var horse: Horse
    var horseListViewModel: HorseListViewModel
    var onRestore: (Horse) -> Void

    

    
    init(position: Int, for horse: Horse, horseListViewModel: HorseListViewModel, onRestore: @escaping (Horse) -> Void){
        self.position = position
        self.horse = horse
        self.horseListViewModel = horseListViewModel
        self.onRestore = onRestore
    }
    
    var body: some View {
            ZStack {
                HStack(spacing:14){
                    let horseName = horse.name!.count > 9 ? (horse.name?.substring(to: 9) ?? "") : (horse.name ?? "")

                    Text("\(position). \(horseName)\(horse.name!.count > 9 ? "..." : "")")
                        .foregroundColor(Color("ColorPrimaryDark"))
                        .frame(minHeight:70)
                        .lineLimit(1)
                        .layoutPriority(1)
                        .font(.system(size: 16, weight: .semibold))
                            Spacer()
                            Button(action: {
                                print("button pressed for \(horse.name)")
                                onRestore(horse)
                            }, label: {
                                ZStack {
                                    RoundedRectangle(cornerRadius: 4)
                                        .stroke(Color("ColorPrimaryDark"), style: StrokeStyle(lineWidth: 0.7))
                                    Text("戻す")
                                        .foregroundColor(Color("ColorPrimaryDark"))
                                        .font(.system(size: 9, weight: .semibold))
                                    
                                }
                                
                            })
                        .buttonStyle(PlainButtonStyle())
                        .frame(width: 50, height: 25)
                } //:HSTACK
                .padding(.trailing,18)
                .padding(.leading,18)
                .background(Color.white)
                
            
            } //: ZSTACK
    
    }
    
}


//struct HorseItemList_Previews: PreviewProvider {
//    static var previews: some View {
//        HorseItemRow(
//            position: 1,
//            for: Horse(id: 1, stableId: 1, farmId: 1, trainingFarmId: 1, user: nil, stable: nil, ownerName: nil, farmName: nil, trainingFarmName: nil, birthDate: nil, sex: nil, age: nil, name: "TestHorse", color: nil, class_: nil, fatherName: nil, motherName: nil, motherFatherName: nil, totalStake: nil, notes: nil, createdAt: nil, updatedAt: nil)
//        ).previewLayout(.sizeThatFits)
//    }
//}
