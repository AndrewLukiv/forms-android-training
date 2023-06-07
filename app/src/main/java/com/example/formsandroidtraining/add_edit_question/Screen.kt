package com.example.formsandroidtraining.add_edit_question

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditQuestionScreen(
    onQuestionSaved: () -> Unit,
    viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionUiState by viewModel.questionUiState.collectAsStateWithLifecycle()
    LaunchedEffect(questionUiState.isSaved) {
        if (questionUiState.isSaved) {
            onQuestionSaved()
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.saveQuestion() }) {
                Icon(Icons.Filled.Done, contentDescription = "Save question")
            }
        }
    ) {
        Column {
            TextField(value = questionUiState.title,
                onValueChange = viewModel::updateTitle,
                label = { Text("Title") })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = questionUiState.isRequired,
                    onCheckedChange = viewModel::updateIsRequired
                )
                Text(text = "Required question")
            }
            questionUiState.types.forEach { it ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = questionUiState.type == it.type,
                        onClick = {
                            viewModel.changeQuestionType(it.type)
                        },
                    )
                    Text(it.label)
                }
            }
            val updaterContext = remember(viewModel) {
                QuestionSettingsUpdaterContext {
                    viewModel.updateQuestionTypeData(it)
                }
            }
            with(updaterContext) {
                when (questionUiState.settings) {
                    is QuestionTypeUiState.Number ->
                        NumberQuestionSettings(questionUiState.settings as QuestionTypeUiState.Number)

                    is QuestionTypeUiState.Text ->
                        TextQuestionSettings(questionUiState.settings as QuestionTypeUiState.Text)
                }
            }
        }
    }
}

@Immutable
fun interface QuestionSettingsUpdaterContext : (QuestionTypeUpdater) -> Unit {
    fun <T> ((T) -> QuestionTypeUpdater).callback(): (T) -> Unit =
        {
            this@QuestionSettingsUpdaterContext(this(it))
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSettingsUpdaterContext.TextQuestionSettings(
    textQuestionUiState: QuestionTypeUiState.Text
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = textQuestionUiState.isSingleLine,
            onCheckedChange = TextQuestionSettings::updateSingleLine.callback(),
        )
        Text(text = "Single line")
    }
    Row {
        Checkbox(
            checked = textQuestionUiState.useMin,
            onCheckedChange = TextQuestionSettings::updateUseMin.callback(),
        )
        TextField(
            value = textQuestionUiState.min,
            enabled = textQuestionUiState.useMin,
            onValueChange = TextQuestionSettings::updateMin.callback(),
            isError = textQuestionUiState.minErrorMessage != null,
            supportingText = {
                if (textQuestionUiState.minErrorMessage != null && textQuestionUiState.useMin) {
                    Text(textQuestionUiState.minErrorMessage)
                }
            },
            label = { Text(text = "Minimum characters") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

    }
    Row {
        Checkbox(
            checked = textQuestionUiState.useMax,
            onCheckedChange = TextQuestionSettings::updateUseMax.callback()
        )
        TextField(
            value = textQuestionUiState.max,
            enabled = textQuestionUiState.useMax,
            onValueChange = TextQuestionSettings::updateMax.callback(),
            isError = textQuestionUiState.maxErrorMessage != null,
            supportingText = {
                if (textQuestionUiState.maxErrorMessage != null && textQuestionUiState.useMax) {
                    Text(textQuestionUiState.maxErrorMessage)
                }
            },
            label = { Text(text = "Maximum characters") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSettingsUpdaterContext.NumberQuestionSettings(
    numberQuestionUiState: QuestionTypeUiState.Number,
) {
    Row {
        Checkbox(
            checked = numberQuestionUiState.useMin,
            onCheckedChange = NumberQuestionSettings::updateUseMin.callback()
        )
        TextField(
            value = numberQuestionUiState.min,
            enabled = numberQuestionUiState.useMin,
            onValueChange = NumberQuestionSettings::updateMin.callback(),
//            isError = wrongMinLength,
            label = { Text(text = "Minimum value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

    }
    Row {
        Checkbox(
            checked = numberQuestionUiState.useMax,
            onCheckedChange = NumberQuestionSettings::updateUseMax.callback()
        )
        TextField(
            value = numberQuestionUiState.max,
            enabled = numberQuestionUiState.useMax,
            onValueChange = NumberQuestionSettings::updateMax.callback(),
//            isError = wrongMaxLength,
            label = { Text(text = "Maximum value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

    }
}

