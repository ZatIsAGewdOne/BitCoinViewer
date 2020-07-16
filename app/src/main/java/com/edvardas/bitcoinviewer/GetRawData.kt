package com.edvardas.bitcoinviewer

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GetRawData(private var callback: DownloadComplete?) : AsyncTask<String, Unit, String>() {
    companion object {
        private const val TAG = "GetRawData"
    }
    private var status = DownloadStatus.IDLE

    fun runInSameThread(s: String?) {
        Log.d(TAG, "runInSameThread: starts")
        callback?.downloadComplete(doInBackground(s), status)
        Log.d(TAG, "runInSameThread: ends")
    }

    override fun onPostExecute(result: String?) {
        Log.d(TAG, "onPostExecute: result -> $result")
        callback?.downloadComplete(result, status)
    }

    override fun doInBackground(vararg strings: String?): String? {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        if (strings == null || strings.isEmpty()) {
            status = DownloadStatus.NOT_INITIALISED
            return null
        }

        try {
            status = DownloadStatus.PROCESSING
            val url = URL(strings[0])
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            Log.d(TAG, "doInBackground: response code was ${connection.responseCode}")
            val result = StringBuilder()
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line = reader.readLine()
            while(line != null) {
                result.append(line).append("\n")
                line = reader.readLine()
            }
            status = DownloadStatus.OK
            return result.toString()
        } catch (e: MalformedURLException) {
            Log.e(TAG, "doInBackground: Invalid URL! ${e.message}")
        } catch (e: IOException) {
            Log.e(TAG, "doInBackground: Error while reading data! ${e.message}")
        } catch (e: SecurityException) {
            Log.e(TAG, "doInBackground: Invalid access! ${e.message}")
        } finally {
            connection?.disconnect()
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: Exception) {
                    Log.e(TAG, "doInBackground: Error while closing reader! ${e.message}")
                }
            }
        }
        status = DownloadStatus.FAILED_OR_EMPTY
        return null
    }
}