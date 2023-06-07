package com.example.formsandroidtraining.data

import com.example.formsandroidtraining.Form
import com.example.formsandroidtraining.Question
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface FormRepository {
    fun getQuestionsStream(formId: String): Flow<ImmutableList<Question>>
    suspend fun createQuestion(questionModel: QuestionModel): String
    suspend fun updateQuestion(id: String, questionModel: QuestionModel)
    suspend fun getQuestion(id: String): Question
    fun getFormsStream(): Flow<ImmutableList<Form>>
    suspend fun createForm(name: String): String
    suspend fun updateForm(id: String, name: String)
    suspend fun getForm(id: String): Form
    fun getFormStream(id: String): Flow<Form>
}