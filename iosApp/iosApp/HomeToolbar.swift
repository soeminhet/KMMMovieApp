//
//  HomeToolbar.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct HomeToolbar: View {
    
    let onClickFavourite: () -> Void
    
    var body: some View {
        HStack {
            Text("Welcome Back")
                .font(.title2)
            
            Spacer()
            
            Button {
                onClickFavourite()
            } label: {
                Image(systemName: "heart")
                    .tint(.primary)
                    .font(.title2)
            }
        }
        .frame(maxWidth: .infinity, minHeight: 50)
        .scenePadding(.horizontal)
    }
}

#Preview {
    HomeToolbar(
        onClickFavourite: {}
    )
}
