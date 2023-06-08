package com.example.formsandroidtraining.data

import com.example.formsandroidtraining.Form
import com.example.formsandroidtraining.Question
import com.example.formsandroidtraining.data.local.FormDao
import com.example.formsandroidtraining.data.local.FormsDatabase
import com.example.formsandroidtraining.data.local.LocalForm
import com.example.formsandroidtraining.data.local.QuestionDao
import com.example.formsandroidtraining.data.local.toExternal
import com.example.formsandroidtraining.di.ApplicationScope
import com.example.formsandroidtraining.di.DefaultDispatcher
import com.example.formsandroidtraining.di.IoDispatcher
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFormRepository @Inject constructor(
    private val formDao: FormDao,
    private val questionDao: QuestionDao,
    private val db: FormsDatabase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : FormRepository {
    private suspend fun createUUID(): String = withContext(defaultDispatcher) {
        UUID.randomUUID().toString()
    }

    override fun getQuestionsStream(formId: String): Flow<ImmutableList<Question>> =
        questionDao.observeByFormId(formId).map { localQuestions ->
            withContext(defaultDispatcher) {
                localQuestions.map { it.toExternal() }.toImmutableList()
            }
        }

    override suspend fun createQuestion(questionModel: QuestionModel): String {
        val questionId = createUUID()
        withContext(ioDispatcher) {
            val questionIndex =
                questionDao.getFormLastQuestionIndex(questionModel.formId)?.let { it.toUInt() + 1u }
                    ?: 0u
            val question = questionModel.toLocal(questionId, questionIndex)
            questionDao.upsert(question)
        }
        return questionId
    }

    override suspend fun updateQuestion(id: String, questionModel: QuestionModel) {
        withContext(ioDispatcher) {
            val questionIndex = questionDao.getIndexById(id).toUInt()
            val question = questionModel.toLocal(id, questionIndex)
            questionDao.upsert(question)
        }
    }

    override suspend fun getQuestion(id: String): Question =
        withContext(ioDispatcher) { questionDao.getById(id).toExternal() }

    override fun getFormsStream(): Flow<ImmutableList<Form>> =
        formDao.observeAll().map { localForms ->
            withContext(defaultDispatcher) {
                localForms.map { it.toExternal() }.toImmutableList()
            }
        }

    override suspend fun createForm(name: String): String {
        val formId = createUUID()
        updateForm(formId, name)
        return formId
    }

    override suspend fun updateForm(id: String, name: String) {
        val form = LocalForm(
            id = id,
            name = name
        )
        withContext(ioDispatcher) {
            formDao.upsert(form)
        }
    }

    override suspend fun getForm(id: String) =
        withContext(ioDispatcher) { formDao.getById(id).toExternal() }

    override fun getFormStream(id: String): Flow<Form> =
        formDao.observeById(id).map { localForm ->
            localForm.toExternal()
        }
}