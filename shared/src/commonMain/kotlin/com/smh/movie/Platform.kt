package com.smh.movie

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform