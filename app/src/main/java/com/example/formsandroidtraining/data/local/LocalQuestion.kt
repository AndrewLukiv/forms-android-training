package com.example.formsandroidtraining.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.formsandroidtraining.Question
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "questions")
data class LocalQuestion(
    @PrimaryKey val id: String,
    val formId:String,
    val title: String,
    val isRequired: Boolean,
    val settings: String
)

fun Question.toLocal() = LocalQuestion(
    id=id,
    formId=formId,
    title=title,
    isRequired = required,
    settings = Json.encodeToString(type)
)

fun LocalQuestion.toExternal()=
    Question(
        id=id,
        formId=formId,
        title=title,
        required = isRequired,
        type = Json.decodeFromString(settings)
    )
