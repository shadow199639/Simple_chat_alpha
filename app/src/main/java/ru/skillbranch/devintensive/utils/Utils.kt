package ru.skillbranch.devintensive.utils

import java.lang.Exception
import java.util.*

object Utils {

    private val dict = mapOf(
        'а' to "a",
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

    fun transliteration(payload: String, divider: String = " "): String {
        var res = ""

        for (i in payload) {
            when {
                i == ' ' -> res += divider
                !dict.containsKey(i.toLowerCase()) -> res += i
                dict.containsKey(i.toLowerCase()) -> {
                    if (i.isLowerCase())
                        res += dict[i]
                    else {
                        var temp = dict[i.toLowerCase()].toString()
                        if (temp.length > 1) {
                            res += temp[0].toUpperCase()
                            for(j in 1 .. temp.length - 1){
                                res += temp[j]
                            }
                        } else res += temp.toUpperCase()
                    }
                }
            }
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

    fun validCheck(payload: String): Boolean = payload.isEmpty() || payload.matches(
        Regex("^(https://){0,1}(www.){0,1}github.com\\/[A-z\\d](?:[A-z\\d]|(_|-)(?=[A-z\\d])){0,256}(/)?\$",RegexOption.IGNORE_CASE)) &&
            !payload.matches(Regex("^.*(" +
                    "\\/enterprise|" +
                    "\\/features|" +
                    "\\/topics|" +
                    "\\/collections|" +
                    "\\/trending|" +
                    "\\/events|" +
                    "\\/marketplace" +
                    "|\\/pricing|" +
                    "\\/nonprofit|" +
                    "\\/customer-stories|" +
                    "\\/security|" +
                    "\\/login|" +
                    "\\/join)\$",RegexOption.IGNORE_CASE)
            )
}

