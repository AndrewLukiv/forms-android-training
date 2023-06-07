package com.example.formsandroidtraining.create_edit_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formsandroidtraining.FormsDestinations
import com.example.formsandroidtraining.FormsDestinationsArgs.FORM_ID_ARG
import com.example.formsandroidtraining.data.FormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CREATE_FORM_LABEL = "Create form"
private const val UPDATE_FORM_LABEL = "Update form"

data class FormUiState(
    val name: String = "",
    val actionLabel: String = CREATE_FORM_LABEL,
    val isLoading: Boolean = false,
    val nameErrorMessage: String? = null,
    val isSaved: Boolean = false,
    val createdFormId: String? = null
)

@HiltViewModel
class FormViewModel @Inject constructor(
    private val formRepository: FormRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val id: String? = savedStateHandle[FORM_ID_ARG]
    private val _formUiState: MutableStateFlow<FormUiState> = MutableStateFlow(FormUiState())
    val formUiState = _formUiState.asStateFlow()

    init {
        if (id != null) {
            loadForm(id)
        }
    }

    fun updateName(newName: String) {
        _formUiState.update {
            it.copy(
                name = newName,
                nameErrorMessage = if (it.name.isNotBlank()) null else it.nameErrorMessage
            )
        }
    }

    fun saveForm() {
        if (_formUiState.value.name.isBlank()) {
            _formUiState.update { it.copy(nameErrorMessage = "Name should be not blank") }
            return
        }
        if (id == null) {
            createForm()
        } else {
            updateForm(id)
        }
    }

    private fun updateForm(formId: String) {
        viewModelScope.launch {
            formRepository.updateForm(formId, _formUiState.value.name)
            _formUiState.update {
                it.copy(isSaved = true)
            }
        }
    }

    private fun createForm() {
        viewModelScope.launch {
            val formId = formRepository.createForm(_formUiState.value.name)
            _formUiState.update {
                it.copy(isSaved = true, createdFormId = formId)
            }
        }
    }

    private fun loadForm(formId: String) {
        _formUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val form = formRepository.getForm(formId)
            _formUiState.update {
                it.copy(
                    name = form.name,
                    isLoading = false,
                    actionLabel = UPDATE_FORM_LABEL
                )
            }
        }
    }
}