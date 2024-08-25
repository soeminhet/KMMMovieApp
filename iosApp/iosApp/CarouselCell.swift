//
//  CarouselCell.swift
//  iosApp
//
//  Created by Soe Min Htet on 24/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import ACarousel
import SDWebImageSwiftUI

struct CarouselCell: View {
    
    let carousels: [CarouselUiModel]
    
    @State private var currentCarouselIndex = 0
    
    var body: some View {
        if !carousels.isEmpty {
            ACarousel(
                carousels,
                id: \.id,
                index: $currentCarouselIndex,
                spacing: 16,
                sidesScaling: 0.8
            ) { movie in
                ZStack(alignment: .bottomLeading) {
                    WebImage(url: URL(string: movie.image)) { image in
                        image.resizable()
                    } placeholder: {
                        Rectangle()
                            .frame(maxWidth: .infinity)
                            .foregroundColor(.gray)
                    }
                    .indicator(.activity)
                    .frame(maxWidth: .infinity)
                    .scaledToFit()
                    .cornerRadius(20)
                    
                    Text(movie.title)
                        .foregroundStyle(.white)
                        .padding(.horizontal, 16)
                        .padding(.bottom, 16)
                        .font(.title3)
                        .fontWeight(.medium)
                        .lineLimit(/*@START_MENU_TOKEN@*/2/*@END_MENU_TOKEN@*/)
                        .truncationMode(.tail)
                }
            }
            .frame(maxWidth: .infinity)
            .aspectRatio(2, contentMode: .fit)
            
            HStack {
                ForEach(0..<carousels.count, id: \.self) { index in
                    Circle()
                        .fill(currentCarouselIndex == index ? Color.theme.orange : .gray)
                        .frame(width: 8, height: 8)
                }
            }
        }
    }
}

#Preview {
    CarouselCell(
        carousels: [
            CarouselUiModel(id: 1, image: "", title: "Testing 1"),
            CarouselUiModel(id: 2, image: "", title: "Testing 2"),
            CarouselUiModel(id: 3, image: "", title: "Testing 3")
        ]
    )
}
