package ru.redbyte.exchangerate.presentation.main

import ru.redbyte.exchangerate.base.BaseContract
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView

interface CurrencyExchangeContract {

    interface View : BaseContract.View {
        fun showError(message: String?)
        fun showBaseExchangeRate(list: List<ExchangeRateView>)
        fun showBalance(balance: Map<Currency, Double>)
    }

    interface Presenter : BaseContract.Presenter {
        var balance: Map<Currency, Double>
        fun saveBalance(balance: Map<Currency, Double>)
        fun getRate(base: String, exchangeRate: ExchangeRateView): Double
    }
}