package com.example.formsandroidtraining.forms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.formsandroidtraining.data.FormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormsViewModel @Inject constructor(
    private val formRepository: FormRepository,
//    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val forms = formRepository.getFormsStream()
}
