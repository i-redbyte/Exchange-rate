package ru.redbyte.exchangerate.domain.exchange

import io.reactivex.Single
import ru.redbyte.exchangerate.domain.ExchangeRate

interface ExchangeDataSource {
    fun getRates(): Single<List<ExchangeRate>>
}