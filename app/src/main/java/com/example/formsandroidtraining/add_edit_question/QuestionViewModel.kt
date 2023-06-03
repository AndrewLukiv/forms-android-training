package com.example.formsandroidtraining.add_edit_question

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

data class QuestionUiState(
    val title: String = "",
    val isRequired: Boolean = true,
)
typealias QuestionTypeUpdater = (QuestionTypeUiState) -> QuestionTypeUiState

fun interface UpdatableQuestionSettingsUi : (QuestionTypeUpdater) -> Unit {
    fun <T> ((T) -> QuestionTypeUpdater).callback(): (T) -> Unit =
        {
            this@UpdatableQuestionSettingsUi(this(it))
        }
}

interface QuestionTypeUiProperties {
    val label: String
}

data class QuestionTypeUIProperty(
    private val type: KClass<out QuestionTypeUiState>,
    val label: String,
    val defaultValue: QuestionTypeUpdater
) {
    fun isInstance(value: QuestionTypeUiState) = type.isInstance(value)
}

sealed interface QuestionTypeUiState {
    data class Text(
        val isSingleLine: Boolean = false,
        val useMin: Boolean = false,
        val min: String = "",
        val useMax: Boolean = false,
        val max: String = ""
    ) : QuestionTypeUiState {
        fun updateSingleLine(isSingleLine: Boolean): QuestionTypeUpdater = {
                (it as Text).copy(isSingleLine = isSingleLine)
            }
        fun updateMin(newMin: String): QuestionTypeUpdater = {
            this@Text.copy(min = newMin)
        }

        fun updateMax(newMax: String): QuestionTypeUpdater = {
            this@Text.copy(max = newMax)
        }

        fun updateUseMin(newUseMin: Boolean): QuestionTypeUpdater = {
            this@Text.copy(useMin = newUseMin)
        }

        fun updateUseMax(newUseMax: Boolean): QuestionTypeUpdater = {
            this@Text.copy(useMax = newUseMax)
        }

        @Composable
        override fun QuestionTypeSettings(questionUpdatesContext: UpdatableQuestionSettingsUi) =
            questionUpdatesContext.TextQuestionSettings(
                textQuestionUiState = this,
            )

        companion object : QuestionTypeUiProperties {
            override val label = "Text question"

        }
    }

    data class Number(
        val useMin: Boolean = false,
        val min: String = "",
        val useMax: Boolean = false,
        val max: String = ""
    ) : QuestionTypeUiState {
        fun updateMin(newMin: String): QuestionTypeUpdater = {
            this@Number.copy(min = newMin)
        }

        fun updateMax(newMax: String): QuestionTypeUpdater = {
            this@Number.copy(max = newMax)
        }

        fun updateUseMin(newUseMin: Boolean): QuestionTypeUpdater = {
            this@Number.copy(useMin = newUseMin)
        }

        fun updateUseMax(newUseMax: Boolean): QuestionTypeUpdater = {
            this@Number.copy(useMax = newUseMax)
        }

        @Composable
        override fun QuestionTypeSettings(questionUpdatesContext: UpdatableQuestionSettingsUi) =
            questionUpdatesContext.NumberQuestionSettings(
                numberQuestionUiState = this,
            )

        companion object : QuestionTypeUiProperties {
            override val label = "Number question"
        }
    }

    @Composable
    fun QuestionTypeSettings(questionUpdatesContext: UpdatableQuestionSettingsUi)

    companion object {
        fun getTypes() =
            QuestionTypeUiState::class.sealedSubclasses.map { type: KClass<out QuestionTypeUiState> ->
                val typeCompanion = type.companionObjectInstance as QuestionTypeUiProperties
                QuestionTypeUIProperty(
                    type,
                    label = typeCompanion.label,
                    defaultValue = {
                        type.createInstance()
                    }
                )
            }

    }
}
@HiltViewModel
class QuestionViewModel @Inject constructor() : ViewModel() {
    private val _questionUiState = MutableStateFlow(QuestionUiState())
    val questionUiState: StateFlow<QuestionUiState> = _questionUiState.asStateFlow()

    val _typeUiState: MutableStateFlow<QuestionTypeUiState> =
        MutableStateFlow(QuestionTypeUiState.Text())
    val typeUiState: StateFlow<QuestionTypeUiState> = _typeUiState.asStateFlow()

    fun updateTitle(newTitle: String) {
        _questionUiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateIsRequired(required: Boolean) {
        _questionUiState.update {
            it.copy(isRequired = required)
        }
    }


    fun updateQuestionTypeData(updater: QuestionTypeUpdater) {
        _typeUiState.update(updater)
    }
}