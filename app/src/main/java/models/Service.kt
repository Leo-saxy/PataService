package com.example.pataservice.models

data class Service(
    var id: String = "",
    var providerId: String = "",
    var providerName: String = "",
    var serviceName: String = "",
    var category: String = "",
    var description: String = "",
    var price: Double = 0.0
)
