package com.krakos.minijp.model

import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("data") val words: List<Word>
)

/**
 * @param slug id in the form of word or some sort of ID
 * @param jlpt represents the exam level at which the word is expected to be known
 * @param japanese at least one-element-list of forms of given word
 */
data class Word(
    val slug: String,
    @SerializedName("is_common") val isCommon: Boolean,
    val jlpt: List<String>,
    val japanese: List<Japanese>,
    @SerializedName("senses") val translations: List<Translation>
)


val sampleWord =
    Word(
        "乗り物 ",
        true,
        listOf("jlpt-n5"),
        listOf(Japanese("乗り物a", "かてい")),
        listOf(
            Translation(
                listOf("home", "household","family","hearth"),
                listOf("Noun"),
                listOf("wanikani12")
            ),
            Translation(
                listOf("home", "household","family","hearth"),
                listOf("Wikipedia definition"),
                listOf("wanikani0")
            ),
            Translation(
                listOf("car"),
                listOf("Noun"),
                listOf("wanikani3")
            )

        )
    )