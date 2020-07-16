package com.edvardas.bitcoinviewer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyAdapter(private var context: Context?, private var currencyList: List<Currency>?) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    companion object {
        private const val TAG = "CurrencyAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        Log.d(TAG, "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val curr = currencyList?.get(position)
        Log.d(TAG, "onBindViewHolder: ${curr?.code} --> $position")
        holder.currCode?.text = curr?.code
        holder.currDescription?.text = curr?.description
        holder.currSymbol?.text = curr?.symbol
        holder.currRate?.text = curr?.rateFloat?.toString()
    }

    override fun getItemCount(): Int {
        return if (currencyList != null && currencyList?.size != 0) currencyList?.size!! else 0
    }

    fun loadNewData(currencyList: List<Currency>?) {
        this.currencyList = currencyList
        notifyDataSetChanged()
    }

    class CurrencyViewHolder(currView: View) : RecyclerView.ViewHolder(currView) {
        var currCode: TextView? = null
        var currSymbol: TextView? = null
        var currDescription: TextView? = null
        var currRate: TextView? = null

        init {
            currCode = currView.findViewById(R.id.curr_code)
            currSymbol = currView.findViewById(R.id.curr_symbol)
            currDescription = currView.findViewById(R.id.curr_description)
            currRate = currView.findViewById(R.id.curr_rate)
        }
    }
}