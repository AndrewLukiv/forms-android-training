package com.example.formsandroidtraining.create_edit_form

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditFormScreen(
    onFormSaved: (String?) -> Unit,
    viewModel: FormViewModel = hiltViewModel()
) {
    val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()
    LaunchedEffect(formUiState.isSaved, formUiState.createdFormId) {
        if (formUiState.isSaved) {
            onFormSaved(formUiState.createdFormId)
        }
    }
    Row {
        TextField(
            value = formUiState.name,
            onValueChange = viewModel::updateName,
            isError = formUiState.nameErrorMessage != null,
            supportingText = {
                if (formUiState.nameErrorMessage != null) {
                    Text(formUiState.nameErrorMessage!!)
                }
            },
            enabled = !formUiState.isLoading,
            label = { Text("Form name") }
        )
        Button(onClick = viewModel::saveForm, enabled = !formUiState.isLoading) {
            Text(formUiState.actionLabel)
        }
    }
}