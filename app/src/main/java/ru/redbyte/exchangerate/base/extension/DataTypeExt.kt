package ru.redbyte.exchangerate.base.extension

import java.util.*

fun Double.format(digits: Int) = "%.${digits}f".format(Locale.ENGLISH, this)

fun Float.format(digits: Int) = "%.${digits}f".format(Locale.ENGLISH, this)