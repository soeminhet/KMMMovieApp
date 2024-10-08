//
//  HorizontalMoviesCell.swift
//  iosApp
//
//  Created by Soe Min Htet on 24/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HorizontalTitleCell: View {
    
    let title: String
    let onClickMore: (() -> Void)?
    
    init(title: String, onClickMore: (() -> Void)? = nil) {
        self.title = title
        self.onClickMore = onClickMore
    }
    
    var body: some View {
        HStack(alignment: .center) {
            Text(title)
                .font(.title3)
                
            Spacer()
            
            if let onClick = onClickMore {
                Button {
                    onClick()
                } label: {
                    Image(systemName: "chevron.right")
                        .tint(.primary)
                }
            }
        }
        .scenePadding(.horizontal)
    }
}


struct HorizontalMoviesCell: View {
    
    let title: String
    let movies: [MovieUiModel]
    let onClickMore: () -> Void
    let onClickFavourite: (MovieUiModel) -> Void
    
    init(
        title: String,
        movies: [MovieUiModel],
        onClickMore: @escaping () -> Void,
        onClickFavourite: @escaping (MovieUiModel) -> Void
    ) {
        self.title = title
        self.movies = movies
        self.onClickMore = onClickMore
        self.onClickFavourite = onClickFavourite
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            if !movies.isEmpty {
                HorizontalTitleCell(title: title, onClickMore: onClickMore)
            }
            
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(alignment: .top, spacing: 16) {
                    ForEach(movies, id: \.id) { movie in
                        MovieCell(
                            movie: movie,
                            onClickFavourite: onClickFavourite
                        )
                        .frame(width: 200)
                    }
                }
                .scenePadding(.horizontal)
            }
        }
    }
}

#Preview {
    HorizontalMoviesCell(
        title: "Title",
        movies: [MovieUiModel.companion.example],
        onClickMore: {},
        onClickFavourite: { _ in }
    )
}
