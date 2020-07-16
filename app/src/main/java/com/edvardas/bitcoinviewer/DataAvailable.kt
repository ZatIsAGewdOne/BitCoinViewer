package com.edvardas.bitcoinviewer

interface DataAvailable {
    fun dataAvailable(data: List<Currency>?, downloadStatus: DownloadStatus?)
}