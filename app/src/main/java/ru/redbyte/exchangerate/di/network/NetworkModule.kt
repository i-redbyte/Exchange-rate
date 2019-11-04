package ru.redbyte.exchangerate.di.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.redbyte.exchangerate.data.common.ExchangeApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkSettings: NetworkSettings
    ): OkHttpClient {
        val logInterceptor = if (networkSettings.logRequests)
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        else
            null
        return OkHttpClient.Builder()
            .apply { logInterceptor?.let { addNetworkInterceptor(it) } }
            .connectTimeout(networkSettings.connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(networkSettings.readTimeout, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideRetrofit(
        networkSettings: NetworkSettings,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(networkSettings.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkettings(): NetworkSettings = NetworkSettingsFactory.create()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): ExchangeApi = retrofit.create(ExchangeApi::class.java)

}