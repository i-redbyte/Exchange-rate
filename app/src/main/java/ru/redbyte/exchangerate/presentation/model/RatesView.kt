package ru.redbyte.exchangerate.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.redbyte.exchangerate.domain.Rates
import java.math.BigDecimal

@Parcelize
class RatesView(
        val eur: BigDecimal,
        val gbp: BigDecimal,
        val usd: BigDecimal,
        val rub: BigDecimal
) : Parcelable

fun Rates.asView() = RatesView(
        eur = eur,
        gbp = gbp,
        usd = usd,
        rub = rub
)