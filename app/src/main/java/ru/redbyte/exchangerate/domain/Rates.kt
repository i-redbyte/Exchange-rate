package ru.redbyte.exchangerate.domain

import java.math.BigDecimal

class Rates(
        val eur: BigDecimal,
        val gbp: BigDecimal,
        val usd: BigDecimal,
        val rub: BigDecimal
)