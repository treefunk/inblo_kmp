//
//  MultiPicker.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 1/29/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MultiPicker: View {
    typealias Label = String
       typealias Entry = String

       let data: [ (Label, [Entry]) ]
       @Binding var selection: [Entry]

       var body: some View {
           GeometryReader { geometry in
               HStack {
                   ForEach(0..<self.data.count) { column in
                       Picker(self.data[column].0, selection: self.$selection[column]) {
                           ForEach(0..<self.data[column].1.count) { row in
                               Text(verbatim: self.data[column].1[row])
                               .tag(self.data[column].1[row])
                           }
                       }
                       .pickerStyle(WheelPickerStyle())
                       .frame(width: geometry.size.width / CGFloat(self.data.count), height: geometry.size.height)
                       .frame(width: .infinity)
                       .clipped()
                   }
               }
           }
       }
}

struct HorseTempPicker: View {
    typealias Label = String
       typealias Entry = String
    @Binding var selection: [Int]
    
       var body: some View {
           GeometryReader { geometry in
               HStack(spacing:0) {
                    
                        Spacer()
                   
                       Picker("Body temp", selection: self.$selection[0]) {
                           ForEach(35..<43){ num in
                               Text(verbatim: String(num))
                                   .tag(num)
                           }
                       }
                       .pickerStyle(WheelPickerStyle())
                   
                       .frame(width: (geometry.size.width / CGFloat(2)) - 40)
//                       .frame(minWidth:0)
//                       .frame(width: 50)
//                       .compositingGroup()
                       .clipped()
                   
                        Text(".")
                   
                        Picker("Body temp", selection: self.$selection[1]) {
                            ForEach(0..<10){ num in
                                Text(verbatim: String(num))
                                    .tag(num)
                            }
                        }
                       .pickerStyle(WheelPickerStyle())
                       .frame(width: (geometry.size.width / CGFloat(2)))
//                       .frame(minWidth:0)
//                       .frame(width: 70)
//                       .compositingGroup()
                       .clipped()
                   
                    Spacer()
               }
           }
       }
}

//struct HorseWeightPicker: View {
//    typealias Label = String
//       typealias Entry = String
//    @Binding var selection: [Int]
//
//       var body: some View {
//           GeometryReader { geometry in
//               HStack(spacing:0) {
//
//                        Spacer()
//
//                       Picker("Body temp", selection: self.$selection[0]) {
//                           ForEach(35..<43){ num in
//                               Text(verbatim: String(num))
//                                   .tag(num)
//                           }
//                       }
//                       .pickerStyle(WheelPickerStyle())
//
//                       .frame(width: (geometry.size.width / CGFloat(2)) - 40)
////                       .frame(minWidth:0)
////                       .frame(width: 50)
////                       .compositingGroup()
//                       .clipped()
//
//                        Text(".")
//
//                        Picker("Body temp", selection: self.$selection[1]) {
//                            ForEach(0..<10){ num in
//                                Text(verbatim: String(num))
//                                    .tag(num)
//                            }
//                        }
//                       .pickerStyle(WheelPickerStyle())
//                       .frame(width: (geometry.size.width / CGFloat(2)))
////                       .frame(minWidth:0)
////                       .frame(width: 70)
////                       .compositingGroup()
//                       .clipped()
//
//                    Spacer()
//               }
//           }
//       }
//}
