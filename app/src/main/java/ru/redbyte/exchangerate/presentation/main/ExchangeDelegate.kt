package ru.redbyte.exchangerate.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.redbyte.exchangerate.R
import ru.redbyte.exchangerate.base.DelegationAdapter
import ru.redbyte.exchangerate.base.extension.format
import ru.redbyte.exchangerate.data.exchange.Currency
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.presentation.model.ExchangeRateView
import java.math.BigDecimal

class ExchangeDelegate(
        context: Context,
        private val adapter: DelegationAdapter
) : AbsListItemAdapterDelegate<ExchangeRateView, Any, ExchangeDelegate.Holder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var balance: Map<Currency, BigDecimal> = mapOf()
        set(value) {
            adapter.notifyDataSetChanged()
            field = value
        }
    var selectExchangeRate: ExchangeRateView? = null
        set(value) {
            adapter.notifyDataSetChanged()
            field = value
        }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean =
            item is ExchangeRateView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_exchange, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(item: ExchangeRateView, holder: Holder, payloads: List<Any>) {
        holder.bind(item, balance, selectExchangeRate)
    }

    class Holder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvBase: TextView = requireViewById(itemView, R.id.tvBase)
        private val tvRate: TextView = requireViewById(itemView, R.id.tvRate)
        private val tvYouHave: TextView = requireViewById(itemView, R.id.tvYouHave)

        @SuppressLint("SetTextI18n")
        fun bind(
                item: ExchangeRateView,
                balance: Map<Currency, BigDecimal>,
                selectExchangeRate: ExchangeRateView?
        ) {
            val symbol = getCurrencySymbol(item.base, itemView.context)
            val symbolTarget =
                    getCurrencySymbol(selectExchangeRate?.base ?: USD, itemView.context)
            tvBase.text = item.base.name
            tvRate.text =
                    "${symbol}1 = $symbolTarget${getRate(selectExchangeRate?.base ?: USD, item)}"

            val youHaveString = "${balance[valueOf(item.base.name)]?.format(2)}$symbol"

            tvYouHave.text =
                    itemView.context.getString(R.string.currency_exchange_you_have, youHaveString)
        }

        private fun getCurrencySymbol(base: Currency, context: Context): String =
                when (base) {
                    USD -> context.getString(R.string.usd)
                    GBP -> context.getString(R.string.gbp)
                    EUR -> context.getString(R.string.eur)
                    RUB -> context.getString(R.string.rub)
                }

        private fun getRate(base: Currency, exchangeRate: ExchangeRateView?): String =
                when (base) {
                    USD -> exchangeRate?.rates?.usd?.format(DECIMAL_PLACES) ?: ""
                    GBP -> exchangeRate?.rates?.gbp?.format(DECIMAL_PLACES) ?: ""
                    EUR -> exchangeRate?.rates?.eur?.format(DECIMAL_PLACES) ?: ""
                    RUB -> exchangeRate?.rates?.rub?.format(DECIMAL_PLACES) ?: ""
                    else -> ""
                }

        companion object {
            private const val DECIMAL_PLACES = 3
        }
    }
}