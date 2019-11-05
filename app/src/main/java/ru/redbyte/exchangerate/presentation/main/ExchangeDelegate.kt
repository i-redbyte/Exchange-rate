package ru.redbyte.exchangerate.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.extension.format
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView

class ExchangeDelegate(
    context: Context,
    private val listener: ExchangeListener
) : AbsListItemAdapterDelegate<ExchangeRateView, Any, ExchangeDelegate.Holder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var selectExchangeRate: ExchangeRateView? = null

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
        item is ExchangeRateView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_exchange, parent, false)

        return Holder(view, selectExchangeRate).apply {
            etAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) = Unit

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isNotEmpty()) {
                        listener.onChangeAmount(s.toString())
                    }
                }
            })
        }
    }

    override fun onBindViewHolder(item: ExchangeRateView, holder: Holder, payloads: List<Any>) =
        holder.bind(item)

    class Holder(
        itemView: View,
        private val selectExchangeRate: ExchangeRateView?
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvBase: TextView = requireViewById(itemView, R.id.tvBase)
        private val tvRate: TextView = requireViewById(itemView, R.id.tvRate)
        private val tvYouHave: TextView = requireViewById(itemView, R.id.tvYouHave)
        val etAmount: EditText = requireViewById(itemView, R.id.etAmount)

        @SuppressLint("SetTextI18n")
        fun bind(item: ExchangeRateView) {
            val symbol = getCurrencySymbol(item.base, itemView.context)
            val symbolTarget = getCurrencySymbol(selectExchangeRate?.base?:"", itemView.context)

            tvBase.text = item.base
            tvRate.text = "${symbol}1 = $symbolTarget${getRate(item.base, selectExchangeRate)}"
        }

        private fun getCurrencySymbol(base: String, context: Context): String =
            when (base) {
                USD.name -> context.getString(R.string.usd)
                GBP.name -> context.getString(R.string.gbp)
                EUR.name -> context.getString(R.string.eur)
                else -> ""
            }

        private fun getRate(base: String, exchangeRate: ExchangeRateView?): String =
            when (base) {
                USD.name -> exchangeRate?.rates?.usd?.format(2) ?: ""
                GBP.name -> exchangeRate?.rates?.gbp?.format(2) ?: ""
                EUR.name -> exchangeRate?.rates?.eur?.format(2) ?: ""
                else -> ""
            }
    }
}

interface ExchangeListener {
    fun onChangeAmount(amount: String)
}