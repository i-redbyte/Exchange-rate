package ru.redbyte.exchangerate.presentation.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redbyte.exchangerate.base.BasePresenter
import ru.redbyte.exchangerate.domain.UseCase.None
import ru.redbyte.exchangerate.domain.exchange.GetAllRates
import ru.redbyte.exchangerate.presentation.model.asView
import javax.inject.Inject

class CurrencyExchangePresenter @Inject constructor(
    private val getAllRates: GetAllRates

) : BasePresenter<CurrencyExchangeContract.View>(), CurrencyExchangeContract.Presenter {

    override fun start() {
        disposables += getAllRates.execute(None)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.map { exchangeRate -> exchangeRate.asView() } }
            .subscribe({
                view.showBaseExchangeRate(it)
            }) { view.showError(it.message) }
    }
}