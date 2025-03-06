package top.ctong.plugin.toggleboolean.utils

import ai.grazie.utils.isUppercase
import java.util.*

object BoolCvs {

    val WORD_RECORDS = mapOf(
        "true" to "false",
        "false" to "true",
        "on" to "off",
        "off" to "on",
        "yes" to "no",
        "no" to "yes",
        "1" to "0",
        "0" to "1",
        "enable" to "disable",
        "disable" to "enable",
        "enabled" to "disabled",
        "disabled" to "enabled",
        "before" to "after",
        "after" to "before",
        "first" to "last",
        "last" to "first",
    )

    fun cvt(boolWord: String): String? {
        return findMapping(boolWord, WORD_RECORDS)
    }

    fun findMapping(boolWord: String, mappings: Map<String, String>): String? {
        val tw = mappings[boolWord] ?: mappings[boolWord.lowercase(Locale.getDefault())]
        return format(boolWord, tw)
    }

    private fun format(word: String?, tw: String?): String? {
        if (word.isNullOrBlank() || tw.isNullOrBlank()) return null

        if (word.isUppercase()) return tw.uppercase()
        if (word.first().isUpperCase()) return tw.replaceFirstChar { char -> char.uppercase() }

        return tw
    }

}