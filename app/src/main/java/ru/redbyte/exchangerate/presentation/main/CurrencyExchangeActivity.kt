package ru.redbyte.exchangerate.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.BaseActivity
import ru.redbyte.exchangerate.base.DelegationAdapter
import ru.redbyte.exchangerate.base.extension.setActionBar
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView

class CurrencyExchangeActivity : BaseActivity<CurrencyExchangeContract.Presenter>(),
    CurrencyExchangeContract.View {

    private lateinit var adapterSource: DelegationAdapter
    private lateinit var adapterReceiver: DelegationAdapter
    private lateinit var sourceDelegate: ExchangeDelegate
    private lateinit var receiverDelegate: ExchangeDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_exchange)
        setupView()
        presenter.start()
    }

    private fun setupView() {
        setupActionBar()
        setupRecyclerView()
        btnExchange.setOnClickListener { }
    }

    private fun setupRecyclerView() {
        //************Source RV************
        adapterSource = DelegationAdapter()
        adapterReceiver = DelegationAdapter()
        rvSource.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sourceDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String) {

            }
        })
        adapterSource.delegatesManager.addDelegate(sourceDelegate)
        rvSource.adapter = adapterSource
        //************Receiver RV************
        rvReceiver.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        receiverDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String) {

            }
        })
        adapterReceiver.delegatesManager.addDelegate(receiverDelegate)
        rvReceiver.adapter = adapterReceiver
    }

    override fun showBaseExchangeRate(list: List<ExchangeRateView>) {
        adapterSource.items = list
        adapterReceiver.items = list
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupActionBar() = setActionBar(findViewById(R.id.tActionBar)) {
        setTitle(R.string.app_name)
        setDisplayHomeAsUpEnabled(false)
    }
}