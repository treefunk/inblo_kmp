//
//  TableLabelModifier.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/15/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI


struct TableLabelModifier: ViewModifier {
    
//    typealias Body = <#type#>
    
    func body(content: Content) -> some View {
        content
            .font(.system(size: 14, weight: .semibold))
            .foregroundColor(.white)
            .frame(width:60)
    }
    
    
}

extension View {
    func inbloTableLabel() -> some View {
            modifier(TableLabelModifier())
    }
}
