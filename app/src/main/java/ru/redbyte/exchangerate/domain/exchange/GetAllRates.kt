package ru.redbyte.exchangerate.domain.exchange

import io.reactivex.Single
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.domain.ExchangeRate
import ru.redbyte.exchangerate.domain.SingleUseCase
import ru.redbyte.exchangerate.domain.UseCase.None
import javax.inject.Inject

class GetAllRates @Inject constructor(
        private val resultDataSource: ExchangeDataSource
) : SingleUseCase<List<ExchangeRate>, None>() {

    override fun execute(params: None): Single<List<ExchangeRate>> {
        val usd = resultDataSource.getRate(USD, "$EUR,$GBP")
        val eur = resultDataSource.getRate(EUR, "$USD,$GBP")
        val gbp = resultDataSource.getRate(GBP, "$USD,$EUR")
        return Single
                .merge(usd, eur, gbp)
                .toList()
    }
}