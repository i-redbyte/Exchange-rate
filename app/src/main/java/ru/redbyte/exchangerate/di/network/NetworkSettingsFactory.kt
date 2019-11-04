package ru.redbyte.exchangerate.di.network

import ru.redbyte.exchangerate.BuildConfig

object NetworkSettingsFactory {

    fun create(): NetworkSettings = object : NetworkSettings {
        @Suppress("ConstantConditionIf")
        override val baseUrl: String = "https://api.exchangeratesapi.io/"

        override val readTimeout: Long = 30
        override val connectionTimeout: Long = 30

        override val logRequests: Boolean = BuildConfig.DEBUG
    }

}