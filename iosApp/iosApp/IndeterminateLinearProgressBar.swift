//
//  IndeterminateLinearProgressBar.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct IndeterminateLinearProgressBar: View {
    
    let trackColor: Color
    let trackBackground: Color
    let trackHeight: CGFloat
    
    init(
        trackColor: Color = Color.theme.orange,
        trackBackground: Color = .gray,
        trackHeight: CGFloat = 3
    ) {
        self.trackColor = trackColor
        self.trackBackground = trackBackground
        self.trackHeight = trackHeight
    }
    
    @State private var isAnimating = false
    
    var body: some View {
        ZStack(alignment: .leading) {
            GeometryReader { geometry in
                trackBackground.opacity(0.3)
                    .frame(width: geometry.size.width, height: trackHeight)
                
                trackColor
                    .frame(width: geometry.size.width / 3, height: trackHeight)
                    .offset(x: isAnimating ? geometry.size.width : -geometry.size.width)
                    .animation(
                        Animation.linear(duration: 1.0).repeatForever(autoreverses: false),
                        value: isAnimating
                    )
            }
        }
        .cornerRadius(2)
        .onAppear {
            self.isAnimating = true
        }
    }
}

#Preview {
    IndeterminateLinearProgressBar()
}
