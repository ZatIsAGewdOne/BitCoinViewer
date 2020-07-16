package com.edvardas.bitcoinviewer

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class CoinDeskJsonData(private var callback: DataAvailable?, private var baseUrl: String) : AsyncTask<String, Unit, List<Currency>>(), DownloadComplete {
    companion object {
        private const val TAG = "CoinDeskJsonData"
    }

    var currencyList: MutableList<Currency>? = null
    private var runningOnSameThread = false

    fun executeOnSameThread() {
        runningOnSameThread = true
        val rawData = GetRawData(this)
        rawData.execute(createUri())
    }

    override fun onPostExecute(result: List<Currency>?) {
        Log.d(TAG, "onPostExecute: starts")
        callback?.dataAvailable(result, DownloadStatus.OK)
        Log.d(TAG, "onPostExecute: ends")
    }

    override fun doInBackground(vararg p0: String?): List<Currency>? {
        Log.d(TAG, "doInBackground: starts")
        val rawData = GetRawData(this)
        rawData.runInSameThread(createUri())
        return currencyList
    }

    override fun downloadComplete(data: String?, downloadStatus: DownloadStatus?) {
        var status = downloadStatus
        Log.d(TAG, "downloadComplete: status = $downloadStatus")
        if (status == DownloadStatus.OK) {
            currencyList = ArrayList()

            try {
                val jsonData = JSONObject(data)
                val bpi = jsonData.getJSONObject("bpi")
                bpi.keys().forEach {
                    val curr = createCurrency(bpi.getJSONObject(it))
                    currencyList?.add(curr)
                    Log.d(TAG, "downloadComplete: Added currency $curr")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.e(TAG, "downloadComplete: Error while parsing JSON! ${e.message}")
                status = DownloadStatus.FAILED_OR_EMPTY
            }
        }
        if (runningOnSameThread && callback != null) {
            callback?.dataAvailable(currencyList, status)
        }
    }

    private fun createCurrency(bpi: JSONObject): Currency {
        val code = bpi.getString("code")
        var symbol = bpi.getString("symbol")
        symbol = updateSymbol(symbol)
        val rate = bpi.getString("rate")
        val description = bpi.getString("description")
        val rateFloat = bpi.getDouble("rate_float").toFloat()
        return Currency(code, symbol, rate, description, rateFloat)
    }

    private fun createUri(): String {
        return Uri.parse(baseUrl).buildUpon().build().toString()
    }

    private fun updateSymbol(symbol: String) : String {
        var symbol = symbol
        when(symbol) {
            "&#36;" -> symbol = "\u0024"
            "&pound;" -> symbol = "\u00a3"
            "&euro;" -> symbol = "\u20ac"
        }
        return symbol
    }
}