package ru.redbyte.exchangerate.di

import dagger.Binds
import dagger.Module
import ru.redbyte.exchangerate.di.common.PerScreen
import ru.redbyte.exchangerate.presentation.main.MainActivity
import ru.redbyte.exchangerate.presentation.main.MainContract
import ru.redbyte.exchangerate.presentation.main.MainPresenter

@Module
interface MainModule {

    @PerScreen
    @Binds
    fun view(view: MainActivity): MainContract.View

    @PerScreen
    @Binds
    fun presenter(presenter: MainPresenter): MainContract.Presenter

}