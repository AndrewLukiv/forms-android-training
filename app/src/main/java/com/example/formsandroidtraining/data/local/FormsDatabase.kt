package com.example.formsandroidtraining.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [LocalQuestion::class, LocalForm::class], version = 2, exportSchema = false)
abstract class FormsDatabase : RoomDatabase() {
    abstract fun formDao(): FormDao
    abstract fun questionDao(): QuestionDao
}