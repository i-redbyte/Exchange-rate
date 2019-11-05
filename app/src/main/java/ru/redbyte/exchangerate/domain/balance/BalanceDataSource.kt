package ru.redbyte.exchangerate.domain.balance

import io.reactivex.Completable
import io.reactivex.Single
import ru.redbyte.exchangerate.data.exchange.Currency

interface BalanceDataSource {

    fun getBalance(): Single<Map<Currency, Double>>
    fun saveBalance(balance: Map<Currency, Double>): Completable
}