package com.example.windmoiveapp.extension

import com.example.windmoiveapp.model.UserModel
import com.google.gson.Gson

object GsonExt {
    fun convertObjetToGson(item : Any): String {
        return Gson().toJson(item).toString()
    }
    fun convertObjetToGson(listItem: List<Any>): String {
        return Gson().toJson(listItem).toString()
    }

    fun <T> convertGsonToObjet(itemJson: String, nameClass: Class<T>): T {
         return Gson().fromJson(itemJson, nameClass)
    }
}