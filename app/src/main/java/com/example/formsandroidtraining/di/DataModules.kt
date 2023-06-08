package com.example.formsandroidtraining.di

import android.content.Context
import androidx.room.Room
import com.example.formsandroidtraining.data.DefaultFormRepository
import com.example.formsandroidtraining.data.FormRepository
import com.example.formsandroidtraining.data.local.FormDao
import com.example.formsandroidtraining.data.local.FormsDatabase
import com.example.formsandroidtraining.data.local.QuestionDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindFormRepository(repository: DefaultFormRepository): FormRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            FormsDatabase::class.java,
            "forms.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesFormDao(database: FormsDatabase): FormDao = database.formDao()

    @Provides
    fun providesQuestionDao(database: FormsDatabase): QuestionDao = database.questionDao()
}