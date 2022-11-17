//
//  TableValueModifier.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/16/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct TableValueModifier: ViewModifier{
    
    func body(content: Content) -> some View {
        content
            .font(.system(size: 13, weight: .regular))
//            .frame(alignment:.leading)
            .foregroundColor(.black)
            .frame(width:60)
    }
    
}

extension View {
    func inbloTableValue() -> some View {
        modifier(TableValueModifier())
    }
}
