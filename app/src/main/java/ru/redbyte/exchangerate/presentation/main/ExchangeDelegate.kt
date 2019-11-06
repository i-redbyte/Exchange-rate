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
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView

class ExchangeDelegate(
        context: Context,
        private val listener: ExchangeListener
) : AbsListItemAdapterDelegate<ExchangeRateView, Any, ExchangeDelegate.Holder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var balance: Map<Currency, Double> = mapOf()

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is ExchangeRateView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_exchange, parent, false)

        return Holder(view, balance)
                .apply {
                    etAmount.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus.not() && etAmount.text.isEmpty()) etAmount.setText(itemView.context.getString(R.string.zero))
                    }
                    etAmount.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable) = Unit

                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                            if (s.isNotEmpty()) {
                                listener.onChangeAmount(s.toString(), adapterPosition)
                            }
                        }
                    })
                }
    }

    override fun onBindViewHolder(item: ExchangeRateView, holder: Holder, payloads: List<Any>) =
            holder.bind(item)

    class Holder(
            itemView: View,
            private val balance: Map<Currency, Double>
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvBase: TextView = requireViewById(itemView, R.id.tvBase)
        private val tvRate: TextView = requireViewById(itemView, R.id.tvRate)
        private val tvYouHave: TextView = requireViewById(itemView, R.id.tvYouHave)
        val etAmount: EditText = requireViewById(itemView, R.id.etAmount)

        @SuppressLint("SetTextI18n")
        fun bind(item: ExchangeRateView) {
            val symbol = getCurrencySymbol(item.base, itemView.context)
            val symbolTarget = getCurrencySymbol(item.selectExchangeRate?.base ?: "", itemView.context)

            tvBase.text = item.base
            tvRate.text = "${symbol}1 = $symbolTarget${getRate(item.selectExchangeRate?.base ?: "", item)}"
            val youHaveString = "${balance[valueOf(item.base)]}$symbol"
            tvYouHave.text = itemView.context.getString(R.string.currency_exchange_you_have, youHaveString)
        }

        private fun getCurrencySymbol(base: String, context: Context): String =
                when (base) {
                    USD.name -> context.getString(R.string.usd)
                    GBP.name -> context.getString(R.string.gbp)
                    EUR.name -> context.getString(R.string.eur)
                    RUB.name -> context.getString(R.string.rub)
                    else -> ""
                }

        private fun getRate(base: String, exchangeRate: ExchangeRateView?): String =
                when (base) {
                    USD.name -> exchangeRate?.rates?.usd?.format(DECIMAL_PLACES) ?: ""
                    GBP.name -> exchangeRate?.rates?.gbp?.format(DECIMAL_PLACES) ?: ""
                    EUR.name -> exchangeRate?.rates?.eur?.format(DECIMAL_PLACES) ?: ""
                    RUB.name -> exchangeRate?.rates?.rub?.format(DECIMAL_PLACES) ?: ""
                    else -> ""
                }

        companion object {
            private const val DECIMAL_PLACES = 3
        }
    }
}

interface ExchangeListener {
    fun onChangeAmount(amount: String, position: Int)
}