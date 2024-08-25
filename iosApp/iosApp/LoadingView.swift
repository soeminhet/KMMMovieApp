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
        ProgressView()
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
