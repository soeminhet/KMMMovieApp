package com.smh.movie.domain.model

enum class VoteAverage {
    High, Medium, Low
}

data class MovieModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val votePercentage: Int,
) {
    val votePercentageString: String get() = "$votePercentage%"

    val votePercentageFloat: Float get() = votePercentage / 100f

    val voteAverage: VoteAverage get() {
        return when {
            votePercentage >= 70 -> VoteAverage.High
            votePercentage in 40..69 -> VoteAverage.Medium
            else -> VoteAverage.Low
        }
    }

    companion object {
        val example = MovieModel(
            id = 1,
            title = "The Last Horizon",
            description = "A thrilling journey through space and time as a crew of astronauts attempts to save Earth from an impending cosmic disaster.",
            imageUrl = "https://example.com/images/the-last-horizon.jpg",
            releaseDate = "2024-12-15",
            backdropUrl = "https://example.com/images/the-last-horizon-backdrop.jpg",
            votePercentage = 27
        )
    }
}