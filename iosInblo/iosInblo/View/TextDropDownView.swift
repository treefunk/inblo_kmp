//
//  TextDropDownView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/23/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct TextDropDownView: View {
    
    var dropDownData: [String] = []
    var labelText: String = ""
    var placeHolder: String = ""
    var text: Binding<String>
//    var dropDownLabels: (T) -> String
    var callback: ((Int, String) -> Void)? = nil
    
    init(
        dropDownData: [String] = [],
        labelText: String = "",
        placeHolder: String = "",
        text: Binding<String>,
//        dropDownLabels: @escaping (T) -> String,
        _ callback: ((Int, String) -> Void)? = nil
    ){
        self.dropDownData = dropDownData
        self.dropDownData.insert("", at: 0)

        self.labelText = labelText
        self.placeHolder = placeHolder
        self.text = text
//        self.dropDownLabels = dropDownLabels
        self.callback = callback
    }
    
    var body: some View {
        Menu(content: {
            ForEach(self.dropDownData.indices, id: \.self){ index in
                    Button(action: {
                        if(callback != nil){
                            callback?(index, self.dropDownData[index])
                        }else{
                            text.wrappedValue = self.dropDownData[index]
                        }
                    }, label: {
                        Text(self.dropDownData[index])
                    })
                
            }
        }, label: {
            TextView(text: text, labelText: labelText, placeHolder: placeHolder,isEditable: false)
        })
    }
}
//
//struct TextDropDownView_Previews: PreviewProvider {
//    static var previews: some View {
//        TextDropDownView()
//    }
//}
