//
//  MaterialTabs.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/11/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import MaterialComponents
import MaterialComponents.MaterialTabs_TabBarView

struct MaterialTabs: UIViewRepresentable {
    var tabItems: [MaterialTabItem]
//    var didSelectTab: ((UITabBarItem) -> Void)? = nil
    @Binding var selected: UITabBarItem?
    
    func makeUIView(context: Context) -> MDCTabBarView {
        let tabBarView = MDCTabBarView()

        
        let items = tabItems.map{
            $0.uiTabBarItem
        }
        tabBarView.items = items
        let blue = UIColor(named: "ColorPrimary")
        
        if(tabBarView.selectedItem == nil && !tabBarView.items.isEmpty){
            tabBarView.setSelectedItem(tabBarView.items[0], animated: false)
        }
        
        

        tabBarView.preferredLayoutStyle = .fixed// or .fixed
        tabBarView.sizeToFit()
        tabBarView.setTitleColor(UIColor(named: "ColorButtonSmallGray"), for: .normal)
        tabBarView.setTitleColor(blue, for: .selected)
        tabBarView.setTitleFont(UIFont(name: "Arial", size: 14), for: .normal)
    
        tabBarView.tintColor = blue
        tabBarView.autoresizingMask = [.flexibleWidth]
        tabBarView.sizeToFit()

        tabBarView.selectionIndicatorStrokeColor = blue
        tabBarView.imageTintColor(for: .selected)
        tabBarView.setImageTintColor(UIColor(named: "ColorPrimary"), for: .selected)
    
//        tabBarView.sizeThatFits(CGSize(width:0,height:10))
        tabBarView.contentSize = CGSize(width: 5, height: 5)
//        tabBarView.delegate = context.coordinator
        tabBarView.tabBarDelegate = context.coordinator
        return tabBarView
    }
    
    func updateUIView(_ uiView: MDCTabBarView, context: Context) {
//        uiView.setTitleFont(UIFont(name: "Arial", size: 3), for: .normal)
        if(self.selected != nil){
            uiView.selectedItem = self.selected
        }
//        uiView
        
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    typealias UIViewType = MDCTabBarView
    
    struct MaterialTabItem{
        
        var uiTabBarItem: UITabBarItem
        var callback: (UITabBarItem) -> Void
        
        init(_ item: UITabBarItem, _ callback: @escaping (UITabBarItem) -> Void ){
            self.uiTabBarItem = item
            self.callback = callback
        }
        
    }

    
    
}

extension MaterialTabs {
    class Coordinator: NSObject, MDCTabBarViewDelegate,UIScrollViewDelegate {
        var parent: MaterialTabs
        
        init(_ parent: MaterialTabs){
            self.parent = parent
        }
        
        func tabBarView(_ tabBarView: MDCTabBarView, didSelect item: UITabBarItem) {
            let selected = parent.tabItems.first(where: {
                $0.uiTabBarItem == item
            })
            parent.selected = selected!.uiTabBarItem
            
            selected?.callback(item)
        }
        
        
    }
}
