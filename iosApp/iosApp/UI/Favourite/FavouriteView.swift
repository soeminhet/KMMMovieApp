//
//  FavouriteView.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct FavouriteView: View {
    
    @EnvironmentObject var router: Router
    @StateObject private var vm = FavouriteViewModel()
    
    var body: some View {
        VStack(spacing: 0) {
            ToolbarView(
                title: "Favourites",
                onBack: { router.navigateBack() }
            )
            
            ScrollView(.vertical, showsIndicators: false) {
                Spacer(minLength: 20)
                
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 150))],
                    spacing: 16
                ) {
                    ForEach(vm.movies, id: \.id) { movie in
                        MovieCell(
                            movie: movie,
                            onClickFavourite: { movie in
                                Task {
                                    await vm.removeFavourite(movie: movie)
                                }
                            }
                        )
                    }
                }
                .scenePadding(.horizontal)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .toolbar(.hidden, for: .automatic)
        .task {
            await vm.loadAllFavourites()
        }
    }
}

#Preview {
    FavouriteView()
        .environmentObject(Router())
}
