package ru.redbyte.exchangerate.di.data

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import ru.redbyte.exchangerate.data.balance.BalanceRepository
import ru.redbyte.exchangerate.data.common.ExchangeApi
import ru.redbyte.exchangerate.data.exchange.ExchangeRepository
import ru.redbyte.exchangerate.domain.balance.BalanceDataSource
import ru.redbyte.exchangerate.domain.exchange.ExchangeDataSource
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideExchangeDataSource(
        exchangeApi: ExchangeApi,
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): ExchangeDataSource = ExchangeRepository(
        exchangeApi = exchangeApi,
        sharedPreferences = sharedPreferences,
        gson = gson
    )

    @Provides
    @Singleton
    fun provideBalanceDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): BalanceDataSource = BalanceRepository(
        sharedPreferences = sharedPreferences,
        gson = gson
    )

}