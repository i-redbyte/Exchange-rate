package ru.redbyte.exchangerate.domain.exchange

import io.reactivex.Single
import ru.redbyte.exchangerate.domain.ExchangeRate
import ru.redbyte.exchangerate.domain.SingleUseCase
import ru.redbyte.exchangerate.domain.UseCase.None
import javax.inject.Inject

class GetAllRates @Inject constructor(
    private val resultDataSource: ExchangeDataSource
) : SingleUseCase<List<ExchangeRate>, None>() {

    override fun execute(params: None): Single<List<ExchangeRate>> =
        resultDataSource.getRates()
}