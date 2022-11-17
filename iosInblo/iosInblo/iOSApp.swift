import SwiftUI
import shared

@main
struct iOSApp: App {
    
    private let getHorseListModule = GetHorseListModule(networkModule: NetworkModule())
    @AppStorage("userId") var userId: Int = 0

    
//
	var body: some Scene {
        

		return WindowGroup {
            if(userId == 0){ // not logged in
                LoginView()
            }else{ // logged in
                DashboardView()
            }
		}
	}
}
