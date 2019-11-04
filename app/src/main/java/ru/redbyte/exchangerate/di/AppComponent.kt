package ru.redbyte.exchangerate.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.redbyte.exchangerate.App
import ru.redbyte.exchangerate.di.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            AndroidSupportInjectionModule::class,
            ScreenBindingModule::class,
            NetworkModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {

}