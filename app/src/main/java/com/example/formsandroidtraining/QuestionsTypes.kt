package com.example.formsandroidtraining

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface QuestionsType {
    @Serializable
    @SerialName("text")
    data class Text(
        val minLength: Int?,
        val maxLength: Int?,
        val singleLine: Boolean,
    ) : QuestionsType

    @Serializable
    @SerialName("number")
    data class Number(
        val min: Long?,
        val max: Long?,
    ) : QuestionsType

    @Serializable
    @SerialName("single-choice")
    data class SingleChoice(
        val choices: List<String>
    ) : QuestionsType

    @Serializable
    @SerialName("multiple-choices")
    data class MultipleChoices(
        val minSelected: Int?,
        val maxSelected: Int?,
        val choices: List<String>
    ) : QuestionsType
}

