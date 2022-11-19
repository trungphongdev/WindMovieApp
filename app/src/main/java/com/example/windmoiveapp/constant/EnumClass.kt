package com.example.windmoiveapp.constant


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

enum class AccountPermission(val type: Int) {
    NORMAL(0),
    VIP(1)
}

enum class AccountType(val type: Int) {
    USER(0),
    ADMIN(1)
}