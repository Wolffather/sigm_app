package ru.hey_savvy.sigm_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform