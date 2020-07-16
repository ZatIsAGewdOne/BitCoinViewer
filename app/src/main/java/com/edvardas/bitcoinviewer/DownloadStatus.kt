package com.edvardas.bitcoinviewer

enum class DownloadStatus {
    IDLE,
    PROCESSING,
    NOT_INITIALISED,
    FAILED_OR_EMPTY,
    OK
}