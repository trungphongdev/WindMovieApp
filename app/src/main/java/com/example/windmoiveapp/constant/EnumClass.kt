package com.example.windmoiveapp.constant

import com.example.windmoiveapp.model.UserModel


enum class Categories(val type: String) {
    HOME("Home"),
    ACTION("Action"),
    ADVENTURES("Adventures"),
    ANIMATED("Animated"),
    CARTOONS("Cartoons"),
    FANTASY("Fantasy"),
    FUNNY("Funny"),
    NATURE("Nature"),
    SCIENCE_ART("Science & Arts"),
    SUPERHEROES("Superheroes"),
    ANIMALS("Animal"),
    UNKNOWN("");

    companion object {
        fun getCategoryByType(type: String): Categories {
            return values().find { type == it.type } ?: UNKNOWN
        }

        fun getCategoryByName(nameList: List<String>): String {
            val list = ArrayList<String>()
            for (name in nameList) {
                for (category in values()) {
                    if (category.name == name) {
                        list.add(category.type)
                    }
                }
            }
            return list.joinToString(separator = "  ", prefix = "", postfix = "")
        }


        fun getListTypeCategories(): ArrayList<String> {
            val listType = arrayListOf<String>()
            Categories.values().forEach {
                listType.add(it.type)
            }
            return listType
        }
    }
}

enum class AccountType(val type: Int) {
    NORMAL(0),
    VIP(1);
    companion object {
        fun getAccountByType(userModel: UserModel): AccountType {
            return values().first { it.type == userModel.accountType }
        }
    }
}

enum class AccountPermission(val type: Int) {
    USER(0),
    ADMIN(1)
}
enum class GenderType(val type: Int) {
    MALE(0),
    FEMALE(1),
    NOTHING(-1);
    companion object {
        fun getGenderByType(userModel: UserModel): GenderType {
            return values().first { it.type == userModel.gender }
        }
    }
}

enum class TypeLogin {
    FACEBOOK,
    FIREBASE,
    GOOGLE,
}

enum class StatusLovingMovie(val status: Int) {
    LIKE(0),
    DISLIKE(1),
    NOTHING(2),
}