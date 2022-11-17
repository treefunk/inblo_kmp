//
//  InbloTextField.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/19/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct InbloTextField: View {
    
    var labelText: String
    var placeHolder: String
    @State private var t = ""
    
    
    var body: some View {
        TextField(placeHolder, text: $t)
            .padding(.horizontal,TextFieldConstants.paddingHorizontal)
            .padding(.vertical, TextFieldConstants.paddingVertical)
            .background(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(TextFieldConstants.strokeColor, style: StrokeStyle(lineWidth: 0.7))
            )
//            .font(.body)
//        TextView(text: $t, labelText: "Username", placeHolder: "").frame(width: .infinity, height: 65)
    }
}

//struct InbloTextField_Previews: PreviewProvider {
//    static var previews: some View {
//
//        InbloTextField(text: $t, labelText: "Username", placeHolder: "")
//            .previewLayout(.sizeThatFits)
//            .padding(.all)
//    }
//}

struct TextFieldConstants {
    static let paddingHorizontal: CGFloat = 8
    static let paddingVertical: CGFloat = 10
    static let strokeColor = Color.blue
}
