//
//  TextViewPassword.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 10/20/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import MaterialComponents
import SwiftUI
import MaterialComponents.MaterialTextControls_OutlinedTextFields


struct TextViewPassword: UIViewRepresentable {
    var labelText: String
    var placeHolder: String

    
    func makeUIView(context: Context) -> MDCOutlinedTextField {
        let textField = MDCOutlinedTextField()
        return textField
    }

    func updateUIView(_ uiView: MDCOutlinedTextField, context: Context) {
        uiView.label.text = labelText
    
        uiView.placeholder = placeHolder
        uiView.layoutMargins = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        uiView.sizeToFit()
        uiView.setOutlineColor(UIColor(named: "ColorPrimary")!, for: MDCTextControlState.editing)
//        uiView.frame = CGRect(x: 0, y: 0, width: 100, height: 200)
        uiView.trailingEdgePaddingOverride = 0
        uiView.horizontalInterItemSpacingOverride = 0
        
        let imageOpenEye = Image(systemName: "eye").asUIImage()
        let imageCloseEye = Image(systemName: "eye.slash").asUIImage()
        
        let passwordButton = UIButton(type: .custom)
        passwordButton.frame = CGRect(x: CGFloat(0), y: CGFloat(0),
                                      width: CGFloat(45), height: CGFloat(uiView.frame.height))
        passwordButton.setImage(imageOpenEye, for: .normal)
        passwordButton.setImage(imageCloseEye, for: .selected)
//        passwordButton.addTarget(self, action: #selector(passViewTap), for: .touchUpInside)
        
        uiView.trailingView = passwordButton
        uiView.trailingViewMode = .always
        uiView.isSecureTextEntry = true
    }
    
//    func passViewTap(sender: AnyObject) {
//          sender.isSelected = !sender.isSelected
////          txtCurrentPassword.isSecureTextEntry = !sender.isSelected
//        }
    
    
}

extension View {
// This function changes our View to UIView, then calls another function
// to convert the newly-made UIView to a UIImage.
    public func asUIImage() -> UIImage {
        let controller = UIHostingController(rootView: self)
        
        controller.view.frame = CGRect(x: 0, y: CGFloat(Int.max), width: 1, height: 1)
        UIApplication.shared.windows.first!.rootViewController?.view.addSubview(controller.view)
        
        let size = controller.sizeThatFits(in: UIScreen.main.bounds.size)
        controller.view.bounds = CGRect(origin: .zero, size: size)
        controller.view.sizeToFit()
        
// here is the call to the function that converts UIView to UIImage: `.asUIImage()`
        let image = controller.view.asUIImage()
        controller.view.removeFromSuperview()
        return image
    }
}

extension UIView {
// This is the function to convert UIView to UIImage
    public func asUIImage() -> UIImage {
        let renderer = UIGraphicsImageRenderer(bounds: bounds)
        return renderer.image { rendererContext in
            layer.render(in: rendererContext.cgContext)
        }
    }
}
