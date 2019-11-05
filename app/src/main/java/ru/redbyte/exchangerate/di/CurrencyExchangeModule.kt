package ru.redbyte.exchangerate.di

import dagger.Binds
import dagger.Module
import ru.redbyte.exchangerate.di.common.PerScreen
import ru.redbyte.exchangerate.presentation.main.CurrencyExchangeActivity
import ru.redbyte.exchangerate.presentation.main.CurrencyExchangeContract
import ru.redbyte.exchangerate.presentation.main.CurrencyExchangePresenter

@Module
interface CurrencyExchangeModule {

    @PerScreen
    @Binds
    fun view(view: CurrencyExchangeActivity): CurrencyExchangeContract.View

    @PerScreen
    @Binds
    fun presenter(presenter: CurrencyExchangePresenter): CurrencyExchangeContract.Presenter
}