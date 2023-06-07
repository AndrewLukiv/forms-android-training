package com.example.formsandroidtraining.edit_questions_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formsandroidtraining.Form
import com.example.formsandroidtraining.FormsDestinationsArgs.FORM_ID_ARG
import com.example.formsandroidtraining.Question
import com.example.formsandroidtraining.data.FormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface EditQuestionsListUiState {
    data class Loaded(
        val form: Form,
        val questions: ImmutableList<Question>
    ) : EditQuestionsListUiState

    object Loading : EditQuestionsListUiState
}


@HiltViewModel
class EditQuestionsListViewModel @Inject constructor(
    private val formRepository: FormRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val formId: String = requireNotNull(savedStateHandle[FORM_ID_ARG])
    private val _form: Flow<Form> = formRepository.getFormStream(formId)
    private val _questions: Flow<ImmutableList<Question>> =
        formRepository.getQuestionsStream(formId)

    val uiState: Flow<EditQuestionsListUiState> =
        combine(_form, _questions) { form, questions ->
            EditQuestionsListUiState.Loaded(
                form = form,
                questions = questions
            )
        }

}
