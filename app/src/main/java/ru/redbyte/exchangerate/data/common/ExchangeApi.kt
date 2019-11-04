package ru.redbyte.exchangerate.data.common

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.redbyte.exchangerate.data.ExchangeRateResponse

interface ExchangeApi {
    @GET("latest") //example: base_url/latest?base=USD&symbols=EUR,GBP
    fun getExchangeRate(
            @Query("base") base: String,
            @Query("symbols") symbols:String
    ): Single<ExchangeRateResponse>
}