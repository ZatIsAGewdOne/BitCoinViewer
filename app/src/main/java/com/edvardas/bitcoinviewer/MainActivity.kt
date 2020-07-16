package com.edvardas.bitcoinviewer

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), DataAvailable {
    // api https://api.coindesk.com/v1/bpi/currentprice.json
    companion object {
        private const val TAG = "MainActivity"
    }

    private var currencyAdapter: CurrencyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onResume() {
        super.onResume()

        val coinDeskData = CoinDeskJsonData(this, "https://api.coindesk.com/v1/bpi/currentprice.json")
        coinDeskData.executeOnSameThread()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun dataAvailable(data: List<Currency>?, downloadStatus: DownloadStatus?) {
        if (downloadStatus == DownloadStatus.OK) {
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)
            currencyAdapter = CurrencyAdapter(this, data)
            recyclerView.adapter = currencyAdapter
            Log.d(TAG, "dataAvailable: data is $data")
        } else {
            Log.e(TAG, "dataAvailable: failed with status $downloadStatus")
        }
    }
}