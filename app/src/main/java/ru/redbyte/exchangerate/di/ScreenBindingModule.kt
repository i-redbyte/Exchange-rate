package ru.redbyte.exchangerate.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.redbyte.exchangerate.di.common.PerScreen
import ru.redbyte.exchangerate.presentation.main.MainActivity

@Module
interface ScreenBindingModule {

    @PerScreen
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun bindMain(): MainActivity
}