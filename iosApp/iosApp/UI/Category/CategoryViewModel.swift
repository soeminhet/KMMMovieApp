//
//  CategoryViewModel.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Combine
import Foundation
import shared

@MainActor final class CategoryViewModel: ObservableObject {
    
    let categoryRoute: CategoryRoute
    
    private let getAllFavouriteMoviesUseCase = GetAllFavouriteMoviesUseCase.init()
    private let getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase.init()
    private let getPopularMoviesUseCase = GetPopularMoviesUseCase.init()
    private let insertFavouriteUseCase = InsertOrUpdateFavouriteMovieUseCase.init()
    private let deleteFavouriteUseCase = DeleteFavouriteMovieUseCase.init()
    
    private var currentPage: Int32 = 1
    private var endOfPagination: Bool = false
    
    @Published private var movies: [MovieModel] = []
    @Published private var favouriteMovies: [FavouriteMovieModel] = []
    
    @Published private(set) var errorMsg: String = ""
    @Published private(set) var loading: Bool = false
    @Published private(set) var moreLoading: Bool = false
    @Published private(set) var uiModels: [MovieUiModel] = []
    
    private var cancellables = Set<AnyCancellable>()
    
    init(categoryRoute: CategoryRoute) {
        self.categoryRoute = categoryRoute
        switch categoryRoute {
        case .Popular:
            observeFavouritesWithPopular()
            break
        case .TopRated:
            observeFavouritesWithTopRated()
            break
        }
    }
    
    func startFetch() async {
        await withTaskGroup(of: Void.self) { group in
            group.addTask { await self.loadAllFavourites() }
            group.addTask { await self.fetchCategory() }
            await group.waitForAll()
        }
    }
    
    func fetchMore() async {
        if !endOfPagination && !loading && !moreLoading {
            currentPage += 1
            await fetchCategory()
        }
    }
    
    private func fetchCategory() async {
        switch categoryRoute {
        case .Popular:
            await fetchPopular()
            break;
        case .TopRated:
            await fetchTopRated()
            break;
        }
    }
    
    private func fetchTopRated() async {
        setLoading(true)
        do {
            let result = try await getTopRatedMoviesUseCase.invoke(page: currentPage)
            result.onRight { movies in
                self.endOfPagination = movies?.count == nil || movies?.count == 1
                self.movies = self.movies + movies.toMovieModels()
            }.onLeft { error in
                self.errorMsg = error?.message ?? "Error"
            }
            setLoading(false)
        } catch let error {
            setLoading(false)
            self.errorMsg = error.localizedDescription
        }
    }
    
    private func fetchPopular() async {
        setLoading(true)
        do {
            let result = try await getPopularMoviesUseCase.invoke(page: currentPage)
            result.onRight { movies in
                self.endOfPagination = movies?.count == nil || movies?.count == 1
                self.movies = self.movies + movies.toMovieModels()
            }.onLeft { error in
                self.errorMsg = error?.message ?? "Error"
            }
            setLoading(false)
        } catch let error {
            setLoading(false)
            self.errorMsg = error.localizedDescription
        }
    }
    
    private func setLoading(_ value: Bool) {
        if value {
            self.loading = currentPage == 1
            self.moreLoading = currentPage > 1
        } else {
            self.loading = false
            self.moreLoading = false
        }
    }
    
    private func uiModelMapper(favs: [FavouriteMovieModel], movies: [MovieModel]) -> [MovieUiModel] {
        return movies.map { movie -> MovieUiModel in
            let matched = favs.first { fav in Int32(truncating: fav.id ?? -1) == movie.id }
            return movie.toUiModel(isFavourite: matched != nil)
        }
    }
    
    private func loadAllFavourites() async {
        let result = getAllFavouriteMoviesUseCase.invoke()
        for await movie in result {
            self.favouriteMovies = movie
        }
    }
    
    private func observeFavouritesWithTopRated() {
        $favouriteMovies
            .combineLatest($movies)
            .map(uiModelMapper)
            .sink { [weak self] movies in
                self?.uiModels = movies
            }
            .store(in: &cancellables)
    }
    
    private func observeFavouritesWithPopular() {
        $favouriteMovies
            .combineLatest($movies)
            .map(uiModelMapper)
            .sink { [weak self] movies in
                self?.uiModels = movies
            }
            .store(in: &cancellables)
    }
    
    func toggleFavourite(movie: MovieUiModel) async {
        print(movie)
        do {
            if movie.isFavourite {
                try await deleteFavouriteUseCase.invoke(id: Int64(movie.id))
            } else {
                try await insertFavouriteUseCase.invoke(movie: movie.toFavouriteMovieModel())
            }
        } catch let error {
            self.errorMsg = error.localizedDescription
            print(error.localizedDescription)
        }
    }
}

