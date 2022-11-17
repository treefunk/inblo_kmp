//
//  InbloSecureField.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/19/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct InbloSecureField: View {
    
    var placeHolder: String
    var bindingString: Binding<String>
    

    var body: some View {
        SecureField(placeHolder, text: bindingString)
            .padding(.horizontal,TextFieldConstants.paddingHorizontal)
            .padding(.vertical, TextFieldConstants.paddingVertical)
            .background(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(TextFieldConstants.strokeColor, style: StrokeStyle(lineWidth: 0.7))
            )
            .font(.body)
    }
}

struct InbloSecureField_Previews: PreviewProvider {
    static var previews: some View {

        InbloSecureField(placeHolder: "Password", bindingString: .constant(""))
            .previewLayout(.sizeThatFits)
            .padding(.all)
    }
}
