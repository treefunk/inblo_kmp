//
//  TextView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/11/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import MaterialComponents
import SwiftUI
import MaterialComponents.MaterialTextControls_OutlinedTextFields


struct TextView: UIViewRepresentable {
    @Binding var text: String
    var labelText: String
    var placeHolder: String
    var isSecureTextEntry: Bool = false
    var isEditable: Bool = true
    var maxCharacters: Int? = nil
    var leadingPadding: NSNumber? = nil
    var trailingPadding: NSNumber? = nil


    func makeUIView(context: Context) -> MDCOutlinedTextField {
        let textField = MDCOutlinedTextField()
        textField.delegate = context.coordinator
        return textField
    }

    func updateUIView(_ uiView: MDCOutlinedTextField, context: Context) {
        uiView.label.text = labelText
    
        uiView.placeholder = placeHolder
        uiView.layoutMargins = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        uiView.sizeToFit()
        uiView.text = self.text
        
        if(leadingPadding != nil){
            uiView.leadingEdgePaddingOverride = leadingPadding
        }
        
        if(trailingPadding != nil){
            uiView.trailingEdgePaddingOverride = trailingPadding

        }
        
//        uiView.type
        uiView.font = UIFont(name: "roboto_bold", size: 23)
        uiView.setOutlineColor(UIColor(named: "ColorPrimary")!, for: MDCTextControlState.editing)
//        uiView.frame = CGRect(x: 0, y: 0, width: 100, height: 200)
        uiView.trailingEdgePaddingOverride = 0
        uiView.horizontalInterItemSpacingOverride = 0
        uiView.isSecureTextEntry = isSecureTextEntry
        
        
        
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    
    
}

extension TextView {

    
    class Coordinator: NSObject, UITextFieldDelegate {
        
        var parent: TextView
        
        init(_ parent: TextView){
            self.parent = parent
        }
        func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
            return parent.isEditable
        }
        
        
        func textFieldDidChangeSelection(_ textField: UITextField) {
            parent.text = textField.text!
        }
        
        func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
            if(parent.maxCharacters == nil){
                return true
            }
                
            let currentCharacterCount = textField.text?.count ?? 0
                if range.length + range.location > currentCharacterCount {
                    return false
                }
                let newLength = currentCharacterCount + string.count - range.length
            return newLength <= parent.maxCharacters ?? 0
        }
        
        
    
        
//        override class func didChange(_ changeKind: NSKeyValueChange, valuesAt indexes: IndexSet, forKey key: String) {
////            self.b = text
//        }
        
    }
}
