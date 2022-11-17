//
//  EventType.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 2/2/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI


enum EventOptions: CaseIterable {
    
    struct EventInfo {
        var name: String
        var colorDark: Color
        var colorDarker: Color
        var colorLight: Color
    }
    
    case final_cutoff
    case interm_cutoff
    case race_sched
    case farrier
    case stables_retire
    case stables_return
    case stable_related
    case business_trip
    case others
    
    public static func getEventInfoByEventType(eventType: String) -> EventOptions? {
        return EventOptions.allCases.first(where: { $0.info.name == eventType })
    }
    
    var info: EventInfo {
        switch self {
        case .final_cutoff:
            return EventInfo(name: "最終追切", colorDark: Color(hex: "#FFD033")!, colorDarker: Color(hex: "#F2C100")!, colorLight: Color(hex: "#FFF4C3")!)
            
        case .interm_cutoff:
            return EventInfo(name: "中間追切", colorDark: Color(hex: "#FFD033")!, colorDarker: Color(hex: "#F2C100")!, colorLight: Color(hex: "#FFF4C3")!)
        case .race_sched:
            return EventInfo(name: "レース予定", colorDark: Color(hex: "#EA7266")!, colorDarker: Color(hex: "#E55863")!, colorLight: Color(hex: "#FFDFDF")!)
        case .farrier:
            return EventInfo(name: "装蹄", colorDark: Color(hex: "#EA7266")!, colorDarker: Color(hex: "#E55863")!, colorLight: Color(hex: "#FFDFDF")!)
        case .stables_retire:
            return EventInfo(name: "退厩", colorDark: Color(hex: "#FFD033")!, colorDarker: Color(hex: "#F2C100")!, colorLight: Color(hex: "#FFF4C3")!)
        case .stables_return:
            return EventInfo(name: "帰厩", colorDark: Color(hex: "#22D143")!, colorDarker: Color(hex: "#44AD60")!, colorLight: Color(hex: "#DCF8E8")!)
        case .stable_related:
            return EventInfo(name: "厩舎関連", colorDark: Color(hex: "#22D143")!, colorDarker: Color(hex: "#44AD60")!, colorLight: Color(hex: "#DCF8E8")!)
        case .business_trip:
            return EventInfo(name: "出張・不在予定", colorDark: Color(hex: "#7D79E8")!, colorDarker: Color(hex: "#736FF1")!, colorLight: Color(hex: "#DCDEF8")!)
        case .others:
            return EventInfo(name: "その他", colorDark: Color(hex: "#7D79E8")!, colorDarker: Color(hex: "#736FF1")!, colorLight: Color(hex: "#DCDEF8")!)
        }
    }
}
