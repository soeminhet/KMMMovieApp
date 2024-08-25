//
//  VerticalPagingCell.swift
//  iosApp
//
//  Created by Soe Min Htet on 24/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VerticalPagingCell: View {
    
    let title: String?
    let movies: [MovieUiModel]
    let isLoading: Bool
    let isMoreLoading: Bool
    let onFetchMore: () async -> Void
    let onClickFavourite: (MovieUiModel) -> Void
    
    init(
        title: String? = nil,
        movies: [MovieUiModel],
        isLoading: Bool,
        isMoreLoading: Bool,
        onFetchMore: @escaping () async -> Void,
        onClickFavourite: @escaping (MovieUiModel) -> Void
    ) {
        self.title = title
        self.movies = movies
        self.isLoading = isLoading
        self.isMoreLoading = isMoreLoading
        self.onFetchMore = onFetchMore
        self.onClickFavourite = onClickFavourite
    }
    
    var body: some View {
        VStack(alignment: .center, spacing: 6) {
            if let title = title, !movies.isEmpty {
                HorizontalTitleCell(title: title)
            }
            
            LazyVGrid(
                columns: [GridItem(.adaptive(minimum: 150))],
                spacing: 16
            ) {
                ForEach(movies, id: \.id) { movie in
                    MovieCell(
                        movie: movie,
                        onClickFavourite: onClickFavourite
                    )
                    .task {
                        if movie == movies.last && !isLoading && !isMoreLoading {
                            await onFetchMore()
                        }
                    }
                }
            }
            .scenePadding(.horizontal)
            
            if isMoreLoading {
                ProgressView()
            }
        }
    }
}

#Preview {
    VerticalPagingCell(
        title: "Upcoming",
        movies: [MovieUiModel.companion.example],
        isLoading: false,
        isMoreLoading: false,
        onFetchMore: { },
        onClickFavourite: { _ in}
    )
}
