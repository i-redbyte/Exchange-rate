package ru.redbyte.exchangerate.presentation.main

import ru.redbyte.exchangerate.base.BaseContract
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView

interface CurrencyExchangeContract {

    interface View : BaseContract.View {
        fun showError(message: String?)
        fun showBaseExchangeRate(list: List<ExchangeRateView>)
    }

    interface Presenter : BaseContract.Presenter {

    }
}