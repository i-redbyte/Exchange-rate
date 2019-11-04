package ru.redbyte.exchangerate.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.redbyte.exchangerate.domain.ExchangeRate

@Parcelize
class ExchangeRateView(
    val base: String,
    val date: String,
    val rates: RatesView
):Parcelable

fun ExchangeRate.asView() = ExchangeRateView(
    base = base,
    date = date,
    rates = rates.asView()
)