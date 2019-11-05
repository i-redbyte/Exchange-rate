package ru.redbyte.exchangerate.data.exchange

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Single
import ru.redbyte.exchangerate.data.ExchangeRateResponse
import ru.redbyte.exchangerate.data.common.ExchangeApi
import ru.redbyte.exchangerate.data.toExchangeRate
import ru.redbyte.exchangerate.domain.ExchangeRate
import ru.redbyte.exchangerate.domain.exchange.ExchangeDataSource
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val exchangeApi: ExchangeApi,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : ExchangeDataSource {

    override fun getRate(base: Currency, symbols: String): Single<ExchangeRate> =
        exchangeApi.getExchangeRate(base, symbols)
            .map(ExchangeRateResponse::toExchangeRate)
}