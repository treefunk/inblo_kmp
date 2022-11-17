//
//  StringExt.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 3/29/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

extension String {
    func index(from: Int) -> Index {
        return self.index(startIndex, offsetBy: from)
    }

    func substring(from: Int) -> String {
        let fromIndex = index(from: from)
        return String(self[fromIndex...])
    }

    func substring(to: Int) -> String {
        let toIndex = index(from: to)
        return String(self[..<toIndex])
    }

    func substring(with r: Range<Int>) -> String {
        let startIndex = index(from: r.lowerBound)
        let endIndex = index(from: r.upperBound)
        return String(self[startIndex..<endIndex])
    }
    
    func replace(target: String, withString: String) -> String
        {
            return self.replacingOccurrences(of: target, with: withString, options: NSString.CompareOptions.literal, range: nil)
        }
}
