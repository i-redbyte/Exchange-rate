package ru.redbyte.exchangerate.presentation.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redbyte.exchangerate.base.BasePresenter
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.domain.UseCase.None
import ru.redbyte.exchangerate.domain.balance.GetBalance
import ru.redbyte.exchangerate.domain.balance.Param
import ru.redbyte.exchangerate.domain.balance.SaveBalance
import ru.redbyte.exchangerate.domain.exchange.GetAllRates
import ru.redbyte.exchangerate.presentation.model.asView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyExchangePresenter @Inject constructor(
        private val getAllRates: GetAllRates,
        private val getBalance: GetBalance,
        private val saveBalance: SaveBalance

) : BasePresenter<CurrencyExchangeContract.View>(), CurrencyExchangeContract.Presenter {

    override fun start() {
        getBalance()
        disposables += Observable.interval(0, REQUEST_PERIOD, TimeUnit.SECONDS)
            .flatMap { getAllRates.execute(None).toObservable() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.map { exchangeRate -> exchangeRate.asView() } }
                .subscribe(view::showBaseExchangeRate) { view.showError(it.message) }
    }

    override fun saveBalance(balance: Map<Currency, Double>) {
        disposables += saveBalance.execute(Param(balance))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { getBalance() }
                .subscribe({}) { view.showError(it.message) }
    }

    private fun getBalance() {
        disposables += getBalance.execute(None)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.showBalance(it) }) { view.showError(it.message) }
    }

    companion object {
        private const val REQUEST_PERIOD = 30L
    }
}