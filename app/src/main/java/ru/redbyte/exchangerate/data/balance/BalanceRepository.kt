package ru.redbyte.exchangerate.data.balance

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ru.redbyte.exchangerate.BuildConfig.APPLICATION_ID
import ru.redbyte.exchangerate.base.extension.commit
import ru.redbyte.exchangerate.base.extension.typeTokenOf
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.domain.balance.BalanceDataSource
import java.lang.reflect.Type
import java.math.BigDecimal
import javax.inject.Inject

class BalanceRepository @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : BalanceDataSource {
    private val balanceTypeToken: Type by lazy { typeTokenOf<HashMap<Currency, BigDecimal>>() }

    override fun getBalance(): Single<Map<Currency, BigDecimal>> = Single.fromCallable {
        val balance = sharedPreferences.getString(BALANCE, "") ?: ""
        return@fromCallable gson.fromJson(balance, balanceTypeToken)
                ?: mapOf(USD to BigDecimal(100.0), EUR to BigDecimal(100.0), GBP to BigDecimal(100.0), RUB to BigDecimal(100.0))
    }

    override fun saveBalance(balance: Map<Currency, BigDecimal>): Completable = Completable.fromAction {
        sharedPreferences.commit {
            putString(BALANCE, gson.toJson(balance))
        }
    }

    companion object {
        private const val BALANCE = "$APPLICATION_ID.BALANCE"
    }
}
