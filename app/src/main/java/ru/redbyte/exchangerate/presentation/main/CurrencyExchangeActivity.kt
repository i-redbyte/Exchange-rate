package ru.redbyte.exchangerate.presentation.main

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.BaseActivity
import ru.redbyte.exchangerate.base.DelegationAdapter
import ru.redbyte.exchangerate.base.DelegationAdapter.Payload
import ru.redbyte.exchangerate.base.extension.attachSnapHelperWithListener
import ru.redbyte.exchangerate.base.extension.format
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
    private var amountRate: BigDecimal = BigDecimal(0.0)
    private var selectRateResult: BigDecimal = BigDecimal(0.0)
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
        setupRecyclerView()
        btnExchange.setOnClickListener {
            rvSource.clearFocus()
            rvReceiver.clearFocus()
            calculateExchangeRate()
        }
    }

    private fun calculateExchangeRate() {
        if (presenter.balance[targetBase]!! >= selectRateResult) {
            presenter.calculateBalance(selectBase, targetBase, selectRateResult, amountRate)
            sourceDelegate.balance = presenter.balance
            receiverDelegate.balance = presenter.balance
        } else {
            showError(getString(R.string.error_balance))
        }
    }

    override fun showBaseExchangeRate(list: List<ExchangeRateView>) {
        adapterSource.items = list
        adapterReceiver.items = list
        if (sourceDelegate.selectExchangeRate == null && receiverDelegate.selectExchangeRate == null) {
            sourceDelegate.selectExchangeRate = list.firstOrNull()
            receiverDelegate.selectExchangeRate = list.firstOrNull()
        }
    }

    private fun setupRecyclerView() {
        adapterSource = DelegationAdapter()
        adapterReceiver = DelegationAdapter()

        receiverDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String, position: Int) {
                // TODO: Red_byte 2019-11-06 extract to method
                val item = adapterReceiver.items[position] as ExchangeRateView
                val etAmount = rvSource.layoutManager?.findViewByPosition(getCurrentPosition(rvSource))?.findViewById<EditText>(R.id.etAmount)
                val etCurrent = rvReceiver.layoutManager?.findViewByPosition(getCurrentPosition(rvReceiver))?.findViewById<EditText>(R.id.etAmount)
                val rate = presenter.getRate(receiverDelegate.selectExchangeRate?.base ?: "", item)
                val result = amount.toBigDecimal() * rate
                if (getCurrentPosition(rvSource) != -1) {
                    val target = adapterSource.items[getCurrentPosition(rvSource)] as ExchangeRateView
                    amountRate = amount.toBigDecimal()
                    selectBase = Currency.valueOf(item.base)
                    targetBase = Currency.valueOf(target.base)
                    selectRateResult = result
                }
                if (currentFocus == etCurrent)
                    etAmount?.setText(result.format(2))
            }

            override fun updateRateResult(amount: String, position: Int) {
                val etAmount = rvSource.layoutManager?.findViewByPosition(getCurrentPosition(rvSource))?.findViewById<EditText>(R.id.etAmount)
                val item = adapterSource.items[position] as ExchangeRateView
                val rate = presenter.getRate(receiverDelegate.selectExchangeRate?.base ?: "", item)
                val result = amount.toBigDecimal() * rate
                etAmount?.setText(result.format(2))
            }
        }, adapterReceiver)

        sourceDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String, position: Int) {
                // TODO: Red_byte 2019-11-06 extract to method
                val etCurrent = rvSource.layoutManager?.findViewByPosition(getCurrentPosition(rvSource))?.findViewById<EditText>(R.id.etAmount)
                val item = adapterSource.items[position] as ExchangeRateView
                val etAmount = rvReceiver.layoutManager?.findViewByPosition(getCurrentPosition(rvReceiver))?.findViewById<EditText>(R.id.etAmount)
                val rate = presenter.getRate(sourceDelegate.selectExchangeRate?.base ?: "", item)
                val result = amount.toBigDecimal() * rate
                if (getCurrentPosition(rvReceiver) != -1) {
                    val target = adapterReceiver.items[getCurrentPosition(rvReceiver)] as ExchangeRateView
                    amountRate = amount.toBigDecimal()
                    selectBase = Currency.valueOf(item.base)
                    targetBase = Currency.valueOf(target.base)
                    selectRateResult = result
                }
                if (currentFocus == etCurrent)
                    etAmount?.setText(result.format(2))
            }

            override fun updateRateResult(amount: String, position: Int) {
                val etAmount = rvReceiver.layoutManager?.findViewByPosition(getCurrentPosition(rvReceiver))?.findViewById<EditText>(R.id.etAmount)
                val item = adapterSource.items[position] as ExchangeRateView
                val rate = presenter.getRate(sourceDelegate.selectExchangeRate?.base ?: "", item)
                val result = amount.toBigDecimal() * rate
                etAmount?.setText(result.format(2))
            }
        }, adapterSource)
        //************Source RV************

        rvSource.attachSnapHelperWithListener(PagerSnapHelper(), NOTIFY_ON_SCROLL_STATE_IDLE, object : OnChangeSnapPositionListener {
            override fun onSnapPositionChange(position: Int) {
                val item = adapterSource.items[position] as ExchangeRateView
                val targetItem = adapterReceiver.items[getCurrentPosition(rvReceiver)] as ExchangeRateView
                receiverDelegate.selectExchangeRate = item
                sourceDelegate.selectExchangeRate = targetItem
            }
        })
        rvSource.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterSource.delegatesManager.addDelegate(sourceDelegate)
        rvSource.adapter = adapterSource

        //************Receiver RV************

        rvReceiver.attachSnapHelperWithListener(PagerSnapHelper(), NOTIFY_ON_SCROLL_STATE_IDLE, object : OnChangeSnapPositionListener {
            override fun onSnapPositionChange(position: Int) {
                val item = adapterReceiver.items[position] as ExchangeRateView
                val targetItem = adapterSource.items[getCurrentPosition(rvSource)]as ExchangeRateView
                receiverDelegate.selectExchangeRate = targetItem
                sourceDelegate.selectExchangeRate = item
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
        Snackbar.make(btnExchange, "SUCCESS CHANGE BALANCE!", Snackbar.LENGTH_LONG).show()
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupActionBar() = setActionBar(findViewById(R.id.tActionBar)) {
        setTitle(R.string.app_name)
        setDisplayHomeAsUpEnabled(false)
    }
}