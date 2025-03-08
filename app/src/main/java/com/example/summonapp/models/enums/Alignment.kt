package com.example.summonapp.models.enums

import com.example.summonapp.capitalise

enum class Alignment {
    LAWFUL_GOOD, NEUTRAL_GOOD, CHAOTIC_GOOD,
    LAWFUL_NEUTRAL, NEUTRAL, CHAOTIC_NEUTRAL,
    LAWFUL_EVIL, NEUTRAL_EVIL, CHAOTIC_EVIL;

    fun displayString(): String {
        return this.toString().lowercase().replace("_", " ").capitalise()
    }
}