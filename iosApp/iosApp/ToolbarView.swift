//
//  CategoryTitleView.swift
//  iosApp
//
//  Created by Soe Min Htet on 25/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ToolbarView: View {
    
    let title: String
    let onBack: () -> Void
    
    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Button {
                    onBack()
                } label: {
                    Image(systemName: "arrow.backward")
                        .font(.title3)
                        .tint(.primary)
                }
                
                Spacer()
                
                Text(title)
                    .font(.title2)
                
                Spacer()
                
                Image(systemName: "arrow.backward")
                    .font(.title3)
                    .tint(.primary)
                    .opacity(0)
            }
            .scenePadding(.horizontal)
            .frame(maxWidth: .infinity, minHeight: 50, alignment: .center)
            
            Divider()
                .padding(.top, 10)
        }
    }
}


#Preview {
    ToolbarView(
        title: "Title",
        onBack: {}
    )
}
