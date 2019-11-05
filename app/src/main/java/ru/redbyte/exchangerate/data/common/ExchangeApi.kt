package ru.redbyte.exchangerate.data.common

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.redbyte.exchangerate.data.exchange.ExchangeRateResponse
import ru.redbyte.exchangerate.data.exchange.Currency

interface ExchangeApi {
    @GET("latest/") //example: base_url/latest?base=USD&symbols=EUR,GBP
    fun getExchangeRate(
        @Query("base") base: Currency,
        @Query("symbols") symbols: String
    ): Single<ExchangeRateResponse>
}