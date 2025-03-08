package com.example.summonapp

import java.util.Locale

fun Int.formatAsModifier(): String = if (this >= 0) "+$this" else "$this"

fun String.capitalise(): String = lowercase().replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

fun String.capitaliseWords(): String = split(" ").joinToString(" ") { it.capitalise() }
