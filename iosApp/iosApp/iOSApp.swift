import SwiftUI
import shared

@main
struct iOSApp: App {
    
    @ObservedObject var router = Router()
    
    init() {
        KoinHelperKt.doInitKoin()
    }
    
	var body: some Scene {
        WindowGroup {
            NavigationStack(path: $router.navPath) {
                HomeView()
                    .navigationDestination(for: CategoryRoute.self) { route in
                        CategoryView(categoryRoute: route)
                    }
                    .navigationDestination(for: String.self) { route in
                        if route == "Favourite" {
                            FavouriteView()
                        }
                    }
            }
            .environmentObject(router)
        }
	}
}
