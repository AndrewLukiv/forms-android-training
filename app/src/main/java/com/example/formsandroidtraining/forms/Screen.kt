package com.example.formsandroidtraining.forms

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.immutableListOf

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormsScreen(
    onFormOpen: (String) -> Unit,
    onCreateForm: () -> Unit,
    viewModel: FormsViewModel = hiltViewModel()
) {
    val forms by viewModel.forms.collectAsStateWithLifecycle(initialValue = emptyList())
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onCreateForm) {
            Icon(Icons.Filled.Add, contentDescription = "Create form")
        }
    }) {
        Column {
            if (forms.isEmpty()) {
                Text("You don't have created forms")
            } else {
                LazyColumn {
                    forms.forEach {
                        item(it.id) {
                            Text(
                                text = it.name,
                                modifier = Modifier.clickable {
                                    onFormOpen(it.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}