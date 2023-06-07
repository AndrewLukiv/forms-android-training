package com.example.formsandroidtraining.add_edit_question

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formsandroidtraining.FormsDestinationsArgs.FORM_ID_ARG
import com.example.formsandroidtraining.FormsDestinationsArgs.QUESTION_ID_ARG
import com.example.formsandroidtraining.data.QuestionModel
import com.example.formsandroidtraining.QuestionsType
import com.example.formsandroidtraining.data.FormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

open class Update<T : QuestionTypeUiState> {
    protected inline fun update(crossinline updater: (T) -> QuestionTypeUiState): QuestionTypeUpdater =
        {
            @Suppress("UNCHECKED_CAST")
            updater(it as T)
        }
}

object NumberQuestionSettings : Update<QuestionTypeUiState.Number>() {
    fun updateMin(newMin: String): QuestionTypeUpdater = update {
        it.copy(min = newMin)
    }

    fun updateMax(newMax: String): QuestionTypeUpdater = update {
        it.copy(max = newMax)
    }

    fun updateUseMin(newUseMin: Boolean): QuestionTypeUpdater = update {
        it.copy(useMin = newUseMin)
    }

    fun updateUseMax(newUseMax: Boolean): QuestionTypeUpdater = update {
        it.copy(useMax = newUseMax)
    }
}

object TextQuestionSettings : Update<QuestionTypeUiState.Text>() {
    fun updateSingleLine(isSingleLine: Boolean): QuestionTypeUpdater = update {
        it.copy(isSingleLine = isSingleLine)
    }


    fun updateMin(newMin: String): QuestionTypeUpdater = update {
        it.copy(
            min = newMin,
            minErrorMessage = minLengthValidator(newMin, it),
        )
    }


    fun updateMax(newMax: String): QuestionTypeUpdater = update {
        it.copy(
            max = newMax,
            maxErrorMessage = maxLengthValidator(newMax, it),
        )
    }

    fun updateUseMin(newUseMin: Boolean): QuestionTypeUpdater = update {
        it.copy(useMin = newUseMin)
    }

    fun updateUseMax(newUseMax: Boolean): QuestionTypeUpdater = update {
        it.copy(useMax = newUseMax)
    }
}

fun maxLengthValidator(newMax: String, settings: QuestionTypeUiState.Text): String? =
    newMax.toIntErrorOrNull { maxNumber ->
        when {
            maxNumber == 0 -> "Maybe you want create optional question?."
            maxNumber < 0 -> "String length cant be negative number"
            settings.useMin &&
                    settings.minErrorMessage == null &&
                    settings.min.isNotBlank() &&
                    maxNumber < settings.min.toInt() ->
                "Maximum should be grater then minimum"

            else -> null
        }
    }

fun minLengthValidator(newMin: String, settings: QuestionTypeUiState.Text): String? =
    newMin.toIntErrorOrNull { minNumber ->
        when {
            minNumber == 0 -> "Maybe you want create optional question?."
            minNumber < 0 -> "String length cant be negative number"
            settings.useMax &&
                    settings.maxErrorMessage == null &&
                    settings.max.isNotBlank() &&
                    minNumber > settings.max.toInt() ->
                "Minimum should be smaller then maximum"

            else -> null
        }
    }

fun String.toLongErrorOrNull(validator: (Long) -> String?): String? {
    val number = this.toLongOrNull()
    val error = if (number == null) {
        "Invalid number"
    } else {
        validator(number)
    }
    return error
}

fun String.toIntErrorOrNull(validator: (Int) -> String?): String? {
    val number = this.toIntOrNull()
    val error = if (number == null) {
        "Invalid number"
    } else {
        validator(number)
    }
    return error
}

sealed interface QuestionTypeUiState {
    fun toQuestionTypeUnchecked(): QuestionsType
    fun isValid(): Boolean

    data class Text(
        val isSingleLine: Boolean = false,
        val useMin: Boolean = false,
        val min: String = "",
        val minErrorMessage: String? = null,
        val useMax: Boolean = false,
        val max: String = "",
        val maxErrorMessage: String? = null,
    ) : QuestionTypeUiState {
        override fun isValid(): Boolean {
            val isMinLengthValid = !useMin || minLengthValidator(min, this) == null
            val isMaxLengthValid = !useMax || maxLengthValidator(max, this) == null
            return isMaxLengthValid && isMinLengthValid
        }

        override fun toQuestionTypeUnchecked() =
            QuestionsType.Text(
                singleLine = isSingleLine,
                minLength = if (useMin) min.toInt() else null,
                maxLength = if (useMax) max.toInt() else null,
            )

        companion object : QuestionTypeUiProperties {
            override val label = "Text question"

        }

    }

