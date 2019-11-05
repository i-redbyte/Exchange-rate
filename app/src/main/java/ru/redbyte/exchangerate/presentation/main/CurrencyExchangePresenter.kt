package ru.redbyte.exchangerate.presentation.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redbyte.exchangerate.base.BasePresenter
import ru.redbyte.exchangerate.domain.UseCase.None
import ru.redbyte.exchangerate.domain.exchange.GetAllRates
import javax.inject.Inject

class CurrencyExchangePresenter @Inject constructor(
        private val getAllRates: GetAllRates

) : BasePresenter<CurrencyExchangeContract.View>(), CurrencyExchangeContract.Presenter {

    override fun start() {
        disposables += getAllRates.execute(None)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }) { view.showError(it.message) }
    }
}