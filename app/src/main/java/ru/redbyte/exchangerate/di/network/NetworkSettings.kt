package ru.redbyte.exchangerate.di.network

interface NetworkSettings {
    val baseUrl: String

    val readTimeout: Long
    val connectionTimeout: Long

    val logRequests: Boolean
}