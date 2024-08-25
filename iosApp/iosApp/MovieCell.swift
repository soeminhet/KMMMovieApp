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
    
    var body: some View {
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
            
            Text(movie.title)
                .padding(.horizontal, 16)
                .font(.title3)
                .fontWeight(.medium)
                .offset(y: -10)
                .lineLimit(/*@START_MENU_TOKEN@*/2/*@END_MENU_TOKEN@*/)
                .truncationMode(.tail)
            
            Text(movie.releaseDate)
                .padding(.horizontal, 16)
                .offset(y: -10)
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
        movie: MovieUiModel.companion.example
    ).frame(width: 250)
}
