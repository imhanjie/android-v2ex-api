package com.imhanjie.v2ex.api.model

data class MyUserInfo(
    var name: String,
    var avatar: String,
    var nodeStars: Long,
    var topicStars: Long,
    var following: Long,
    var siteNo: Long,
    var moneyGold: Long,
    var moneySilver: Long,
    var moneyBronze: Long
)