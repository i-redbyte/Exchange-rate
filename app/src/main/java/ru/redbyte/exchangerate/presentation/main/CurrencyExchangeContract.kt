package ru.redbyte.exchangerate.presentation.main

import ru.redbyte.exchangerate.base.BaseContract
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView
import java.math.BigDecimal

interface CurrencyExchangeContract {

    interface View : BaseContract.View {
        fun updateBalance()
        fun showBaseExchangeRate(list: List<ExchangeRateView>)
        fun showBalance(balance: Map<Currency, BigDecimal>)
        fun showOkChangeBalance()
        fun showError(message: String?)
    }

    interface Presenter : BaseContract.Presenter {
        var balance: MutableMap<Currency, BigDecimal>
        fun saveBalance(balance: Map<Currency, BigDecimal>)
        fun calculateBalance(
                selectBase: Currency,
                targetBase: Currency,
                amountRate: BigDecimal,
                exchangeRate:ExchangeRateView
        )
    }
}