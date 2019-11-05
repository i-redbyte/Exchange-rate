package ru.redbyte.exchangerate.domain.balance

import io.reactivex.Single
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.domain.SingleUseCase
import ru.redbyte.exchangerate.domain.UseCase.None
import javax.inject.Inject

class GetBalance @Inject constructor(
    private val balanceDataSource: BalanceDataSource
) : SingleUseCase<Map<Currency, Double>, None>() {

    override fun execute(params: None): Single<Map<Currency, Double>> =
        balanceDataSource.getBalance()
}