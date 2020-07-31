package com.example.fianlappfirebase.acitvities.Models

import java.util.*

data class Messages_Model(
    val authorId: String = "",
    val message: String = "",
    val profileImageURL: String = "",
    val sentAt: Date = Date()){

}