package com.example.whoareyou.model

data class Employee(
    val id: Int,
    val name: String,
    val team: String,
    val position: String,
    val nickname: String,
    val jobTitle: String,
    val internalPhone: String,
    val mobilePhone: String,
    val fax: String,
    val email: String,
    val profileImageName: String? = null,
    val isFavorite: Boolean = false
)
