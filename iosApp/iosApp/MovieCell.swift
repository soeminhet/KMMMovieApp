//
//  MovieCell.swift
//  iosApp
//
//  Created by Soe Min Htet on 18/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import SDWebImageSwiftUI

struct MovieCell: View {
    
    let movie: MovieUiModel
    let onClickFavourite: (MovieUiModel) -> Void
    
    var body: some View {
        ZStack(alignment: .topTrailing) {
            VStack(alignment: .leading, spacing: 0) {
                WebImage(url: URL(string: movie.imageUrl)) { image in
                    image.resizable()
                } placeholder: {
                    Rectangle()
                        .foregroundColor(.gray)
                }
                .indicator(.activity)
                .frame(maxWidth: .infinity)
                .scaledToFit()
                .cornerRadius(20)
                
                MovieVoting(movie: movie)
                
                VStack(alignment: .leading) {
                    Text(movie.title)
                        .padding(.horizontal, 16)
                        .font(.body)
                        .fontWeight(.medium)
                        .lineLimit(/*@START_MENU_TOKEN@*/2/*@END_MENU_TOKEN@*/)
                        .truncationMode(.tail)
                    
                    Text(movie.releaseDate)
                        .font(.footnote)
                        .padding(.horizontal, 16)
                }
                .frame(height: 90, alignment: .top)
                .offset(y: -10)
            }
            
            Button {
                onClickFavourite(movie)
            } label: {
                Image(systemName: movie.isFavourite ? "heart.fill" : "heart")
                    .tint(Color.theme.red)
                    .font(.title2)
            }
            .padding()
        }
    }
}

struct MovieVoting: View {
    
    let movie: MovieUiModel
    
    private var color: Color {
        switch movie.voteAverage {
        case .high:
            return Color.theme.green
        case .medium:
            return Color.theme.orange
        case .low:
            return Color.theme.red
        }
    }
    
    var body: some View {
        Circle()
            .fill(Color.theme.navyBlue)
            .overlay(
                Circle()
                    .stroke(color, lineWidth: 4)
            )
            .overlay(
                Text(movie.votePercentageString)
                    .foregroundStyle(.white)
                    .font(.footnote)
                    .bold()
            )
            .frame(width: 50, height: 50)
            .padding(.horizontal, 16)
            .offset(y: -20)
    }
}

#Preview {
    MovieCell(
        movie: MovieUiModel.companion.example,
        onClickFavourite: { _ in }
    ).frame(width: 250)
}
