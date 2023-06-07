package com.example.formsandroidtraining.edit_questions_list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionsListScreen(
    onEditForm: (String) -> Unit,
    onAddOrEditQuestion: (String?) -> Unit,
    viewModel: EditQuestionsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        initialValue = EditQuestionsListUiState.Loading
    )
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { onAddOrEditQuestion(null) }) {
            Icon(Icons.Filled.Add, contentDescription = "Add question")
        }
    }) {
        if (uiState is EditQuestionsListUiState.Loaded) {
            val loadedUiState = uiState as EditQuestionsListUiState.Loaded
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(loadedUiState.form.name, style = MaterialTheme.typography.headlineMedium)
                    IconButton(onClick = { onEditForm(loadedUiState.form.id) }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit form name")
                    }
                }
                LazyColumn {
                    loadedUiState.questions.forEach {
                        item(it.id) {
                            Text(
                                it.title,
                                modifier = Modifier.clickable { onAddOrEditQuestion(it.id) })
                        }
                    }
                }
            }

        }
    }
}