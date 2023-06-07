package com.example.formsandroidtraining.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalQuestion::class, LocalForm::class], version = 1, exportSchema = false)
abstract class FormsDatabase : RoomDatabase() {
    abstract fun formDao(): FormDao
    abstract fun questionDao(): QuestionDao
}