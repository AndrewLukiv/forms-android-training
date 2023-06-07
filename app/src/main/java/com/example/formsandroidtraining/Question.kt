package com.example.formsandroidtraining

data class Question(
    val id:String,
    val formId:String,
    val title:String,
    val required:Boolean,
    val type:QuestionsType
)
