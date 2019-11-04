package ru.redbyte.exchangerate.domain

class ExchangeRate(
        val base: String,
        val date: String,
        val rates: Rates
)