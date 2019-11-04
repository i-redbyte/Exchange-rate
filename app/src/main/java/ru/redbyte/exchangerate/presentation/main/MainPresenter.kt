package ru.redbyte.exchangerate.presentation.main

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redbyte.exchangerate.base.BasePresenter
import ru.redbyte.exchangerate.domain.UseCase.None
import ru.redbyte.exchangerate.domain.exchange.GetAllRates
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val getAllRates: GetAllRates

) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun start() {
        disposables += getAllRates.execute(None)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}) {
                Log.d("_debug", "error:${it.message}")
                view.showError(it.message)
            }
    }
}