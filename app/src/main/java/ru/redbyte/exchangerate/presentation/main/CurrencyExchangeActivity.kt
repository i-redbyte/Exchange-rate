package ru.redbyte.exchangerate.presentation.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.BaseActivity
import ru.redbyte.exchangerate.base.DelegationAdapter
import ru.redbyte.exchangerate.base.DelegationAdapter.*
import ru.redbyte.exchangerate.base.DelegationAdapter.Payload.*
import ru.redbyte.exchangerate.base.extension.setActionBar
import ru.redbyte.exchangerate.data.exchange.Currency
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
        btnExchange.setOnClickListener {
            rvSource.clearFocus()
            rvReceiver.clearFocus()
        }
    }

    private fun setupRecyclerView() {
        receiverDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String) {

            }
        })

        sourceDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String) {

            }
        })
        adapterSource = DelegationAdapter()
        adapterReceiver = DelegationAdapter()
        //************Source RV************
        val pshSource = PagerSnapHelper()
        pshSource.attachToRecyclerView(rvSource)
        rvSource.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterSource.delegatesManager.addDelegate(sourceDelegate)
        rvSource.adapter = adapterSource
        addScrollListenerLogic(rvSource, adapterSource, receiverDelegate)

        //************Receiver RV************

        val pshReceiver = PagerSnapHelper()
        pshReceiver.attachToRecyclerView(rvReceiver)
        rvReceiver.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterReceiver.delegatesManager.addDelegate(receiverDelegate)
        rvReceiver.adapter = adapterReceiver
        addScrollListenerLogic(rvReceiver, adapterReceiver, sourceDelegate)
    }

    private fun addScrollListenerLogic(
        rv: RecyclerView,
        adapter: DelegationAdapter,
        delegate: ExchangeDelegate
    ) {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = getCurrentPosition(recyclerView)
                    val item = adapter.items[position] as ExchangeRateView
                    delegate.selectExchangeRate = item
                    // TODO: Red_byte 2019-11-06 release it
                    Log.d("_debug", "BASE: ${item.base}")
                    Log.d("_debug", "USD: ${item.rates.usd}")
                    Log.d("_debug", "EUR: ${item.rates.eur}")
                    Log.d("_debug", "GBP: ${item.rates.gbp}")
                    Log.d("_debug", "position: $position")
                }
            }
        })
    }

    private fun getCurrentPosition(rv: RecyclerView): Int =
        (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

    override fun showBaseExchangeRate(list: List<ExchangeRateView>) {
        adapterSource.items = list
        adapterReceiver.items = list
        sourceDelegate.selectExchangeRate = list.firstOrNull()
        receiverDelegate.selectExchangeRate = list.firstOrNull()
    }

    override fun showBalance(balance: Map<Currency, Double>) {
        sourceDelegate.balance = balance
        receiverDelegate.balance = balance
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupActionBar() = setActionBar(findViewById(R.id.tActionBar)) {
        setTitle(R.string.app_name)
        setDisplayHomeAsUpEnabled(false)
    }
}