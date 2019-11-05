package ru.redbyte.exchangerate.data.exchange

import com.google.gson.annotations.SerializedName
import ru.redbyte.exchangerate.domain.Rates

class RatesResponse(
        @SerializedName("EUR")
        val eur: Double,
        @SerializedName("GBP")
        val gbp: Double,
        @SerializedName("USD")
        val usd: Double
)

fun RatesResponse.toRates() = Rates(
        eur = eur,
        gbp = gbp,
        usd = usd
)