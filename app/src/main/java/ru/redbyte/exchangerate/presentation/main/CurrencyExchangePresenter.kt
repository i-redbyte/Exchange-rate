package ru.redbyte.exchangerate.presentation.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redbyte.exchangerate.base.BasePresenter
import ru.redbyte.exchangerate.domain.UseCase.None
import ru.redbyte.exchangerate.domain.balance.GetBalance
import ru.redbyte.exchangerate.domain.balance.SaveBalance
import ru.redbyte.exchangerate.domain.exchange.GetAllRates
import ru.redbyte.exchangerate.presentation.model.asView
import javax.inject.Inject

class CurrencyExchangePresenter @Inject constructor(
    private val getAllRates: GetAllRates,
    private val getBalance: GetBalance,
    private val saveBalance: SaveBalance

) : BasePresenter<CurrencyExchangeContract.View>(), CurrencyExchangeContract.Presenter {

    override fun start() {
        disposables += getAllRates.execute(None)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.map { exchangeRate -> exchangeRate.asView() } }
            .flatMap { rates ->
                getBalance.execute(None)
                    .map { rates to it }
            }
            .subscribe({
                val rates = it.first
                val balance = it.second
                view.showBaseExchangeRate(rates)
                view.showBalance(balance)
            }) { view.showError(it.message) }
    }
}