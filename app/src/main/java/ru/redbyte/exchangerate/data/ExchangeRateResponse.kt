package ru.redbyte.exchangerate.data

import com.google.gson.annotations.SerializedName
import ru.redbyte.exchangerate.domain.ExchangeRate

class ExchangeRateResponse(
        @SerializedName("base")
        val base: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("rates")
        val rates: RatesResponse
)

fun ExchangeRateResponse.toExchangeRate() = ExchangeRate(
        base = base,
        date = date,
        rates = rates.toRates()
)