package ru.redbyte.exchangerate.domain.balance

import io.reactivex.Completable
import io.reactivex.Single
import ru.redbyte.exchangerate.data.exchange.Currency
import java.math.BigDecimal

interface BalanceDataSource {

    fun getBalance(): Single<Map<Currency, BigDecimal>>
    fun saveBalance(balance: Map<Currency, BigDecimal>): Completable
}