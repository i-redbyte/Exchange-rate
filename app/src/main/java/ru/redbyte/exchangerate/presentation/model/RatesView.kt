package ru.redbyte.exchangerate.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.redbyte.exchangerate.domain.Rates

@Parcelize
class RatesView(
        val eur: Double,
        val gbp: Double,
        val usd: Double,
        val rub: Double
) : Parcelable

fun Rates.asView() = RatesView(
        eur = eur,
        gbp = gbp,
        usd = usd,
        rub = rub
)