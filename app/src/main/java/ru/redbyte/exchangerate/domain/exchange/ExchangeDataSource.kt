package ru.redbyte.exchangerate.domain.exchange

import io.reactivex.Single
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.domain.ExchangeRate

interface ExchangeDataSource {
    fun getRate(
        base: Currency,
        symbols: String
    ): Single<ExchangeRate>
}