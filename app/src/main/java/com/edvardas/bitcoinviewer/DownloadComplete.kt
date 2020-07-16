package com.edvardas.bitcoinviewer

interface DownloadComplete {
    fun downloadComplete(data: String?, downloadStatus: DownloadStatus?)
}