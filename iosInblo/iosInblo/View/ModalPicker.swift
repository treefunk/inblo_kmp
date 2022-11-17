//
//  ModalPicker.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/27/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct ModalPicker: View {
    
    @Binding var isShowing: Bool
    
    var body: some View {
        ZStack {
            if isShowing {
                Color.black
                    .opacity(0.3)
                    .ignoresSafeArea()
                    .onTapGesture{
                        isShowing = false
                    }
                VStack{
                    Text("Hello")
                }
                .frame(height: 400)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .transition(.move(edge: .bottom))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
        .ignoresSafeArea()
        .animation(.easeInOut)
        
       
    }
}

struct ModalPicker_Previews: PreviewProvider {
    static var previews: some View {
        ModalPicker(isShowing: .constant(true))
            .previewLayout(.sizeThatFits)
    }
}
