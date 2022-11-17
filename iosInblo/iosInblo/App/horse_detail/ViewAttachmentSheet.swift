//
//  ViewAttachmentSheet.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 3/8/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import Combine
import SDWebImageSwiftUI

struct ViewAttachmentSheet: View {
    
    var isPresented: Binding<Bool>
    var attachedFiles: [AttachedFileDto]
    var dirString: String
    let activeUrl: String = ApiSettings.companion.activeUrl
    
    @State
    var fileUrl: String = ""
    
//    @ObservedObject var imageLoader:ImageLoader
//    @State var image:UIImage = UIImage()
    
    @State
    var index: Int = 0
//    var
    
    init(isPresented: Binding<Bool>, attachedFiles: [AttachedFileDto], dirString: String){
        self.isPresented = isPresented
        self.attachedFiles = attachedFiles
        self.dirString = dirString
//        imageLoader = ImageLoader(urlString:"https://colinphl.xsrv.jp/inblo-api-staging/api\(dirString)\(attachedFiles[0].filePath)")
//        self.fileUrl = "https://colinphl.xsrv.jp/inblo-api-staging/api\(dirString)\(attachedFiles[0].filePath)"
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                HStack {
                    Spacer()
                        .frame(maxWidth: .infinity)
//                    Text("治療内容")
//                        .frame(maxWidth: .infinity)
                    HStack {
                        Spacer()
                        Button(action: {
                            self.isPresented.wrappedValue = false
                        }, label: {
                            Image("IconCloseRound")
                        })
                        .buttonStyle(PlainButtonStyle())
                    }
                    .padding(.trailing,12)
                    .padding(.top,12)
                }//:HSTACK
                
                HStack {
                    Button(action: {
                        self.index-=1
                        self.loadImage(dirString: self.dirString, path: self.attachedFiles[self.index].filePath)
                    }){
                        Image(systemName: "chevron.left")
                    }
                    .isHidden(!(index != 0))
                    
                    Spacer()
                    
                    Button(action: {
                        self.index+=1
                        self.loadImage(dirString: self.dirString, path: self.attachedFiles[self.index].filePath)
                    }){
                        Image(systemName: "chevron.right")

                    }
                    .isHidden(!(index < (attachedFiles.count - 1)))
                }
                .padding(.trailing,12)
                .padding(.leading,12)
                .padding(.bottom,24)
                .padding(.top,24)
                
                Spacer()
                WebImage(url: URL(string: fileUrl))
                               .resizable()
                               .aspectRatio(contentMode: .fit)
                               .frame(maxHeight:.infinity)
                               .layoutPriority(10)

                
                Spacer(minLength: 50)
//
//                Image("https://colinphl.xsrv.jp/inblo-api-staging/api\(dirString)\(attachedFiles[index].filePath)")
//                    .resizable()
//                    .aspectRatio(contentMode: .fit)
            }
                
        }
        .onAppear(perform:{
            print(self.attachedFiles)
            loadImage(dirString: dirString, path: self.attachedFiles[self.index].filePath)
        })
            
    }
    
    func loadImage(dirString: String, path: String){
        self.fileUrl = "\(activeUrl)\(dirString)\(path)"
//        self.imageLoader = ImageLoader(urlString:"https://colinphl.xsrv.jp/inblo-api-staging/api\(dirString)\(path)")
    }
}

//struct ViewAttachmentSheet_Previews: PreviewProvider {
//    static var previews: some View {
//        ViewAttachmentSheet()
//    }
//}


class ImageLoader: ObservableObject {
    var didChange = PassthroughSubject<Data, Never>()
    var data = Data() {
        didSet {
            didChange.send(data)
        }
    }

    init(urlString:String) {
        guard let url = URL(string: urlString) else { return }
        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data else { return }
            DispatchQueue.main.async {
                self.data = data
            }
        }
        task.resume()
    }
}
