package com.example.formsandroidtraining.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE formId = :formId")
    fun observeByFormId(formId: String): Flow<List<LocalQuestion>>

    @Upsert
    suspend fun upsert(question: LocalQuestion)

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getById(questionId: String): LocalQuestion
}