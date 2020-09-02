package ru.skillbranch.devintensive.extensions

import android.text.BoringLayout
import java.lang.Exception
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

//класс перечислений
enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(time: Int): String {
        return when (time % 10) {
            1 -> {
                return when (this.name) {
                    SECOND.name -> "$time секунду"
                    MINUTE.name -> "$time минуту"
                    HOUR.name -> "$time час"
                    else -> "$time день"
                }
            }
            in 2..4 -> {
                return when (this.name) {
                    SECOND.name -> "$time секунды"
                    MINUTE.name -> "$time минуты"
                    HOUR.name -> "$time часа"
                    else -> "$time дня"
                }
            }

            else -> {
                return when (this.name) {
                    SECOND.name -> "$time секунд"
                    MINUTE.name -> "$time минут"
                    HOUR.name -> "$time часов"
                    else -> "$time дней"
                }
            }
        }
    }
}

fun Date.humanizeDiff(date: Date = Date()): String {
    var diff = this.time - date.time
    var isFuture: Boolean

    isFuture = diff > 0

    diff = abs(diff)
    if (isFuture) {
        return when (diff) {
            in (0 * SECOND)..(1 * SECOND) -> "через секунду"
            in (1 * SECOND)..(45 * SECOND) -> "через несколько секунд"
            in (45 * SECOND)..(75 * SECOND) -> "через минуту"
            in (75 * SECOND)..(45 * MINUTE) -> "через ${TimeUnits.MINUTE.plural((diff / MINUTE).toInt())}"
            in (45 * MINUTE)..(75 * MINUTE) -> "через час "
            in (75 * MINUTE)..(22 * HOUR) -> "через ${TimeUnits.HOUR.plural((diff / HOUR).toInt())}"
            in (22 * HOUR)..(26 * HOUR) -> "через день"
            in (26 * HOUR)..(360 * DAY) -> "через ${TimeUnits.DAY.plural((diff / DAY).toInt())}"
            else -> "более чем через год"
        }
    } else {
        return when (diff) {
            in (0 * SECOND)..(1 * SECOND) -> "только что"
            in (1 * SECOND)..(45 * SECOND) -> "несколько секунд назад"
            in (45 * SECOND)..(75 * SECOND) -> "минуту назад"
            in (75 * SECOND)..(45 * MINUTE) -> "${TimeUnits.MINUTE.plural((diff / MINUTE).toInt())} назад"
            in (45 * MINUTE)..(75 * MINUTE) -> "час назад"
            in (75 * MINUTE)..(22 * HOUR) -> "${TimeUnits.HOUR.plural((diff / HOUR).toInt())} назад"
            in (22 * HOUR)..(26 * HOUR) -> "день назад"
            in (26 * HOUR)..(360 * DAY) -> "${TimeUnits.DAY.plural((diff / DAY).toInt())} назад"
            else -> "более года назад"
        }
    }
}