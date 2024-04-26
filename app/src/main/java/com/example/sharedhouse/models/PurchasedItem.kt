package com.example.sharedhouse.models

class PurchasedItem {
    var name: String = ""
    var price: Double = 0.0
    var sharedWith: List<String> = emptyList()
    var purchasedBy: String = ""
    var quantity = 0
    var picture_uuid: String  = ""
    var documentId: String = ""
}