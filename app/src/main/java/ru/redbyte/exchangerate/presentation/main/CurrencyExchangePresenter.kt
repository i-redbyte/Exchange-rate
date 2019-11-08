package ru.redbyte.exchangerate.presentation.main

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redbyte.exchangerate.base.BasePresenter
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.domain.UseCase.None
import ru.redbyte.exchangerate.domain.balance.GetBalance
import ru.redbyte.exchangerate.domain.balance.Param
import ru.redbyte.exchangerate.domain.balance.SaveBalance
import ru.redbyte.exchangerate.domain.exchange.GetAllRates
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView
import ru.redbyte.exchangerate.presentation.model.asView
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyExchangePresenter @Inject constructor(
        private val getAllRates: GetAllRates,
        private val getBalance: GetBalance,
        private val saveBalance: SaveBalance

) : BasePresenter<CurrencyExchangeContract.View>(), CurrencyExchangeContract.Presenter {

    override var balance: MutableMap<Currency, BigDecimal> = mutableMapOf()

    override fun start() {
        getBalance()
        disposables += Observable.interval(0, REQUEST_PERIOD, TimeUnit.SECONDS)
                .flatMap { getAllRates.execute(None).toObservable() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.map { exchangeRate -> exchangeRate.asView() } }
                .subscribe(view::showBaseExchangeRate) { view.showError(it.message) }
    }

    override fun saveBalance(balance: Map<Currency, BigDecimal>) {
        disposables += saveBalance.execute(Param(balance))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { getBalance() }
                .subscribe({}) { view.showError(it.message) }
    }

    private fun getRate(base: Currency, exchangeRate: ExchangeRateView): BigDecimal =
            when (base) {
                USD -> exchangeRate.rates.usd
                GBP -> exchangeRate.rates.gbp
                EUR -> exchangeRate.rates.eur
                RUB -> exchangeRate.rates.rub
            }

    override fun calculateBalance(
            selectBase: Currency,
            targetBase: Currency,
            amountRate: BigDecimal,
            exchangeRate: ExchangeRateView
    ) {
        val result = amountRate * getRate(targetBase, exchangeRate)
        Log.d("_debug","$result")
        if (balance[targetBase]!! >= result) {
            balance[selectBase] = balance[selectBase]!! - amountRate
            balance[targetBase] = balance[targetBase]!! + result
            saveBalance(balance.toMap())
            view.updateBalance()
        } else {
            view.showError("Error! Not enough funds in your account.")
        }
    }

    private fun getBalance() {
        disposables += getBalance.execute(None)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { balance = it.toMutableMap() }
                .subscribe(view::showBalance) { view.showError(it.message) }
    }

    companion object {
        private const val REQUEST_PERIOD = 30L
    }
}