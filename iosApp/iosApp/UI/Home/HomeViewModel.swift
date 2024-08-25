//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Soe Min Htet on 18/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

extension HomeView {
    @MainActor class HomeViewModel: ObservableObject {
        
        private let getAllFavouriteMoviesUseCase = GetAllFavouriteMoviesUseCase.init()
        private let getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCase.init()
        private let getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase.init()
        private let getPopularMoviesUseCase = GetPopularMoviesUseCase.init()
        private let getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase.init()
        private let insertFavouriteUseCase = InsertOrUpdateFavouriteMovieUseCase.init()
        private let deleteFavouriteUseCase = DeleteFavouriteMovieUseCase.init()
        
        @Published private var favouriteMovies: [FavouriteMovieModel] = []
        @Published private var topRatedMovies: [MovieModel] = []
        @Published private var popularMovies: [MovieModel] = []
        @Published private var upcomingMovies: [MovieModel] = []
        
        @Published private(set) var nowPlayingUiModels: [CarouselUiModel] = []
        @Published private(set) var topRatedUiModels: [MovieUiModel] = []
        @Published private(set) var popularUiMovies: [MovieUiModel] = []
        @Published private(set) var upcomingUiMovies: [MovieUiModel] = []
        @Published private(set) var errorMsg: String = ""
        
        private var currentUpcomingPage: Int32 = 1
        private var endOfPagination: Bool = false
        @Published private(set) var isMoreUpcomingLoading: Bool = false
        
        @Published private(set) var loading: Bool = false
        @Published private var isNowPlayingLoading: Bool = false
        @Published private var isPopularLoading: Bool = false
        @Published private var isTopRatedLoading: Bool = false
        @Published private var isUpcomingLoading: Bool = false
        
        private var cancellables = Set<AnyCancellable>()
        
        init() {
            observeLoading()
            observeFavouritesWithTopRated()
            observeFavouritesWithPopular()
            observeFavouritesWithUpcoming()
        }
        
        func startFetch() async {
            await withTaskGroup(of: Void.self) { group in
                group.addTask { await self.loadAllFavourites() }
                group.addTask { await self.fetchNowPlaying() }
                group.addTask { await self.fetchTopRated() }
                group.addTask { await self.fetchPopular() }
                group.addTask { await self.fetchUpcoming() }
                await group.waitForAll()
            }
        }
        
        private func observeLoading() {
            $isNowPlayingLoading
                .combineLatest($isTopRatedLoading, $isPopularLoading, $isUpcomingLoading) { nowPlaying, topRated, popular, upcoming in
                    nowPlaying || topRated || popular || upcoming
                }
                .sink { value in
                    self.loading = value
                }
                .store(in: &cancellables)
        }
        
        private func observeFavouritesWithTopRated() {
            $favouriteMovies
                .combineLatest($topRatedMovies)
                .map(uiModelMapper)
                .sink { [weak self] movies in
                    self?.topRatedUiModels = movies
                }
                .store(in: &cancellables)
        }
        
        private func observeFavouritesWithPopular() {
            $favouriteMovies
                .combineLatest($popularMovies)
                .map(uiModelMapper)
                .sink { [weak self] movies in
                    self?.popularUiMovies = movies
                }
                .store(in: &cancellables)
        }
        
        private func observeFavouritesWithUpcoming() {
            $favouriteMovies
                .combineLatest($upcomingMovies)
                .map(uiModelMapper)
                .sink { [weak self] movies in
                    self?.upcomingUiMovies = movies
                }
                .store(in: &cancellables)
        }
        
        
        private func uiModelMapper(favs: [FavouriteMovieModel], movies: [MovieModel]) -> [MovieUiModel] {
            return movies.map { movie -> MovieUiModel in
                let matched = favs.first { fav in Int32(truncating: fav.id ?? -1) == movie.id }
                return movie.toUiModel(isFavourite: matched != nil)
            }
        }
        
        private func carouselUiModelMapper(movies: [MovieModel]) -> [CarouselUiModel] {
            return movies.map { movie -> CarouselUiModel in
                CarouselUiModel(id: movie.id, image: movie.backdropUrl, title: movie.title)
            }
        }
        
        private func loadAllFavourites() async {
            let result = getAllFavouriteMoviesUseCase.invoke()
            for await movie in result {
                self.favouriteMovies = movie
            }
        }
        
        private func fetchNowPlaying() async {
            self.isNowPlayingLoading = true
            do {
                let result = try await getNowPlayingMoviesUseCase.invoke(page: Int32(1))
                result.onRight { movies in
                    let data = movies.toMovieModels(prefixLength: 5)
                    self.nowPlayingUiModels = self.carouselUiModelMapper(movies: data)
                }.onLeft { error in
                    self.errorMsg = error?.message ?? "Error"
                }
                self.isNowPlayingLoading = false
            } catch let error {
                self.isNowPlayingLoading = false
                self.errorMsg = error.localizedDescription
            }
        }
        
        private func fetchTopRated() async {
            self.isTopRatedLoading = true
            do {
                let result = try await getTopRatedMoviesUseCase.invoke(page: Int32(1))
                result.onRight { movies in
                    self.topRatedMovies = movies.toMovieModels(prefixLength: 5)
                }.onLeft { error in
                    self.errorMsg = error?.message ?? "Error"
                }
                self.isTopRatedLoading = false
            } catch let error {
                self.isTopRatedLoading = false
                self.errorMsg = error.localizedDescription
            }
        }
        
        private func fetchPopular() async {
            self.isPopularLoading = true
            do {
                let result = try await getPopularMoviesUseCase.invoke(page: Int32(1))
                result.onRight { movies in
                    self.popularMovies = movies.toMovieModels(prefixLength: 5)
                }.onLeft { error in
                    self.errorMsg = error?.message ?? "Error"
                }
                self.isPopularLoading = false
            } catch let error {
                self.isPopularLoading = false
                self.errorMsg = error.localizedDescription
            }
        }
        
        private func fetchUpcoming() async {
            if currentUpcomingPage > 1 {
                isMoreUpcomingLoading = true
            } else {
                isUpcomingLoading = true
            }
            
            do {
                let result = try await getUpcomingMoviesUseCase.invoke(page: currentUpcomingPage)
                result.onRight { movies in
                    self.endOfPagination = movies == nil || movies?.count == 0
                    self.upcomingMovies = self.upcomingMovies + movies.toMovieModels()
                }.onLeft { error in
                    self.errorMsg = error?.message ?? "Error"
                }
                self.isUpcomingLoading = false
                self.isMoreUpcomingLoading = false
            } catch let error {
                self.isUpcomingLoading = false
                self.isMoreUpcomingLoading = false
                self.errorMsg = error.localizedDescription
            }
        }
        
        func fetchMoreUpcoming() async {
            if(!endOfPagination) {
                currentUpcomingPage += 1
                await fetchUpcoming()
            }
        }
        
        func toggleFavourite(movie: MovieUiModel) async {
            do {
                if movie.isFavourite {
                    try await deleteFavouriteUseCase.invoke(id: Int64(movie.id))
                } else {
                    try await insertFavouriteUseCase.invoke(movie: movie.toFavouriteMovieModel())
                }
            } catch let error {
                self.errorMsg = error.localizedDescription
            }
        }
    }
}

extension NSArray? {
    func toMovieModels(prefixLength: Int? = nil) -> [MovieModel] {
        guard let self else { return [] }
        let movieModels = self as! [MovieModel]
        if let length = prefixLength {
            return Array(movieModels.prefix(length))
        } else {
            return movieModels
        }
    }
}
