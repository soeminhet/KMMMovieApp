//
//  Router.swift
//  iosApp
//
//  Created by Soe Min Htet on 24/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

@MainActor public final class Router: ObservableObject {
    @Published var navPath = NavigationPath()
    public init() {}

    public func navigate(to destination: any Hashable) {
        print(navPath.count)
        navPath.append(destination)
        print(navPath.count)
    }

    public func navigateBack() {
        navPath.removeLast()
    }

    public func navigateToRoot() {
        navPath.removeLast(navPath.count)
    }
}
