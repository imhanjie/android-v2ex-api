package com.imhanjie.v2ex.api.support

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*

inline fun <reified T> parseJson(json: String): T {
    return Gson().fromJson(json, T::class.java)
}

inline fun <reified T> parseJsonList(json: String): ArrayList<T> {
    val type = object : TypeToken<ArrayList<JsonObject>>() {
    }.type
    val jsonObjects = Gson().fromJson<ArrayList<JsonObject>>(json, type)
    val arrayList = ArrayList<T>()
    for (jsonObject in jsonObjects) {
        arrayList.add(Gson().fromJson(jsonObject, T::class.java))
    }
    return arrayList
}

fun parseToJson(o: Any): String {
    return Gson().toJson(o)
}