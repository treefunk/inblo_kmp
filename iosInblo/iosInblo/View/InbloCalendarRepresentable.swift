//
//  InbloCalendar.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 1/17/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import FSCalendar
import shared
import SwiftCoroutine

struct InbloCalendarRepresentable: UIViewRepresentable {
    typealias UIViewType = FSCalendar
    
//    @Binding var selectedDate: Date
    
    @Binding var dates: [Event]
//    var oldDates = [Event]()
    
    var onActiveMonthYear: (String,String, FSCalendar) -> Void
    var currentMonth = ""

    
    let secondary = UIColor(named: "ColorPrimaryDark")
    let primary = UIColor(named: "ColorPrimary")
    let tersiary = UIColor(named: "ColorHighlight")
    
    var calendar = FSCalendar()
    
    func updateUIView(_ uiView: FSCalendar, context: Context) {
        calendar.setCurrentPage(Date(), animated: false)
        calendar.reloadData()
    }
    
    func makeUIView(context: Context) -> FSCalendar {
        calendar.delegate = context.coordinator
        calendar.dataSource = context.coordinator
        calendar.appearance.eventDefaultColor = .orange
        
        calendar.calendarHeaderView.backgroundColor = self.primary
        calendar.calendarWeekdayView.backgroundColor = self.primary
        calendar.appearance.headerTitleColor = self.tersiary
        calendar.appearance.weekdayTextColor = self.tersiary
        
        calendar.appearance.eventSelectionColor = self.tersiary
        calendar.appearance.eventDefaultColor = self.primary
        calendar.appearance.eventOffset = CGPoint(x: 0, y: -12)
        
        calendar.appearance.todaySelectionColor = self.primary
        calendar.appearance.selectionColor = self.secondary
        calendar.appearance.todayColor = self.primary
//        calendar.placeholderType = .none
        
        calendar.appearance.titleWeekendColor = self.secondary
        calendar.appearance.titleDefaultColor = self.primary
        return calendar
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject, FSCalendarDelegate, FSCalendarDataSource {
        
        fileprivate let gregorian = Calendar(identifier: .gregorian)
            fileprivate let formatter: DateFormatter = {
                let formatter = DateFormatter()
                formatter.dateFormat = "MM-dd-yyyy"
                return formatter
            }()
            
        fileprivate weak var calendar: FSCalendar!
        fileprivate weak var eventLabel: UILabel!
        
        
        var parent: InbloCalendarRepresentable
        
        init(_ parent: InbloCalendarRepresentable){
            self.parent = parent

            print("init")
            
            
            DispatchQueue.main.startCoroutine {
                let yearMonth = InbloCalendarRepresentable.Coordinator.getYearMonthString(parent.calendar.currentPage)
                print(yearMonth)
                parent.onActiveMonthYear(yearMonth[0],yearMonth[1], parent.calendar)
                parent.calendar.reloadData()
                parent.calendar.reloadInputViews()
                
            }

    
           
        }
        
        
        //MARK: handling events
        func calendar(_ calendar: FSCalendar, numberOfEventsFor date: Date) -> Int {
            
//                        let fmatter = DateFormatter()
//                        fmatter.dateFormat = "dd"
//                        let d = fmatter.string(from: date)
//                        let FmMatter = DateFormatter()
//                        fmatter.dateFormat = "MM"
//                        let m = FmMatter.string(from: date)
//                        if(d == "01"){
//                            let yearMonth = InbloCalendarRepresentable.Coordinator.getYearMonthString(self.parent.calendar.currentPage)
//
//                            print("pagedidchange: \(yearMonth)")
//                //            self.calendar.reloadData()
//
//
//                            print("pagedidchange: -> \(self.parent.dates)")
//                //             self.parent.calendar.reloadInputViews()
//                //            calendar.reloadData()
////                            DispatchQueue.main.async {
//                                self.parent.onActiveMonthYear(yearMonth[0],yearMonth[1],self.parent.calendar)
                                
            //                    self.parent.calendar.reloadInputViews()
//                            }
////                        }
//                        DispatchQueue.main.sync {
//                            self.parent.calendar.reloadData()
//                        }
            
            formatter.dateFormat = "yyyy-MM-dd"
            print("in events -> \(parent.dates)")
            
            for event in parent.dates {
                print("event -> \(event.date)")
                let dateStart = formatter.date(from: event.date)!
                let dateEnd = formatter.date(from: event.dateEnd!)!
                
                print("datestart: \(dateStart)\n dateEnd:\(dateEnd)\n date:\(date)")
                
                if((dateStart ... dateEnd).contains(date)) {
                    return 1
                }
            }
            
            print("dates in number of events -> \(parent.dates)")
            if(parent.dates.count > 0){
                print("start: \(parent.dates[0].start)")
            }
//

        
            return 0
        }
        
        
        
        
        func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
            let yearMonth = InbloCalendarRepresentable.Coordinator.getYearMonthString(self.parent.calendar.currentPage)

            print("pagedidchange: \(yearMonth)")
//            self.calendar.reloadData()


            print("pagedidchange: -> \(self.parent.dates)")
//             self.parent.calendar.reloadInputViews()
//            calendar.reloadData()
           
//            DispatchQueue.main.async {
            
                
//            DispatchQueue.main.async {
                DispatchQueue.main.startCoroutine {
                    self.parent.onActiveMonthYear(yearMonth[0],yearMonth[1],self.parent.calendar)
                self.parent.calendar.reloadData()
                self.parent.calendar.reloadInputViews()
            }
            
        }
        
//        func minimumDate(for calendar: FSCalendar) -> Date {
//
//            return Date()
//        }
//

        func calendar(_ calendar: FSCalendar, willDisplay cell: FSCalendarCell, for date: Date, at monthPosition: FSCalendarMonthPosition) {
//            print("displaying calendar...")
        }
        
    
        
        func calendar(_ calendar: FSCalendar, imageFor date: Date) -> UIImage? {
//            let fmatter = DateFormatter()
//            fmatter.dateFormat = "dd"
//            let d = fmatter.string(from: date)
//            let FmMatter = DateFormatter()
//            fmatter.dataef
//            if(d == "01"){
//                let yearMonth = InbloCalendarRepresentable.Coordinator.getYearMonthString(self.parent.calendar.currentPage)
//
//                print("pagedidchange: \(yearMonth)")
//    //            self.calendar.reloadData()
//
//
//                print("pagedidchange: -> \(self.parent.dates)")
//    //             self.parent.calendar.reloadInputViews()
//    //            calendar.reloadData()
//                DispatchQueue.main.async {
//                    self.parent.onActiveMonthYear(yearMonth[0],yearMonth[1],self.parent.calendar)
//                    self.parent.calendar.reloadData()
////                    self.parent.calendar.reloadInputViews()
//                }
//            }
            
           
            return nil
        }
        
        
        

        func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
            debugPrint("did select date \(self.formatter.string(from: date))")
            let selectedDates = calendar.selectedDates.map({self.formatter.string(from: $0)})
            debugPrint("selected dates is \(selectedDates)")
            if monthPosition == .next || monthPosition == .previous {
    //            calendar.setCurrentPage(date, animated: true)
            }
        }
        
        func calendar(_ calendar: FSCalendar, shouldSelect date: Date, at monthPosition: FSCalendarMonthPosition) -> Bool {
            if monthPosition == .next || monthPosition == .previous {
    //            calendar.setCurrentPage(date, animated: true)
                return false
            }
            return true
        }
        

        
        private static func getYearMonthString(_ currentDate: Date) -> [String] {

            var currentMonth = String(Calendar.current.component(.month, from: currentDate))
            let currentYear = String(Calendar.current.component(.year, from: currentDate))
            
            if(currentMonth.count == 1){
                currentMonth = "0\(currentMonth)"
            }

            
            return [currentMonth,currentYear]
        }
        
        
        

        
    
    }
}


    
    
