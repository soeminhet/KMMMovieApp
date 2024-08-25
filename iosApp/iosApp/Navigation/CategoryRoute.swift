//
//  CategoryRoute.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

enum CategoryRoute: String, Hashable {
    case Popular = "Pouplar Movies"
    case TopRated = "TopRated Movies"
    
    var title: String {
        self.rawValue
    }
    
    static func fromName(_ name: String) -> CategoryRoute {
        return CategoryRoute(rawValue: name) ?? Popular
    }
}
