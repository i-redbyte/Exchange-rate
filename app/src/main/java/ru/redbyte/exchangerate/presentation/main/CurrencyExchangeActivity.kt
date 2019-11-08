package ru.redbyte.exchangerate.presentation.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.BaseActivity
import ru.redbyte.exchangerate.base.DelegationAdapter
import ru.redbyte.exchangerate.base.extension.attachSnapHelperWithListener
import ru.redbyte.exchangerate.base.extension.setActionBar
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.presentation.main.SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView
import java.math.BigDecimal

class CurrencyExchangeActivity : BaseActivity<CurrencyExchangeContract.Presenter>(),
        CurrencyExchangeContract.View {

    private lateinit var adapterSource: DelegationAdapter
    private lateinit var adapterReceiver: DelegationAdapter
    private lateinit var sourceDelegate: ExchangeDelegate
    private lateinit var receiverDelegate: ExchangeDelegate
    private var selectBase: Currency = Currency.USD
    private var targetBase: Currency = Currency.USD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_exchange)
        setupView()
        presenter.start()
    }

    private fun setupView() {
        setupActionBar()
        setupRecyclerViews()
        btnExchange.setOnClickListener { onExchangeRateClick() }
    }

    override fun updateBalance() {
        sourceDelegate.balance = presenter.balance
        receiverDelegate.balance = presenter.balance
        showOkChangeBalance()
    }

    override fun showBaseExchangeRate(list: List<ExchangeRateView>) {
        adapterSource.items = list
        adapterReceiver.items = list
        if (sourceDelegate.selectExchangeRate == null && receiverDelegate.selectExchangeRate == null) {
            sourceDelegate.selectExchangeRate = list.firstOrNull()
            receiverDelegate.selectExchangeRate = list.firstOrNull()
        }
    }

    private fun onExchangeRateClick() {
        rvSource.clearFocus()
        rvReceiver.clearFocus()
        val amount = etAmount.text.toString()
        val amountRate = if (amount.isEmpty()) {
            etAmount.setText(R.string.zero)
            BigDecimal(0.0)
        } else {
            amount.toBigDecimal()
        }
        val item = adapterSource.items[getCurrentPosition(rvSource)] as ExchangeRateView
        presenter.calculateBalance(selectBase, targetBase, amountRate, item)
    }

    private fun setupRecyclerViews() {
        adapterSource = DelegationAdapter()
        adapterReceiver = DelegationAdapter()
        receiverDelegate = ExchangeDelegate(this, adapterReceiver)
        sourceDelegate = ExchangeDelegate(this, adapterSource)

        //************Source RV************

        rvSource.attachSnapHelperWithListener(
                PagerSnapHelper(),
                NOTIFY_ON_SCROLL_STATE_IDLE,
                object : OnChangeSnapPositionListener {
                    override fun onSnapPositionChange(position: Int) {
                        val item = adapterSource.items[position] as ExchangeRateView
                        val targetItem =
                                adapterReceiver.items[getCurrentPosition(rvReceiver)] as ExchangeRateView

                        selectBase = item.base
                        receiverDelegate.selectExchangeRate = item
                        sourceDelegate.selectExchangeRate = targetItem
                    }
                })
        rvSource.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterSource.delegatesManager.addDelegate(sourceDelegate)
        rvSource.adapter = adapterSource

        //************Receiver RV************

        rvReceiver.attachSnapHelperWithListener(
                PagerSnapHelper(),
                NOTIFY_ON_SCROLL_STATE_IDLE,
                object : OnChangeSnapPositionListener {
                    override fun onSnapPositionChange(position: Int) {
                        val item = adapterReceiver.items[position] as ExchangeRateView
                        val targetItem =
                                adapterSource.items[getCurrentPosition(rvSource)] as ExchangeRateView
                        targetBase = item.base
                        sourceDelegate.selectExchangeRate = item
                        receiverDelegate.selectExchangeRate = targetItem
                    }
                })

        rvReceiver.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterReceiver.delegatesManager.addDelegate(receiverDelegate)
        rvReceiver.adapter = adapterReceiver
    }

    private fun getCurrentPosition(rv: RecyclerView): Int =
            (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

    override fun showBalance(balance: Map<Currency, BigDecimal>) {
        sourceDelegate.balance = balance
        receiverDelegate.balance = balance
    }

    override fun showOkChangeBalance() {
        Snackbar
                .make(btnExchange, getString(R.string.currency_exchange_success), Snackbar.LENGTH_LONG)
                .show()
    }

    override fun showError(message: String?) {
        Snackbar
                .make(btnExchange, message ?: "Error with empty body", Snackbar.LENGTH_LONG)
                .show()
    }

    private fun setupActionBar() = setActionBar(findViewById(R.id.tActionBar)) {
        setTitle(R.string.app_name)
        setDisplayHomeAsUpEnabled(false)
    }
}