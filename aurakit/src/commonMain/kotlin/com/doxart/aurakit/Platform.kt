package com.doxart.aurakit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform