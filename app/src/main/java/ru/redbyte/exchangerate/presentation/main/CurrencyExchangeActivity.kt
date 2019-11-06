package ru.redbyte.exchangerate.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.BaseActivity
import ru.redbyte.exchangerate.base.DelegationAdapter
import ru.redbyte.exchangerate.base.DelegationAdapter.Payload
import ru.redbyte.exchangerate.base.extension.attachSnapHelperWithListener
import ru.redbyte.exchangerate.base.extension.setActionBar
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.presentation.main.SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
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

    override fun showBaseExchangeRate(list: List<ExchangeRateView>) {
        val sList = adapterSource.items.map { it as ExchangeRateView }.toMutableList()
        val rList = adapterReceiver.items.map { it as ExchangeRateView }.toMutableList()

        if (sList.isNullOrEmpty().not() && rList.isNullOrEmpty().not()){
            for ((index,value) in list.withIndex()){
                val sTmp = sList[index].selectExchangeRate
                val rTmp = rList[index].selectExchangeRate
                sList[index] = value
                sList[index].selectExchangeRate = sTmp
                rList[index] = value
                rList[index].selectExchangeRate = rTmp
                adapterSource.items = sList
                adapterReceiver.items = rList
            }
        }else {
            list.first().selectExchangeRate = list.first()
            adapterSource.items = list
            adapterReceiver.items = list
        }
    }

    private fun setupRecyclerView() {
        receiverDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String) {
                // TODO: Red_byte 2019-11-06 release it
            }
        })

        sourceDelegate = ExchangeDelegate(this, object : ExchangeListener {
            override fun onChangeAmount(amount: String) {
                // TODO: Red_byte 2019-11-06 release it
            }
        })
        adapterSource = DelegationAdapter()
        adapterReceiver = DelegationAdapter()
        //************Source RV************

        rvSource.attachSnapHelperWithListener(PagerSnapHelper(), NOTIFY_ON_SCROLL_STATE_IDLE, object : OnChangeSnapPositionListener {
            override fun onSnapPositionChange(position: Int) {
                val item = adapterSource.items[position] as ExchangeRateView
                val list = adapterReceiver.items.map { it as ExchangeRateView }.toMutableList()
                val mainList = adapterSource.items.map { it as ExchangeRateView }.toMutableList()
                val targetItem = list[getCurrentPosition(rvReceiver)]
                mainList[position].selectExchangeRate = targetItem

                targetItem.selectExchangeRate = item
                list[getCurrentPosition(rvReceiver)] = targetItem
                adapterReceiver.setItems(list, Payload.UPDATE)
                adapterSource.setItems(mainList, Payload.UPDATE)
            }
        })
        rvSource.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterSource.delegatesManager.addDelegate(sourceDelegate)
        rvSource.adapter = adapterSource

        //************Receiver RV************

        rvReceiver.attachSnapHelperWithListener(PagerSnapHelper(), NOTIFY_ON_SCROLL_STATE_IDLE, object : OnChangeSnapPositionListener {
            override fun onSnapPositionChange(position: Int) {
                val item = adapterReceiver.items[position] as ExchangeRateView
                val list = adapterSource.items.map { it as ExchangeRateView }.toMutableList()
                val mainList = adapterReceiver.items.map { it as ExchangeRateView }.toMutableList()
                val targetItem = list[getCurrentPosition(rvSource)]
                mainList[position].selectExchangeRate = targetItem

                targetItem.selectExchangeRate = item
                list[getCurrentPosition(rvSource)] = targetItem
                adapterSource.setItems(list, Payload.UPDATE)
                adapterReceiver.setItems(mainList, Payload.UPDATE)

            }
        })

        rvReceiver.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterReceiver.delegatesManager.addDelegate(receiverDelegate)
        rvReceiver.adapter = adapterReceiver
    }

    private fun getCurrentPosition(rv: RecyclerView): Int =
            (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

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