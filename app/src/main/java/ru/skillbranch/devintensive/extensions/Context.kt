package ru.skillbranch.avatarimageview.extentions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage

fun Context.dpToPx(dp:Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

fun shortMessage(message: BaseMessage?): Pair<String, String?> = when(message){
    is ImageMessage -> "${message.from.firstName} - отправил фото" to null
    is TextMessage -> message.text.orEmpty() to message.from.firstName
    else -> "Сообщений ещё нет" to null
}

fun <E> MutableList<E>.insertIf(e: E, index: Int, predicate: MutableList<E>.() -> Boolean): MutableList<E> {
    if (this.predicate()) this.add(index, e)
    return this
}
