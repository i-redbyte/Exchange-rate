package ru.redbyte.exchangerate.presentation.main

import android.os.Bundle
import android.widget.Toast
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.BaseActivity

class MainActivity : BaseActivity<MainContract.Presenter>(), MainContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.start()
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
