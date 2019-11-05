package ru.redbyte.exchangerate.domain.balance

import io.reactivex.Completable
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.domain.CompletableUseCase
import javax.inject.Inject

class SaveBalance @Inject constructor(
    private val balanceDataSource: BalanceDataSource
) : CompletableUseCase<Param>() {
    override fun execute(params: Param): Completable = balanceDataSource.saveBalance(params.balance)
}

class Param(val balance: Map<Currency, Double>)