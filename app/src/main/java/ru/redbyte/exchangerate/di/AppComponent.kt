package ru.redbyte.exchangerate.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.redbyte.exchangerate.App
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            AndroidSupportInjectionModule::class,
            ScreenBindingModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {

}