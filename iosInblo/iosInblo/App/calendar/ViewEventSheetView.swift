//
//  ViewEventSheetView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 2/4/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ViewEventSheetView: View {
    
    var isPresented: Binding<Bool>
    var showAddEventSheetView: Binding<Bool>
    var activeMonth: Binding<String>
    var activeYear: Binding<String>
    var activeHorseId: Binding<Int>
    var activeEvent: Binding<Event?>
    
    @EnvironmentObject var viewModel: CalendarViewModel
    @AppStorage("stableId") var stableId: Int = 0

    
    @State var showErrorAlert: Bool = false
    @State var errorAlertBody: String = ""
    
    @State private var showDeleteConfirmation = false
    @State private var pendingId = "0"
    
    
    init(isPresented: Binding<Bool>, showAddEventSheetView: Binding<Bool>, activeEvent: Binding<Event?>, activeMonth: Binding<String>, activeYear: Binding<String>, activeHorseId: Binding<Int>){
        self.isPresented = isPresented
        self.showAddEventSheetView = showAddEventSheetView
        self.activeEvent = activeEvent
        self.activeMonth = activeMonth
        self.activeYear = activeYear
        self.activeHorseId = activeHorseId
    }
    
    var body: some View {
        
        
        let addEventResult = Binding<Bool>(
            get: { self.viewModel.alert != nil },
            set: { _ in self.viewModel.alert?.title = "" }
        )
        
        
        var event = activeEvent.wrappedValue
        if let event = event {
            
            var eventType = EventOptions.getEventInfoByEventType(eventType: event.eventType)!.info
            
            ScrollView(showsIndicators: false) {
                VStack{
                HStack(spacing: 0) {
                    Rectangle()
                        .fill(eventType.colorDarker)
                        .frame(width:6,height:.infinity)
//                            .padding(.trailing,6)
                    
                    ZStack {
                        Color(.white)
                        HStack {
                            VStack {
                                Spacer()
                                HStack {
                                
                                        Text(event.start.isEmpty ? "N/A" : event.start)
                                            .font(.system(size: 21, weight: .semibold))
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                        

        
                                        .layoutPriority(1)
                                        .multilineTextAlignment(.leading)
                                    if event.date != event.dateEnd {
                                        HStack{
                                            Text(":")
                                                .font(.system(size: 22, weight: .bold))
                                                .foregroundColor(Color("ColorPrimaryDark"))
                                                .layoutPriority(1)
                                                .multilineTextAlignment(.leading)
                                            Text(event.end.isEmpty ? "N/A" : event.end)
                                                .font(.system(size: 21, weight: .semibold))
                                                .foregroundColor(Color("ColorPrimaryDark"))
                        
                                                .layoutPriority(1)
                                                .multilineTextAlignment(.leading)
                                        }
                                        
                                    }
                                    Spacer()
                                }
                                .frame(width:.infinity)

                                
                                HStack {
                                    Text(event.date)
                                        .font(.system(size: 10, weight: .semibold))
                                        .foregroundColor(Color("ColorPrimaryDark"))
                                    if event.date != event.dateEnd {
                                        HStack{
                                            Text("-")
                                                .font(.system(size: 10, weight: .semibold))
                                                .foregroundColor(Color("ColorPrimaryDark"))
                                                .layoutPriority(1)
                                                .multilineTextAlignment(.leading)
                                            Text((event.dateEnd == nil ? "N/A" : event.dateEnd) ?? "N/A")
                                                .font(.system(size: 10, weight: .semibold))
                                                .foregroundColor(Color("ColorPrimaryDark"))
                                                .layoutPriority(1)
                                                .multilineTextAlignment(.leading)
                                        }
                                        
                                    }
                                    Spacer()
                                }
                                .frame(width:.infinity)
                                Spacer()

                            }
                            .padding(.leading,12)
                            Spacer()
                            
                            
                            Button(action: {
                                self.isPresented.wrappedValue = false
                                self.showAddEventSheetView.wrappedValue = true
                            }, label: {
                                Image(systemName: "square.and.pencil")
                                    .foregroundColor(Color("ColorPrimaryDark"))
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 30, height: 30)
                            })
                                .buttonStyle(.plain)
                            
                            
                            Spacer()
                            
                            Button(action: {
//                                viewModel.deleteCalendarEvent(eventId: Int(event.id), stableId: stableId, cMonth: activeMonth.wrappedValue, cYear: activeYear.wrappedValue, cHorseId: activeHorseId.wrappedValue)
//                                self.isPresented.wrappedValue = false
                                pendingId = String(event.id)
                                self.showDeleteConfirmation = true
                            }, label: {
                                Image(systemName: "trash")
                                    .foregroundColor(Color("ColorDarkRedButton"))
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 30, height: 30)
                            })
                            .buttonStyle(.plain)
                            
                            Button(action: {
                                self.isPresented.wrappedValue = false
                            }, label: {
                                ZStack {
                                    Capsule()
                                        .fill(eventType.colorDarker)
      
                                    Text("閉じる")
                                        .font(.system(size: 12, weight: .regular))
                                        .foregroundColor(.white)
                                        .padding(.top,4)
                                        .padding(.bottom,4)
                                        .padding(.trailing,12)
                                        .padding(.leading,12)
                                        .layoutPriority(1)
                                    
                                }
                                .padding(.trailing,12)
                                
                            })
                            
//                            .frame(width: .infinity, height: 40)
                        }//: ZSTACK
                
                    }//: ZSTACK
                    .layoutPriority(5)
                    
                    
                    
                }//: HSTACK
                    
                    
                    ZStack(alignment:.center) {
                        Color("ColorDarkBackground")
                        Text(event.title)
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(Color("ColorPrimaryDark"))
                            .padding(.top,24)
                            .padding(.bottom,24)
                            .padding(.leading,16)
                            .padding(.trailing,16)
                    }
                    
                    Text(event.notes ?? "")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(Color("ColorPrimaryDark"))
                        .padding(.top,24)
                        .padding(.bottom,24)
                        .padding(.leading,16)
                        .padding(.trailing,16)
                    
                    HStack {
                        Spacer()
                        ZStack {
                            Capsule()
                                .fill(Color("ColorPrimaryDark"))

                            if( event.user != nil) {
                                Text("担当者 : \(event.user!.username!)")
                                    .font(.system(size: 12, weight: .regular))
                                    .foregroundColor(.white)
                                    .padding(.top,4)
                                    .padding(.bottom,4)
                                    .padding(.trailing,12)
                                    .padding(.leading,12)
                                    .layoutPriority(1)
                            }
                            
                        }
                        .padding(.trailing,12)
                    }
                }//: VSTACK
                .padding(.top,12)
            }//: SCROLLVIEW
            .alert(isPresented: $showDeleteConfirmation){
                
                Alert(title: Text("Are you sure you want to delete this?"),
                            message: Text(""),
                            primaryButton: .default(
                                Text("Cancel"),
                                action: {
                                    
                                }
                            ),
                            secondaryButton: .destructive(
                                Text("Delete"),
                                action: {
                                    print("pending ID is -> \(pendingId)")
                                    viewModel.deleteCalendarEvent(eventId: Int(pendingId) ?? 0, stableId: stableId, cMonth: activeMonth.wrappedValue, cYear: activeYear.wrappedValue, cHorseId: activeHorseId.wrappedValue)
                                    isPresented.wrappedValue = false
                                }
                            )
                        )
        //            viewModel.removeDailyReport(dailyReportId: String(daily.id),horseId: horse.id?.stringValue ?? "")

            }
        }else {
            Text("")
        }
        
    }
        
}

