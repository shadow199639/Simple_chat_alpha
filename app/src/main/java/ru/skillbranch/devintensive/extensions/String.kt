package ru.skillbranch.devintensive.extensions

fun String.truncate(len: Int = 16): String{
    var res = this
    return res.substring(0, len).trimEnd() + "..."
}

fun String.stripHtml() = this.replace(Regex("<[^>]*>|&amp;|&lt;|&gt;|&quot;|&apos;|&#\\d+;"),"").replace(Regex(" +"), " ")