package ru.redbyte.exchangerate.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.domain.ExchangeRate

@Parcelize
class ExchangeRateView(
        val base: Currency,
        val date: String,
        val rates: RatesView
) : Parcelable

fun ExchangeRate.asView() = ExchangeRateView(
        base = Currency.valueOf(base),
        date = date,
        rates = rates.asView()
)