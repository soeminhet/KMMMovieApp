//
//  LoadingView.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LoadingView: View {
    var body: some View {
        VStack(alignment: .center) {
            Spacer()
            
            IndeterminateLinearProgressBar()
                .padding(.horizontal, 6)
            
            IndeterminateLinearProgressBar()
            
            IndeterminateLinearProgressBar()
                .padding(.horizontal, 6)
            
            Spacer()
        }
        .frame(width: 50, height: 50)
        .padding()
        .background(
            Circle()
                .fill(.white)
        )
        .shadow(radius: 10)
    }
}

#Preview {
    LoadingView()
}
