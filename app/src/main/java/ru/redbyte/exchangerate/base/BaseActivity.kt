package ru.redbyte.exchangerate.base

import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<P : BaseContract.Presenter> :
    DaggerAppCompatActivity(),
    BaseContract.View {

    @Inject
    lateinit var presenter: P

    override fun onDestroy() {
        super.onDestroy()
        presenter.stop()
    }

}

