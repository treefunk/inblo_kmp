//
//  HorseItemList.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/3/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared


struct HorseItemRow: View {
    
    var position: Int
    var horse: Horse
    var horseListViewModel: HorseListViewModel
    var isShowingDetailView: Binding<String?>
    var isEditMode: Binding<Bool?>
    var onClickHorse: (Horse) -> Void
    var onArchiveHorse: (Horse) -> Void
    @State
    var offset: CGFloat = 0
    
    @GestureState var isDragging = false

    
    init(position: Int, for horse: Horse, horseListViewModel: HorseListViewModel,isShowingDetailView: Binding<String?>, isEditMode: Binding<Bool?>, onClickHorse: @escaping (Horse) -> Void, onArchiveHorse: @escaping (Horse) -> Void){
        self.position = position
        self.horse = horse
        self.horseListViewModel = horseListViewModel
        self.isShowingDetailView = isShowingDetailView
        self.isEditMode = isEditMode
        self.onClickHorse = onClickHorse
        self.onArchiveHorse = onArchiveHorse
    }
    
    var body: some View {
        NavigationLink(destination: HorseDetailView(horse: horse, isEditMode:isEditMode ).environmentObject(horseListViewModel), tag: String(Int(horse.id ?? 0)),selection: isShowingDetailView
        ) {
            ZStack {
                
                
                HStack(spacing:14){
//                    let horseName = horse.name?.substring(to: 8) ?? "" + (horse.name?.count ?? 0 > 9 ? "..." : "")
                    
//                    var hName = ""
//                    if(horse.name!.count > 9){
//                        hName = horse.name?.substring(to: 8) ?? ""
//                    }else{
//                        hName = horse.name? ?? ""
//                    }
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
                                onClickHorse(horse)
                            }, label: {
                                ZStack {
                                    RoundedRectangle(cornerRadius: 4)
                                        .stroke(Color("ColorPrimaryDark"), style: StrokeStyle(lineWidth: 0.7))
                                    Text("内容修正")
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
                .offset(x: self.offset)
                .gesture(
                    DragGesture()
                        .updating($isDragging, body:{ (value,state,_) in
                            state = true
                            onChanged(value: value)
                        })
                        .onChanged({ value in
                            onChanged(value: value)
                        })
                        .onEnded({ value in
                            onEnded(value: value)
                        })
                ).zIndex(10)
                    
                
                
                ZStack {
                    HStack {
                        Spacer()
                        
                        Button(action: {
                            onArchiveHorse(horse)
                        }, label: {
                            Image(systemName: "archivebox")
                                .resizable()
                                .frame(width: 20, height: 20)
                                .foregroundColor(Color.white)
                        })
                            .padding(.trailing,18)

                
                            
                       
                    }
                    
//                    .padding(.leading,18)
                    
                }
                .frame(maxHeight:.infinity)
                .background(Color("ColorPrimaryDark"))

                
            } //: ZSTACK
            
        }//:Navigation
        .buttonStyle(FlatLinkStyle())
        .zIndex(1)
                
        
        
    }
    
    func onChanged(value: DragGesture.Value){
        if value.translation.width < 0  && isDragging {
            offset = value.translation.width
        }
        if !isDragging {
            offset = 0
        }
    }
    
    func onEnded(value: DragGesture.Value){
//        withAnimation {
            if -value.translation.width >= 50 {
                offset = -61
            }else {
                offset = 0
            }
//        }
    }
}

struct FlatLinkStyle:  ButtonStyle {
    
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .border(Color("ColorPrimaryDark"),width: configuration.isPressed ? 1.5 : 0)
            .animation(.easeOut(duration: 0.2), value: configuration.isPressed)
        
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
