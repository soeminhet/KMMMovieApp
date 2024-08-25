//
//  CategoryView.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CategoryView: View {
    
    let categoryRoute: CategoryRoute
    @EnvironmentObject var router: Router
    @StateObject private var vm: CategoryViewModel
    
    init(categoryRoute: CategoryRoute) {
        self.categoryRoute = categoryRoute
        self._vm = StateObject(wrappedValue: CategoryViewModel(categoryRoute: categoryRoute))
    }
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                CategoryTitleView(
                    title: categoryRoute.title,
                    onBack: { router.navigateBack() }
                )
                
                Divider()
                    .padding(.top, 10)
                
                ScrollView(.vertical, showsIndicators: false) {
                    Spacer(minLength: 20)
                    
                    VerticalPagingCell(
                        movies: vm.uiModels,
                        isLoading: vm.loading,
                        isMoreLoading: vm.moreLoading,
                        onFetchMore: { await vm.fetchMore() },
                        onClickFavourite: { movie in
                            Task {
                                await vm.toggleFavourite(movie: movie)
                            }
                        }
                    )
                }
            }
            
            if vm.loading {
                LoadingView()
            }
        }
        .toolbar(.hidden, for: .automatic)
        .task {
            await vm.startFetch()
        }
    }
}

#Preview {
    NavigationStack {
        CategoryView(categoryRoute: .Popular)
            .environmentObject(Router())
    }
}
