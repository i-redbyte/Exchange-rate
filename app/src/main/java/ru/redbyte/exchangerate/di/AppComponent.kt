package ru.redbyte.exchangerate.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.redbyte.exchangerate.App
import ru.redbyte.exchangerate.di.data.DataSourceModule
import ru.redbyte.exchangerate.di.data.LocalDataModule
import ru.redbyte.exchangerate.di.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class,
        NetworkModule::class,
        LocalDataModule::class,
        DataSourceModule::class,
        ScreenBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

}