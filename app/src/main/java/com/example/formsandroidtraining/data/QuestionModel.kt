package com.example.formsandroidtraining.data

import com.example.formsandroidtraining.Question
import com.example.formsandroidtraining.QuestionsType
import com.example.formsandroidtraining.data.local.toLocal

data class QuestionModel(
    val title: String,
    val formId: String,
    val required: Boolean,
    val type: QuestionsType
)

fun QuestionModel.toLocal(id: String) =
    Question(
        id = id,
        formId = formId,
        title = title,
        required = required,
        type = type
    ).toLocal()