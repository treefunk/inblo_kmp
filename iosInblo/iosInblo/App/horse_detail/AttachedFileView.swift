//
//  AttachedFileView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/23/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct AttachedFileView: View {
    
    let fileName: String
    let onPressRemove: () -> Void
    
    init(_ fileName: String, _ onPressRemove: @escaping () -> Void){
        self.fileName = fileName
        self.onPressRemove = onPressRemove
    }
    
    var body: some View {
        HStack {
            Image(systemName: "paperclip")
                .aspectRatio(contentMode: .fit)
            Text(fileName)
                .lineLimit(1)
                .truncationMode(.tail)
                .layoutPriority(1)
            Spacer()
            Button(action: {
                onPressRemove()
            }, label: {
                Image(systemName: "xmark.circle.fill")
                    .foregroundColor(Color("ColorDarkRedButton"))
                    .aspectRatio(contentMode: .fit)
            })
        }
        .padding(.top,10)
        .padding(.bottom,10)
        .padding(.leading,8)
        .padding(.trailing,8)
    }
}

struct AttachedFileView_Previews: PreviewProvider {
    static var previews: some View {
        AttachedFileView("TEST"){
            print("test")
        }.previewLayout(.sizeThatFits)
    }
}
