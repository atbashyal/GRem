package com.lazyracoon.grem

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform