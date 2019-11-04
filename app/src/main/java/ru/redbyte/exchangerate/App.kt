package ru.redbyte.exchangerate

import androidx.appcompat.app.AppCompatDelegate
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import ru.redbyte.exchangerate.di.AppComponent
import ru.redbyte.exchangerate.di.AppModule
import ru.redbyte.exchangerate.di.DaggerAppComponent

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext, this))
            .build()
        return appComponent
    }

    companion object {
        lateinit var appComponent: AppComponent
    }

}