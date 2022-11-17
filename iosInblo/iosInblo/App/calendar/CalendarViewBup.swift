////
////  CalendarView.swift
////  iosInblo
////
////  Created by Jhondee Diaz on 11/3/21.
////  Copyright © 2021 orgName. All rights reserved.
////
//
//import SwiftUI
//import shared
//import JTAppleCalendar
//
//struct CalendarView: View {
//    private let calendar: Calendar
//    private let monthFormatter: DateFormatter
//    private let dayFormatter: DateFormatter
//    private let weekDayFormatter: DateFormatter
//    private let fullFormatter: DateFormatter
//
//    let formatter: DateFormatter = {
//        let formatter = DateFormatter()
//        formatter.dateFormat = "yyyy-MM-dd"
//        return formatter
//    }()
//
//    @State private var selectedDate = Self.now
//
//
//    private static var now = Date() // Cache now
//
//    @ObservedObject var viewModel: CalendarViewModel
//        private let calendarModule = CalendarModule(networkModule: NetworkModule())
//
//    init(calendar: Calendar) {
//        self.calendar = calendar
//        self.monthFormatter = DateFormatter(dateFormat: "MMMM yyyy", calendar: calendar)
////        self.monthFormatter.locale = Locale(identifier: "ja_JP")
//        self.dayFormatter = DateFormatter(dateFormat: "d", calendar: calendar)
//        self.weekDayFormatter = DateFormatter(dateFormat: "EEEEE", calendar: calendar)
//        self.weekDayFormatter.locale = Locale(identifier: "ja_JP")
//        self.fullFormatter = DateFormatter(dateFormat: "MMMM dd, yyyy", calendar: calendar)
//
//        self.viewModel = CalendarViewModel(getAllEventsByMonthYear: calendarModule.getAllEventsByMonthYear, addEvent: calendarModule.addEvent, deleteEvent: calendarModule.deleteEvent)
//
//        onScrollMonthYear(newDate: Date())
//    }
//
//    var body: some View {
//        ScrollView{
//            VStack {
//    //            Text("Selected date: \(fullFormatter.string(from: selectedDate))")
//    //                .bold()
//    //                .foregroundColor(.red)
//
//
//                LazyVStack {
//                        ForEach(0..<viewModel.events.count, id : \.self){ index in
//                            Text(viewModel.events[index].title)
//
//                        }
//                }
//                CalView(
//                    calendar: calendar,
//                    date: $selectedDate,
//                    content: { date in
//                            Button(action: { selectedDate = date }) {
//
//                                VStack(spacing: 0) {
////                                    Text("00").foregroundColor(.clear)
//                                    Text(dayFormatter.string(from: date))
//                                        .foregroundColor(Color("ColorPrimaryDark"))
//                                        .padding(.top,8)
//                                        .font(Font.custom("roboto_bold",size: 10))
//                                    VStack() {
//                                        ForEach(self.filteredEvents,id: \.self){ ev in
//                                            Text(ev.title)
//
//                                        }
////                                        if(!viewModel.events.isEmpty){
////
////                                            viewModel.events.forEach { e in e
////                                                let dateStart = formatter.date(from: e.date)
////                                                let dateEnd = formatter.date(from: e.dateEnd!)
////                                                if((dateStart! ... dateEnd!).contains(date)){
////                                                    self.filteredEvents.append(e)
////                                                }
////                                            }
////
////                                            var extras = 0
////                                            var foundEvents = [Event]()
////
////                                            filterEvents(date: date)
////
////                                            let stringDateInCal = formatter.string(from: date)
////
////                                            Text("test")
////                                        }
////                                        print("in events -> \(parent.dates)")
//
////                                        for event in parent.dates {
////                                            print("event -> \(event.date)")
////                                            let dateStart = formatter.date(from: event.date)!
////                                            let dateEnd = formatter.date(from: event.dateEnd!)!
////
////                                            print("datestart: \(dateStart)\n dateEnd:\(dateEnd)\n date:\(date)")
////
////                                            if((dateStart ... dateEnd).contains(date)) {
////                                                return 1
////                                            }
////                                        }
//                                    }
//                                    .frame(height:40)
//
//                                    Spacer()
//                                }
//                            }
//                            .frame(maxWidth: .infinity)
//                            .frame(height:75)
//                            .accessibilityHidden(true)
////                            .overlay(
////
////                                )
//                            .background(
//                                calendar.isDate(date, inSameDayAs: selectedDate) ? Color("ColorDarkBackground")
//
//                                : calendar.isDateInToday(date) ? Color("ColorDarkBackground")
//                                : Color("ColorDarkBackground")
//                                )
//                            .border(.black, width: calendar.isDate(date, inSameDayAs: selectedDate) ? 0.5 : 0)
//                            .padding(1)
//
//                    },
//                    trailing: { date in
//                        VStack(spacing: 0) {
//                            Text(dayFormatter.string(from: date))
//                                .foregroundColor(Color.secondary)
//                                .padding(.top,8)
//                                .font(Font.custom("roboto_bold",size: 10))
//
//                            VStack() {
//
//                            }
//                            .frame(height:40)
//                            Spacer()
//                        }
//                        .frame(maxWidth: .infinity)
//                        .frame(height:75)
//                        .accessibilityHidden(true)
//                        .background(Color("ColorDarkBackground"))
//                        .padding(1)
//
//                    },
//                    header: { date in
//                        VStack {
//                            Text(weekDayFormatter.string(from: date))
//                                .frame(maxWidth: .infinity)
//                                .frame(height:60)
//
//                        }
//
//
//                    },
//                    title: { date in
//                        HStack {
//                            Button {
//                                withAnimation {
//                                    guard let newDate = calendar.date(
//                                        byAdding: .month,
//                                        value: -1,
//                                        to: selectedDate
//                                    ) else {
//                                        return
//                                    }
//                                    onScrollMonthYear(newDate: newDate)
//                                }
//                            } label: {
//                                Label(
//                                    title: { Text("Previous") },
//                                    icon: { Image(systemName: "chevron.left") }
//                                )
//                                .labelStyle(IconOnlyLabelStyle())
//                                .padding(.horizontal)
//                                .frame(maxHeight: .infinity)
//                            }
//                            Spacer()
//                            Text(monthFormatter.string(from: date))
//                                .font(.headline)
//                                .padding()
//                            Spacer()
//                            Button {
//                                withAnimation {
//                                    guard let newDate = calendar.date(
//                                        byAdding: .month,
//                                        value: 1,
//                                        to: selectedDate
//                                    ) else {
//                                        return
//                                    }
//                                    print("newdate is \(newDate)")
//
//                                    onScrollMonthYear(newDate: newDate)
//                                }
//                            } label: {
//                                Label(
//                                    title: { Text("Next") },
//                                    icon: { Image(systemName: "chevron.right") }
//                                )
//                                .labelStyle(IconOnlyLabelStyle())
//                                .padding(.horizontal)
//                                .frame(maxHeight: .infinity)
//                            }
//                        }
////                        .padding(.bottom, 6)
//                    }
//                )
//                .equatable()
//                Spacer()
//            }//: VSTACK
////            .background(Color("ColorHighlight"))
////            .padding()
//        } //: SCROLLVIEW
//        .onChange(of: selectedDate) { selected in
//
//        }
//
//    }
//    func onScrollMonthYear(newDate: Date){
//        let df = DateFormatter()
//        df.dateFormat = "MM"
//        let month = df.string(from: newDate)
//        df.dateFormat = "yyyy"
//        let year = df.string(from: newDate)
//        print("month is -> \(month)")
//        print("year is -> \(year)")
//        viewModel.getAllEvents(stableId: 1, month: month, year: year, horseId: 0)
//        self.selectedDate = newDate
//
//
//    }
//
//}
//
//// MARK: - Component
//
//public struct CalView<Day: View, Header: View, Title: View, Trailing: View>: View {
//    // Injected dependencies
//    private var calendar: Calendar
//    @Binding private var date: Date
//    private let content: (Date) -> Day
//    private let trailing: (Date) -> Trailing
//    private let header: (Date) -> Header
//    private let title: (Date) -> Title
//
//    // Constants
//    private let daysInWeek = 7
//
//    public init(
//        calendar: Calendar,
//        date: Binding<Date>,
//        @ViewBuilder content: @escaping (Date) -> Day,
//        @ViewBuilder trailing: @escaping (Date) -> Trailing,
//        @ViewBuilder header: @escaping (Date) -> Header,
//        @ViewBuilder title: @escaping (Date) -> Title
//    ) {
//        self.calendar = calendar
//        self._date = date
//        self.content = content
//        self.trailing = trailing
//        self.header = header
//        self.title = title
//    }
//
//    public var body: some View {
//        let month = date.startOfMonth(using: calendar)
//        let days = makeDays()
//
//        return LazyVGrid(columns: Array(repeating: GridItem(.flexible(minimum:40), spacing: 0), count: daysInWeek), spacing: 0) {
//            Section(header: title(month)) {
//                ForEach(days.prefix(daysInWeek), id: \.self, content: header)
//                ForEach(days, id: \.self) { date in
//                    if calendar.isDate(date, equalTo: month, toGranularity: .month) {
//                        content(date)
//                    } else {
//                        trailing(date)
//                    }
//                }
//            }
//        }
//    }
//}
//
//// MARK: - Conformances
//
//extension CalView: Equatable {
//    public static func == (lhs: CalView<Day, Header, Title, Trailing>, rhs: CalView<Day, Header, Title, Trailing>) -> Bool {
//        lhs.calendar == rhs.calendar && lhs.date == rhs.date
//    }
//}
//
//// MARK: - Helpers
//
//private extension CalView {
//    func makeDays() -> [Date] {
//        guard let monthInterval = calendar.dateInterval(of: .month, for: date),
//              let monthFirstWeek = calendar.dateInterval(of: .weekOfMonth, for: monthInterval.start),
//              let monthLastWeek = calendar.dateInterval(of: .weekOfMonth, for: monthInterval.end - 1)
//        else {
//            return []
//        }
//
//        let dateInterval = DateInterval(start: monthFirstWeek.start, end: monthLastWeek.end)
//        return calendar.generateDays(for: dateInterval)
//    }
//}
//
//struct DateWithEvents {
//    var date: Date
//    var filteredEvents: [Event]
//}
//
//private extension Calendar {
//    func generateDates(
//        for dateInterval: DateInterval,
//        matching components: DateComponents
//    ) -> [Date] {
//        var dates = [dateInterval.start]
//
//        enumerateDates(
//            startingAfter: dateInterval.start,
//            matching: components,
//            matchingPolicy: .nextTime
//        ) { date, _, stop in
//            guard let date = date else { return }
//
//            guard date < dateInterval.end else {
//                stop = true
//                return
//            }
//
//            dates.append(date)
//        }
//
//        return dates
//    }
//
//    func generateDays(for dateInterval: DateInterval) -> [Date] {
//        generateDates(
//            for: dateInterval,
//            matching: dateComponents([.hour, .minute, .second], from: dateInterval.start)
//        )
//    }
//}
//
//private extension Date {
//    func startOfMonth(using calendar: Calendar) -> Date {
//        calendar.date(
//            from: calendar.dateComponents([.year, .month], from: self)
//        ) ?? self
//    }
//}
//
//private extension DateFormatter {
//    convenience init(dateFormat: String, calendar: Calendar) {
//        self.init()
//        self.dateFormat = dateFormat
//        self.calendar = calendar
//    }
//}
//
//// MARK: - Previews
//
//#if DEBUG
//struct CalendarView_Previews: PreviewProvider {
//    static var previews: some View {
//        Group {
//            CalendarView(calendar: Calendar(identifier: .gregorian))
//            CalendarView(calendar: Calendar(identifier: .islamicUmmAlQura))
//            CalendarView(calendar: Calendar(identifier: .hebrew))
//            CalendarView(calendar: Calendar(identifier: .indian))
//        }
//    }
//}
//#endif
