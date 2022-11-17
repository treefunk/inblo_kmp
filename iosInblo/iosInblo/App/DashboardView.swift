//
//  DashboardView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/3/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI




struct DashboardView: View {
    
    @State private var selection = 2
    @State private var menuOpened = false
    @State var width = UIScreen.main.bounds.size.width
    @AppStorage("userId") var userId: Int = 0
    @AppStorage("roleId") var roleId: Int = 0
    @AppStorage("username") var username: String = ""
    @AppStorage("horseArchiveMode") var horseArchiveMode: Bool = false

    init() {
//        UITabBar.appearance().isTranslucent = false
        UITabBar.appearance().backgroundColor = UIColor.white
        UITabBar.appearance().barTintColor = UIColor.white // custom color.
    }
    
    var body: some View {
        ZStack {
            
//            if !menuOpened {
            
            
                VStack(spacing: 0) {
                    HStack(spacing: 8){
                            HStack {
                                Image("logo")
                                    .scaledToFit()
                                    .frame(width: 100, height: 40)
                                Spacer()
                            }//:HSTACK
                            .padding(.leading,14)
                            
                        ZStack {
                            Rectangle()
                                .fill(Color("ColorPrimary"))
                                .frame(width:.infinity,height: 50)
                            Button(action: {
                                // open side menu
                                withAnimation(.spring()){
                                    self.menuOpened.toggle()

                                }
                            }, label: {
                                Image("IconMenu")
                                    .resizable()
                                    .scaledToFit()
                                    .padding(.all,12)
                                    .frame(width: 50, height: 50)
                                    .layoutPriority(1)
                            })
                            
                            
                        }
                    }//:HSTACK
                    .background(Color.white)
                    
                    
                    TabView(selection: $selection) {
                        CalendarView(calendar: Calendar(identifier: .gregorian),horseId: .constant(0),horseName: .constant(""))
                            .tabItem{
                                Image("IconBottomCalendar")
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                Text("カレンダー")
                            }
                            .tag(1)
                        
                        HorseListView()
                            .tabItem{
                                Image("IconBottomHorseList")
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                Text("管理馬一覧")
                            }
                            .tag(2)
                        
                        MessagesView()
                            .tabItem{
                                Image("IconBottomMessages")
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                Text("メッセージ")
                            }
                            .tag(3)
                        
                        
                            
                    }//:TABVIEW
//                    .background(Color.white)
                    .accentColor(Color("ColorPrimaryDark"))
                    
                   
                } //: VSTACK
//            }//: IF
            
            
            // Side Menu
//            if menuOpened {
                
//            }
//            HStack(spacing: 0) {
//                Spacer(minLength: 0)
//
//                VStack(spacing: 0 ){
//                    Text("helloo")
//                    Spacer(minLength: 0)
//                }
//                .frame(width: width - 100)
//                .background(Color.white)
//                .offset(x: menuOpened ? 0 : width - 100)
//            }
//            .background(Color.black.opacity(menuOpened ? 0.3 : 0))
//            .onTapGesture {
//                if(menuOpened) {
//                    menuOpened.toggle()
//
//                }
//            }
            
            
        }//: ZSTACK
        .sheet(isPresented: $menuOpened){
            VStack(alignment: .leading) {
                
                HStack {
                    VStack(alignment: .leading) {
                        Text(username)
                            .font(Font.custom("roboto_bold", size: 23))
                        Text(roleId == 1 ? "Person-in-Charge" : "Trainer")
                            .font(Font.custom("roboto_bold", size: 10))
                    }
                    
                    
                    Spacer()
                    
                    Button(action: {
                        menuOpened = false
                    }, label: {
                        Image(systemName: "xmark")
                            .foregroundColor(Color("ColorPrimaryDark"))
                    })
                }
                .padding(.bottom,12)
                .padding(.horizontal, 16)
                .padding(.top, 16)
                
                
                GroupBox {
                    VStack {
                        HStack {
                            Link("地方競馬情報サイト", destination: URL(string: "https://www.keiba.go.jp/KeibaWeb/DataRoom/DataRoomTop")!)
                                .font(.body)
                                .foregroundColor(.black)
                            
                            Spacer()
                            
                            Image(systemName: "info.circle.fill")
                                .foregroundColor(Color("ColorPrimaryDark"))
                        }
//                        Button(action: { }, label: {
//                            HStack {
//                                Text("地方競馬情報サイト")
//                                    .font(.body)
//                                    .foregroundColor(.black)
//                                Spacer()
//                                Image(systemName: "info.circle.fill")
//                                    .foregroundColor(Color("ColorPrimaryDark"))
//                            }
//
//                        })
                        
                        Divider().padding(.vertical,4)
                        
                        Button(action: {
                            if(selection != 2){
                                selection = 2
                            }
                            horseArchiveMode = true
                            menuOpened = false
                        }, label: {
                            Text("削除済管理馬一覧")
                                .font(.body)
                                .foregroundColor(.black)
                            Spacer()
                            Image(systemName: "archivebox")
                                .foregroundColor(Color("ColorPrimaryDark"))

                        })
                        
                        Divider().padding(.vertical,4)
                        
                        Button(action: {
                            userId = 0
                        }, label: {
                            Text("ログアウト")
                                .font(.body)
                                .foregroundColor(.black)
                            Spacer()
                            Image(systemName: "rectangle.portrait.and.arrow.right")
                                .foregroundColor(Color("ColorPrimaryDark"))

                        })
                    }
                    
                }
                .padding(.horizontal,16)

                
                
                
                
                Spacer()
                
                
            }
        } //: SHEET
    


       
        
    }
        
    
    func toggleMenu() {
        menuOpened.toggle()
    }
}

struct DashboardView_Previews: PreviewProvider {
    static var previews: some View {
        DashboardView()
    }
}
