package ru.redbyte.exchangerate.presentation.main

import ru.redbyte.exchangerate.base.BaseContract

interface MainContract {
    interface View : BaseContract.View {
        fun showError(message: String?)
    }

    interface Presenter : BaseContract.Presenter {

    }
}