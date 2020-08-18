package ru.skillbranch.devintensive.utils

import java.lang.Exception
import java.util.*

object Utils {
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

    fun transliteration(payload: String, divider: String = " "): String {
        val dict = mapOf(
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
        var res: String = ""

        for (i in 0..payload.length - 1) {
            if (dict.containsKey(payload[i].toLowerCase())) {
                when {
                    i == 0 -> res += dict[payload[i].toLowerCase()].toString().toUpperCase()
                    payload[i - 1].toString() == " " -> res += dict[payload[i].toLowerCase()].toString()
                        .toUpperCase()
                    else -> res += dict[payload[i].toLowerCase()].toString()
                }
            } else {
                if (payload[i].toString() == " ")
                    res += divider
                else
                    res += payload[i]
            }
        }
        return res
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var res: String? =
            null

        try {
            when {
                firstName == null -> {
                    res = lastName?.toUpperCase(Locale.ENGLISH)?.first().toString()
                }
                lastName == null -> {
                    res = firstName?.toUpperCase(Locale.ENGLISH)?.first().toString()
                }
                firstName == null && lastName == null -> {
                    res = null
                }
                firstName == "" && lastName == "" -> {
                    res = null
                }
                firstName == " " && lastName == " " -> {
                    res = null
                }
                else -> res = firstName?.toUpperCase(Locale.ENGLISH)?.first()
                    .toString() + lastName?.toUpperCase(Locale.ENGLISH)?.first().toString()
            }
        } catch (e: Exception) {
            res = "Incorrect format"
        }
        return res
    }
}

