package com.example.formsandroidtraining.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDao {
    @Query("SELECT * FROM forms")
    fun observeAll(): Flow<List<LocalForm>>

    @Query("SELECT * FROM forms WHERE id = :formId")
    fun observeById(formId: String): Flow<LocalForm>

    @Upsert
    suspend fun upsert(form: LocalForm)

    @Query("SELECT * FROM forms WHERE id = :formId")
    suspend fun getById(formId: String): LocalForm
}
