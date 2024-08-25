//
//  HomeView.swift
//  iosApp
//
//  Created by Soe Min Htet on 17/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import ACarousel
import SwiftUI
import shared

struct HomeView: View {
    
    @EnvironmentObject var router: Router
    @StateObject var vm = HomeViewModel()
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                HomeToolbar(
                    onClickFavourite: { router.navigate(to: "Favourite") }
                )
                
                ScrollView(.vertical, showsIndicators: false) {
                    Spacer(minLength: 20)
                    
                    VStack(spacing: 24) {
                        CarouselCell(carousels: vm.nowPlayingUiModels)
                        
                        HorizontalMoviesCell(
                            title: "Top Rated",
                            movies: vm.topRatedUiModels,
                            onClickMore: { router.navigate(to: CategoryRoute.TopRated) },
                            onClickFavourite: { movie in
                                Task {
                                    await vm.toggleFavourite(movie: movie)
                                }
                            }
                        )
                        
                        HorizontalMoviesCell(
                            title: "Popular",
                            movies: vm.popularUiMovies,
                            onClickMore: { router.navigate(to: CategoryRoute.Popular) },
                            onClickFavourite: { movie in
                                Task {
                                    await vm.toggleFavourite(movie: movie)
                                }
                            }
                        )
                        
                        VerticalPagingCell(
                            title: "Upcoming",
                            movies: vm.upcomingUiMovies,
                            isLoading: vm.loading,
                            isMoreLoading: vm.isMoreUpcomingLoading,
                            onFetchMore: { await vm.fetchMoreUpcoming() },
                            onClickFavourite: { movie in
                                Task {
                                    await vm.toggleFavourite(movie: movie)
                                }
                            }
                        )
                        
                        Spacer()
                    }
                }
            }
            
            if vm.loading {
                LoadingView()
            }
        }
        .task {
            await vm.startFetch()
        }
    }
}

#Preview {
    HomeView()
        .environmentObject(Router())
}
