package com.imhanjie.v2ex.api.model

data class MyUserInfo(
    val userName: String,
    val avatar: String,
    val nodeStars: Long,
    val topicStars: Long,
    val following: Long,
    val siteNo: Long,
    val moneyGold: Long,
    val moneySilver: Long,
    val moneyBronze: Long
)