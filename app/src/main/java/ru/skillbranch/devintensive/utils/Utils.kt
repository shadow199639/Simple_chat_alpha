package ru.skillbranch.devintensive.utils

import java.lang.Exception
import java.util.*

object Utils {

    private val dict = mapOf(
        'а' to "а",
        'б' to "b",
        'в' to "v",
        'г' to "g",
        'д' to "d",
        'е' to "e",
        'ё' to "e",
        'ж' to "zh",
        'з' to "z",
        'и' to "i",
        'й' to "i",
        'к' to "k",
        'л' to "l",
        'м' to "m",
        'н' to "n",
        'о' to "o",
        'п' to "p",
        'р' to "r",
        'с' to "s",
        'т' to "t",
        'у' to 'u',
        'ф' to "f",
        'х' to "h",
        'ц' to "c",
        'ч' to "ch",
        'ш' to "sh",
        'щ' to "sh'",
        'ъ' to "",
        'ы' to "i",
        'ь' to "",
        'э' to "e",
        'ю' to "yu",
        'я' to "ya"
    )

    fun parseFullName(fullName: String?): Pair<String?, String?> {

        var parts = fullName?.split(" ")
        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        when {
            fullName == "" -> {
                firstName = null
                lastName = null
            }
            fullName == " " -> {
                firstName = null
                lastName = null
            }
        }

        return firstName to lastName
    }

    //    fun transliteration(payload: String, divider: String = " "): String {
//
//        var res: String = ""
//        if(payload == null)
//            return null.toString()
//
//
//        for (i in payload.indices) {
//            if (dict.containsKey(payload[i].toLowerCase())) {
//                when {
//                    i == 0 -> res += dict[payload[i].toLowerCase()].toString().toUpperCase()
//                    payload[i - 1].toString() == " " -> res += dict[payload[i].toLowerCase()].toString()
//                        .toUpperCase()
//                    else -> res += dict[payload[i].toLowerCase()].toString()
//                }
//            } else {
//                if (payload[i].toString() == " ")
//                    res += divider
//                else
//                    res += payload[i]
//            }
//        }
//        return res
//    }
    fun transliteration(payload: String, divider: String = " "): String {
        var res = ""
        for (i in payload) {
            if (!dict.containsKey(i.toLowerCase())) {
                res += i
            } else {
                res += dict[i.toLowerCase()].toString()
            }
        }

        var parts = res.split(" ")
        when {
            parts[0] == "null" && parts[1] == "null" -> res = "null"
            parts[0] == "" -> {
                res = parts[1].get(0).toUpperCase() + parts[1].substring(1)
            }
            parts[1] == "" -> {
                res = parts[0].get(0).toUpperCase() + parts[0].substring(1)
            }
            else -> res = parts[0].get(0).toUpperCase() + parts[0].substring(1) +
                    divider + parts[1].get(0).toUpperCase() + parts[1].substring(1)
        }
        return res
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var fName = firstName?.trimStart()?.firstOrNull()?.toUpperCase()
        var sName = lastName?.trimStart()?.firstOrNull()?.toUpperCase()
        return when {
            fName == null && sName == null -> null
            fName == null -> sName.toString()
            sName == null -> fName.toString()
            else -> fName.toString() + sName.toString()
        }
    }
}

