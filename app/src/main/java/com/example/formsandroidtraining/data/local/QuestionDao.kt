package com.example.formsandroidtraining.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE formId = :formId ORDER BY i ASC")
    fun observeByFormId(formId: String): Flow<List<LocalQuestion>>

    @Upsert
    suspend fun upsert(question: LocalQuestion)

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getById(questionId: String): LocalQuestion

    @Query("SELECT i FROM questions WHERE id = :questionId")
    suspend fun getIndexById(questionId: String): Int

    @Query("SELECT i FROM questions WHERE formId = :formId ORDER BY i DESC LIMIT 1")
    suspend fun getFormLastQuestionIndex(formId: String): Int?
}