    data class Number(
        val useMin: Boolean = false,
        val min: String = "",
        val useMax: Boolean = false,
        val max: String = "",
    ) : QuestionTypeUiState {
        override fun isValid(): Boolean {
            return true
        }

        override fun toQuestionTypeUnchecked() =
            QuestionsType.Number(
                max = if (useMax) max.toLong() else null,
                min = if (useMin) min.toLong() else null,
            )

        companion object : QuestionTypeUiProperties {
            override val label = "Number question"
        }
    }

    companion object {
        fun getTypes() =
            QuestionTypeUiState::class.sealedSubclasses.map { type: KClass<out QuestionTypeUiState> ->
                val typeCompanion = type.companionObjectInstance as QuestionTypeUiProperties
                QuestionTypeUIProperty(
                    type,
                    label = typeCompanion.label
                )
            }.toImmutableList()

    }
}

private fun QuestionsType.toUiState(): QuestionTypeUiState = when (this) {
    is QuestionsType.Number -> QuestionTypeUiState.Number(
        min = min?.toString() ?: "",
        useMin = min != null,
        max = max?.toString() ?: "",
        useMax = max != null,
    )

    is QuestionsType.Text -> QuestionTypeUiState.Text(
        isSingleLine = singleLine,
        min = minLength?.toString() ?: "",
        useMin = minLength != null,
        max = maxLength?.toString() ?: "",
        useMax = maxLength != null,
    )
//        is QuestionsType.SingleChoice -> TODO()
//        is QuestionsType.MultipleChoices -> TODO()
    else -> {
        throw IllegalArgumentException()
    }
}

data class QuestionUiState(
    val title: String = "",
    val titleErrorMessage: String? = null,
    val isRequired: Boolean = true,
    val isQuestionValid: Boolean = false,
    val settings: QuestionTypeUiState = QuestionTypeUiState.Text(),
    val type: KClass<out QuestionTypeUiState> = QuestionTypeUiState.Text::class,
    val types: ImmutableList<QuestionTypeUIProperty> = QuestionTypeUiState.getTypes(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
) {
    val isValid: Boolean
        get() = isQuestionValid && settings.isValid()

    fun toQuestionModelUnchecked(formId: String) =
        QuestionModel(
            formId = formId,
            title = title,
            required = isRequired,
            type = settings.toQuestionTypeUnchecked()
        )
}

typealias QuestionTypeUpdater = (QuestionTypeUiState) -> QuestionTypeUiState

interface QuestionTypeUiProperties {
    val label: String
}

@Immutable
data class QuestionTypeUIProperty(
    val type: KClass<out QuestionTypeUiState>,
    val label: String,
)

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val formRepository: FormRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val formId: String = requireNotNull(savedStateHandle[FORM_ID_ARG])
    private val questionId: String? = savedStateHandle[QUESTION_ID_ARG]
    private val _questionUiState = MutableStateFlow(QuestionUiState())
    val questionUiState: StateFlow<QuestionUiState> = _questionUiState.asStateFlow()

    init {
        if (questionId != null) {
            loadQuestion(questionId)
        }
    }

    private inline fun updateQuestionUiState(updater: (QuestionUiState) -> QuestionUiState) {
        _questionUiState.update(updater)
    }

    fun updateTitle(newTitle: String) = updateQuestionUiState {
        it.copy(
            title = newTitle,
            isQuestionValid = newTitle.isNotBlank() && it.settings.isValid(),
            titleErrorMessage = if (newTitle.isBlank()) "Title cant be blank" else null
        )
    }

    fun updateIsRequired(required: Boolean) = updateQuestionUiState {
        it.copy(isRequired = required)
    }

    fun changeQuestionType(newType: KClass<out QuestionTypeUiState>) = updateQuestionUiState {
        it.copy(settings = newType.createInstance(), type = newType)
    }

    fun updateQuestionTypeData(updater: QuestionTypeUpdater) = updateQuestionUiState {
        val newSettings = updater(it.settings)
        it.copy(settings = newSettings)
    }

    private fun loadQuestion(questionId: String) {
        _questionUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val question = formRepository.getQuestion(questionId)
            _questionUiState.update {
                it.copy(
                    isLoading = false,
                    title = question.title,
                    isRequired = question.required,
                    settings = question.type.toUiState()
                )
            }
        }
    }

    fun saveQuestion() {
        val formModel = _questionUiState.value.toQuestionModelUnchecked(formId)
        println(formModel)
        if (questionId == null) {
            createQuestion(formModel)
        } else {
            updateQuestion(questionId, formModel)
        }
    }

    private fun updateQuestion(questionId: String, formModel: QuestionModel) {
        viewModelScope.launch {
            formRepository.updateQuestion(
                questionId,
                formModel
            )
            _questionUiState.update {
                it.copy(isSaved = true)
            }
        }
    }

    private fun createQuestion(formModel: QuestionModel) {
        viewModelScope.launch {
            val questionId =
                formRepository.createQuestion(formModel)
            _questionUiState.update {
                it.copy(isSaved = true)
            }
        }
    }
}