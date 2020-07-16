package com.edvardas.bitcoinviewer

data class Currency(
    var code: String,
    var symbol: String,
    var rate: String,
    var description: String,
    var rateFloat: Float
)