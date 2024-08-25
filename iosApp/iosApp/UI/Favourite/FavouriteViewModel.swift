//
//  FavouriteViewModel.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

@MainActor final class FavouriteViewModel: ObservableObject {
    
    private let getAllFavouriteMoviesUseCase = GetAllFavouriteMoviesUseCase.init()
    private let deleteFavouriteUseCase = DeleteFavouriteMovieUseCase.init()
    
    @Published var movies: [MovieUiModel] = []
    @Published var errorMsg: String = ""

    func loadAllFavourites() async {
        let result = getAllFavouriteMoviesUseCase.invoke()
        for await movies in result {
            withAnimation {
                self.movies = movies.map { movie in
                    MovieUiModel(
                        id: Int32(truncating: movie.id!),
                        title: movie.title,
                        description: movie.description_,
                        imageUrl: movie.imageUrl,
                        backdropUrl: movie.backdropUrl,
                        releaseDate: movie.releaseDate,
                        votePercentage: movie.votePercentage,
                        isFavourite: true
                    )
                }
            }
        }
    }
    
    func removeFavourite(movie: MovieUiModel) async {
        do {
            try await deleteFavouriteUseCase.invoke(id: Int64(movie.id))
        } catch let error {
            errorMsg = error.localizedDescription
        }
    }
}
