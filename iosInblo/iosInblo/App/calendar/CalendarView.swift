//
//  CalendarView.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/3/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared
import JTAppleCalendar
import SwiftCoroutine

struct CalendarView: View {
    private let calendar: Calendar
    private let monthFormatter: DateFormatter
    private let dayFormatter: DateFormatter
    private let weekDayFormatter: DateFormatter
    private let fullFormatter: DateFormatter
    
//    @State
//    private var activeEvents = [Event]()
    
    @State
    private var selectedEvent: Event? = nil
    
    @State
    private var calActive = false
    
    @AppStorage("stableId") var stableId: Int = 0


    let formatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()

    @State private var selectedDate = Date(timeIntervalSinceReferenceDate: -123456789.0)
    @State private var events = [Event]()


    @State private var m = ""
    @State private var y = ""
//    @State var horseId = 0
    
    var horseId: Binding<Int> // active horse id
    var horseName: Binding<String> // active horse name
    
    
    @State private var showAddEventSheet = false
    @State private var showViewEventSheet = false

    
    private static var now = Date() // Cache now
    
    let dateAndTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()
    
    let monthAndDayformatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "MM/dd"
        return formatter
    }()

    @ObservedObject var viewModel: CalendarViewModel
    private let calendarModule = CalendarModule(networkModule: NetworkModule())
    private let getHorseModule = GetHorseListModule(networkModule: NetworkModule())


    init(calendar: Calendar, horseId: Binding<Int>, horseName: Binding<String>) {
        self.calendar = calendar

        self.monthFormatter = DateFormatter(dateFormat: "MMMM yyyy", calendar: calendar)
//        self.monthFormatter.locale = Locale(identifier: "ja_JP")
        self.dayFormatter = DateFormatter(dateFormat: "d", calendar: calendar)
        self.weekDayFormatter = DateFormatter(dateFormat: "EEEEE", calendar: calendar)
        self.weekDayFormatter.locale = Locale(identifier: "ja_JP")
        self.fullFormatter = DateFormatter(dateFormat: "MMMM dd, yyyy", calendar: calendar)

        self.viewModel = CalendarViewModel(getAllEventsByMonthYear: calendarModule.getAllEventsByMonthYear, addEvent: calendarModule.addEvent, deleteEvent: calendarModule.deleteEvent,getHorses: getHorseModule.searchHorses)
        self.horseId = horseId
        self.horseName = horseName


    }

    var body: some View {
        ScrollView(showsIndicators: false){
            VStack {
    //            Text("Selected date: \(fullFormatter.string(from: selectedDate))")
    //                .bold()
    //                .foregroundColor(.red)


//                LazyVStack {
//                        ForEach(0..<viewModel.events.count, id : \.self){ index in
//                            Text(viewModel.events[index].title)
//
//                        }
//                }
                CalView(
                    calendar: calendar,
                    date: $selectedDate,
                    content: { date in
                        ZStack {
                                VStack(spacing: 0) {
//                                    Text("00").foregroundColor(.clear)
                                    Text(dayFormatter.string(from: date))
                                        .foregroundColor(Color("ColorPrimaryDark"))
                                        .font(Font.custom("roboto_bold",size: 10))
                                        .padding(.top,8)
                                        .padding(.bottom,4)
                                    VStack(spacing:0) {

                                        let detectedEvents = viewModel.events.filter({ e in
                                            let dateStart = formatter.date(from: e.date)
                                            let dateEnd = formatter.date(from: e.dateEnd!)
//                                                var eventType = EventOptions.getEventInfoByEventType(eventType: viewModel.events[index].eventType)!.info
                                            if((dateStart! ... dateEnd!).contains(date)){
                                                return true
                                            }
                                            return false
                                        })
                                        
                                        ForEach(detectedEvents.indices, id: \.self){ index in
                                            
                                            var eventType = EventOptions.getEventInfoByEventType(eventType: detectedEvents[index].eventType)!.info
                                            
                                            if(index < 3 ){
                                                
                                                Button(action: {
                                                    selectedEvent = detectedEvents[index]
                                                    showViewEventSheet = true
                                                }, label: {
                                                    Text(detectedEvents[index].title)
                                                        .frame(maxWidth:.infinity)
                                                        .lineLimit(1)
                                                        .truncationMode(.tail)
                                                        .foregroundColor(
                                                            formatter.string(from: date) == detectedEvents[index].date ? .white : .clear
                                                        )
                                                        .padding(.bottom,1)
                                                        .font(Font.custom("roboto",size: 7))
                                                        .background(eventType.colorDarker)
                                                        .padding(.bottom,1)
                                                })

                                                    
                                                        
                                            }
                                        }
                                        
                                        if(detectedEvents.count > 3){
                                            Text("+\((detectedEvents.count-4) + 1)")
                                                .frame(maxWidth:.infinity)
                                                .lineLimit(1)
                                                .truncationMode(.tail)
                                                .foregroundColor(.white)
                                                .padding(1)
                                                .font(Font.custom("roboto",size: 7))
                                                .background(Color.black)
                                                .padding(.bottom,1)
                                        }
                                        
                                        Spacer()
                                        
                                    }
                                    .frame(height:40)
                                    .padding(.top,4)

//                                    Spacer()
                                }
                        }
                        .frame(maxWidth: .infinity)
                        .frame(height:75)
//                            .accessibilityHidden(true)
                        .background(
                            calendar.isDate(date, inSameDayAs: selectedDate) ? Color("ColorDarkBackground")

                            : calendar.isDateInToday(date) ? Color("ColorDarkBackground")
                            : Color("ColorDarkBackground")
                            )
                        .border(.black, width: calendar.isDate(date, inSameDayAs: selectedDate) ? 0.5 : 0)
                        .padding(1)
                        .onTapGesture{
                            selectedDate = date
                            viewModel.activeEvents = viewModel.events.filter { e in
                                let dateStart = formatter.date(from: e.date)
                                let dateEnd = formatter.date(from: e.dateEnd!)
                                if((dateStart! ... dateEnd!).contains(date)){
                                    return true
                                }
                                return false
                        }
                        }
                    }
                    ,
                    trailing: { date in
                        VStack(spacing: 0) {
                            Text(dayFormatter.string(from: date))
                                .foregroundColor(Color("ColorPrimaryDark"))
                                .padding(.top,8)
                                .font(Font.custom("roboto_bold",size: 10))

                            VStack() {

                            }
                            .frame(height:40)
                            Spacer()
                        }
                        .frame(maxWidth: .infinity)
                        .frame(height:75)
                        .accessibilityHidden(true)
                        .background(Color("ColorDarkBackground"))
                        .padding(1)

                    },
                    header: { date in
                        VStack {
                            Text(weekDayFormatter.string(from: date))
                                .foregroundColor(Color("ColorPrimaryDark"))
                                .frame(maxWidth: .infinity)
                                .frame(height:60)

                        }


                    },
                    title: { date in
                        HStack {
                            Button {
//                                withAnimation {
                                    guard let newDate = calendar.date(
                                        byAdding: .month,
                                        value: -1,
                                        to: selectedDate
                                    ) else {
                                        return
                                    }
                                    onScrollMonthYear(newDate: newDate)
//                                }
                            } label: {
                                Label(
                                    title: {
                                        Text("Previous")
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                        
                                    },
                                    icon: {
                                        Image(systemName: "chevron.left")
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                        
                                    }
                                )
                                .labelStyle(IconOnlyLabelStyle())
                                .padding(.horizontal)
                                .frame(maxHeight: .infinity)
                            }
                            Spacer()
                            Text(monthFormatter.string(from: date))
                                .foregroundColor(Color("ColorPrimaryDark"))
                                .font(Font.custom("roboto_bold",size: 23))
                                .padding()
                            Spacer()
                            Button {
//                                withAnimation {
                                    guard let newDate = calendar.date(
                                        byAdding: .month,
                                        value: 1,
                                        to: selectedDate
                                    ) else {
                                        return
                                    }
                                    print("newdate is \(newDate)")

                                    onScrollMonthYear(newDate: newDate)
//                                }
                            } label: {
                                Label(
                                    title: {
                                        Text("Next")
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                    },
                                    icon: {
                                        Image(systemName: "chevron.right")
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                        
                                    }
                                )
                                .labelStyle(IconOnlyLabelStyle())
                                .padding(.horizontal)
                                .frame(maxHeight: .infinity)
                            }
                        }
//                        .padding(.bottom, 6)
                    }
                )
//                .equatable()
                Spacer()
            }//: VSTACK
            .background(Color.white)
//            .padding()
            
            HStack(spacing: 8){
                ZStack{
                    
                    Rectangle()
                        .fill(Color("ColorPrimaryDark"))
                        .frame(width:.infinity,height: 50)
                    
                    HStack {
                        Text("イベント予定")
                            .foregroundColor(.white)
                            .padding(.leading,12)
                        
                        Spacer()
                        
                        Button(action: {
                            showAddEventSheet = true
                            selectedEvent = nil
                        }, label: {
                            ZStack {
                                Capsule()
                                    .fill(Color("ColorPrimary"))
                                
                                Capsule()
                                    .fill(Color("ColorBlueButton"))
                                    .padding(.bottom,5)
                                Capsule()
                                    .strokeBorder(Color("ColorBlueButtonBorder"), lineWidth: 1.5)
                                    .padding(.bottom,5)
                                Text("イベントを追加")
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(.white)
                                    .padding(.top,10)
                                    .padding(.bottom,10)
                                    .padding(.trailing,24)
                                    .padding(.leading,24)
                                    .layoutPriority(1)
                                
                            }.frame(width: .infinity, height: 40)
                        })
                            .padding(.trailing,12)
                    }//:HSTACK
                }//:ZSTACK
                
            }//:HSTACK
            
            VStack {
                
                ForEach(viewModel.activeEvents.indices, id: \.self){ index in
                    var eventType = EventOptions.getEventInfoByEventType(eventType: viewModel.activeEvents[index].eventType)!.info
                    HStack(spacing: 0) {
                        Rectangle()
                            .fill(eventType.colorDarker)
                            .frame(width:6,height:.infinity)
//                            .padding(.trailing,6)
                        
                        ZStack {
                            Color(UIColor(eventType.colorLight))
                            let notSameDay = viewModel.activeEvents[index].date != viewModel.activeEvents[index].dateEnd
                            let dStart = dateAndTimeFormatter.date(from: "\(viewModel.activeEvents[index].date)")
                            
                            let dEnd = dateAndTimeFormatter.date(from: "\(viewModel.activeEvents[index].dateEnd!)")!
                            
                            
                            let startDayString = monthAndDayformatter.string(from: dStart!)
                            let endDayString = monthAndDayformatter.string(from: dEnd)

                    

                            

//                            Text("\(viewModel.activeEvents[index].start)\(notSameDay ? "-\(viewModel.activeEvents[index].end)" : "")")
//                                .font(.system(size: notSameDay ? 11 : 14, weight: .semibold))
//                                .foregroundColor(Color("ColorPrimaryDark"))
                            Text("\(startDayString)\(notSameDay ? "-\(endDayString)" : "")")
                                .font(.system(size: notSameDay ? 11 : 14, weight: .semibold))
                                .foregroundColor(Color("ColorPrimaryDark"))
                        }
                        .frame(width:75.5)
                        
                        ZStack {
                            Color(.white)
                            HStack {
                                VStack {
                                    Spacer()
                                    HStack {
                                        Text(viewModel.activeEvents[index].title)
                                            .font(.system(size: 13, weight: .semibold))
                                            .foregroundColor(Color("ColorPrimaryDark"))
                    
                                            .layoutPriority(1)
                                            .multilineTextAlignment(.leading)
                                        Spacer()
                                    }
                                    .frame(width:.infinity)

                                    
                                    HStack {
                                        Circle()
                                            .fill(
                                                viewModel.activeEvents[index].horse?.name ?? "" != "" ? Color("ColorLightGreen") : Color.clear
                                            )
                                            .frame(width:14, height:13)
                                        Text(viewModel.activeEvents[index].horse?.name ?? "")
                                            .font(.system(size: 14, weight: .semibold))
                                            .foregroundColor(Color("ColorPrimaryDark"))
                                        Spacer()
                                    }
                                    .frame(width:.infinity)
                                    Spacer()

                                }
                                .padding(.leading,12)
                                Spacer()
                                
                                Button(action: {
                                    selectedEvent = viewModel.activeEvents[index]
                                    showViewEventSheet = true
                                }, label: {
                                    ZStack {
                                        Capsule()
                                            .fill(eventType.colorDarker)
          
                                        Text("見る")
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
                            }
                    
                        }.layoutPriority(5)
                        
                        
                    }
                    .frame(height:55)
                    .padding(.leading,12)
                    .padding(.trailing,12)
                    .padding(.top,6)
                    .clipped()
                    .shadow(color: Color(red: 0, green: 0, blue: 0, opacity: 0.4), radius: 4, x: 6, y: 6)
                }
            }
            .padding(.top,2)
        } //: SCROLLVIEW
        .background(Color.white)
        .onChange(of: events) { selected in
            selectedDate = selectedDate
            viewModel.activeEvents = viewModel.events.filter { e in
                let dateStart = formatter.date(from: e.date)
                let dateEnd = formatter.date(from: e.dateEnd!)
                if((dateStart! ... dateEnd!).contains(selectedDate)){
                    return true
                }
                return false
            }
        }
        .onAppear(perform: {
            onScrollMonthYear(newDate: Date(),initial: true)
        })
        
        .fullScreenCover(isPresented: $showAddEventSheet) {
            AddEventSheetView(isPresented: $showAddEventSheet,activeEvent: $selectedEvent,activeMonth: self.$m,activeYear:self.$y, activeHorseId: self.horseId,activeHorseName: self.horseName)
                .environmentObject(viewModel)
        }
        .fullScreenCover(isPresented: $showViewEventSheet){
            ViewEventSheetView(isPresented: $showViewEventSheet,showAddEventSheetView:$showAddEventSheet,activeEvent: $selectedEvent,activeMonth: self.$m,activeYear:self.$y, activeHorseId: self.horseId)
                .environmentObject(viewModel)
        }

    }
    func onScrollMonthYear(newDate: Date, initial: Bool = false){
        viewModel.activeEvents = []
        let df = DateFormatter()
        df.dateFormat = "MM"
        let month = df.string(from: newDate)
        self.m = month
        df.dateFormat = "yyyy"
        let year = df.string(from: newDate)
        self.y = year
        print("month is -> \(month)")
        print("year is -> \(year)")
        print("horseId is -> \(horseId.wrappedValue)")
        self.events = viewModel.getAllEvents(stableId: stableId, month: month, year: year, horseId: self.horseId.wrappedValue)
        self.selectedDate = newDate


    }
    

}

// MARK: - Component

public struct CalView<Day: View, Header: View, Title: View, Trailing: View>: View {
    // Injected dependencies
    private var calendar: Calendar
    @Binding private var date: Date
    private let content: (Date) -> Day
    private let trailing: (Date) -> Trailing
    private let header: (Date) -> Header
    private let title: (Date) -> Title

    // Constants
    private let daysInWeek = 7

    public init(
        calendar: Calendar,
        date: Binding<Date>,
        @ViewBuilder content: @escaping (Date) -> Day,
        @ViewBuilder trailing: @escaping (Date) -> Trailing,
        @ViewBuilder header: @escaping (Date) -> Header,
        @ViewBuilder title: @escaping (Date) -> Title
    ) {
        self.calendar = calendar
        self._date = date
        self.content = content
        self.trailing = trailing
        self.header = header
        self.title = title
    }

    public var body: some View {
        let month = date.startOfMonth(using: calendar)
        let days = makeDays()

        return LazyVGrid(columns: Array(repeating: GridItem(.flexible(minimum:40), spacing: 0), count: daysInWeek), spacing: 0) {
            Section(header: title(month)) {
                ForEach(days.prefix(daysInWeek), id: \.self, content: header)
                ForEach(days, id: \.self) { date in
                    if calendar.isDate(date, equalTo: month, toGranularity: .month) {
                        content(date)
                    } else {
                        trailing(date)
                    }
                }
            }
        }
    }
}

// MARK: - Conformances

//extension CalView: Equatable {
//    public static func == (lhs: CalView<Day, Header, Title, Trailing>, rhs: CalView<Day, Header, Title, Trailing>) -> Bool {
//        lhs.calendar == rhs.calendar && lhs.date == rhs.date
//    }
//}

// MARK: - Helpers

private extension CalView {
    func makeDays() -> [Date] {
        guard let monthInterval = calendar.dateInterval(of: .month, for: date),
              let monthFirstWeek = calendar.dateInterval(of: .weekOfMonth, for: monthInterval.start),
              let monthLastWeek = calendar.dateInterval(of: .weekOfMonth, for: monthInterval.end - 1)
        else {
            return []
        }

        let dateInterval = DateInterval(start: monthFirstWeek.start, end: monthLastWeek.end)
        return calendar.generateDays(for: dateInterval)
    }
}

struct DateWithEvents {
    var date: Date
    var filteredEvents: [Event]
}

private extension Calendar {
    func generateDates(
        for dateInterval: DateInterval,
        matching components: DateComponents
    ) -> [Date] {
        var dates = [dateInterval.start]

        enumerateDates(
            startingAfter: dateInterval.start,
            matching: components,
            matchingPolicy: .nextTime
        ) { date, _, stop in
            guard let date = date else { return }

            guard date < dateInterval.end else {
                stop = true
                return
            }

            dates.append(date)
        }

        return dates
    }

    func generateDays(for dateInterval: DateInterval) -> [Date] {
        generateDates(
            for: dateInterval,
            matching: dateComponents([.hour, .minute, .second], from: dateInterval.start)
        )
    }
}

private extension Date {
    func startOfMonth(using calendar: Calendar) -> Date {
        calendar.date(
            from: calendar.dateComponents([.year, .month], from: self)
        ) ?? self
    }
}

private extension DateFormatter {
    convenience init(dateFormat: String, calendar: Calendar) {
        self.init()
        self.dateFormat = dateFormat
        self.calendar = calendar
    }
}

// MARK: - Previews

#if DEBUG
struct CalendarView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            CalendarView(calendar: Calendar(identifier: .gregorian), horseId: .constant(0),horseName: .constant(""))
            CalendarView(calendar: Calendar(identifier: .islamicUmmAlQura), horseId: .constant(0),horseName: .constant(""))
            CalendarView(calendar: Calendar(identifier: .hebrew), horseId: .constant(0),horseName: .constant(""))
            CalendarView(calendar: Calendar(identifier: .indian), horseId: .constant(0),horseName: .constant(""))
        }
    }
}
#endif
