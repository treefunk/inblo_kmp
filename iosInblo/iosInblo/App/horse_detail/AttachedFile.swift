//
//  AttachedFile.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/23/21.
//  Copyright © 2021 orgName. All rights reserved.
//
import SwiftUI

struct AttachedFile: Hashable {
    var id: String = "0"
    let fileName: String
    let fileExtension: String
    var file: UIImage? = nil
}
