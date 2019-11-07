package ru.redbyte.exchangerate.data.exchange

import com.google.gson.annotations.SerializedName
import ru.redbyte.exchangerate.domain.Rates

class RatesResponse(
        @SerializedName("EUR")
        val eur: Double,
        @SerializedName("GBP")
        val gbp: Double,
        @SerializedName("USD")
        val usd: Double,
        @SerializedName("RUB")
        val rub: Double
)

fun RatesResponse.toRates() = Rates(
        eur = eur.toBigDecimal(),
        gbp = gbp.toBigDecimal(),
        usd = usd.toBigDecimal(),
        rub = rub.toBigDecimal()
)