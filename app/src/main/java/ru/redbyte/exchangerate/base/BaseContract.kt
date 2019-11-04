package ru.redbyte.exchangerate.base


interface BaseContract {

    interface View

    interface Presenter {
        fun start()
        fun stop()
    }

}
