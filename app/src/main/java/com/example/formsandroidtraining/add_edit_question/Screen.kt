package com.example.formsandroidtraining.add_edit_question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextQuestionSettings(viewModel: QuestionViewModel= hiltViewModel()) {
    val questionUiState by viewModel.questionUiState.collectAsStateWithLifecycle()
    val settingsUiState by viewModel.typeUiState.collectAsStateWithLifecycle()
    val questionTypes = remember {
        QuestionTypeUiState.getTypes()
    }
    Column {
        TextField(
            value = questionUiState.title,
            onValueChange = viewModel::updateTitle,
            label = { Text("Title") }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = questionUiState.isRequired,
                onCheckedChange = viewModel::updateIsRequired
            )
            Text(text = "Required question")
        }
        questionTypes.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = it.isInstance(settingsUiState),
                    onClick = { viewModel.updateQuestionTypeData(it.defaultValue) }
                )
                Text(it.label)
            }
        }
        val onUpdate = remember {
            UpdatableQuestionSettingsUi { updater: QuestionTypeUpdater ->
                viewModel.updateQuestionTypeData(updater)
            }
        }
        settingsUiState.QuestionTypeSettings(onUpdate)

//        when (typeUiState) {
//            is QuestionTypeUiState.Number -> NumberQuestionSettings(
//                typeUiState as QuestionTypeUiState.Number,
//                onUpdate
//            )
//
//            is QuestionTypeUiState.Text -> TextQuestionSettings(
//                typeUiState as QuestionTypeUiState.Text,
//                onUpdate
//            )
//        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatableQuestionSettingsUi.TextQuestionSettings(
    textQuestionUiState: QuestionTypeUiState.Text,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = textQuestionUiState.isSingleLine,
            onCheckedChange = textQuestionUiState::updateSingleLine.callback()
        )
        Text(text = "Single line")
    }
    Row {
        Checkbox(
            checked = textQuestionUiState.useMin,
            onCheckedChange = textQuestionUiState::updateUseMin.callback()
        )
        TextField(
            value = textQuestionUiState.min,
            enabled = textQuestionUiState.useMin,
            onValueChange = textQuestionUiState::updateMin.callback(),
//            isError = wrongMinLength,
            label = { Text(text = "Minimum characters") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

    }
    Row {
        Checkbox(
            checked = textQuestionUiState.useMax,
            onCheckedChange = textQuestionUiState::updateUseMax.callback()
        )
        TextField(
            value = textQuestionUiState.max,
            enabled = textQuestionUiState.useMax,
            onValueChange = textQuestionUiState::updateMax.callback(),
//            isError = wrongMaxLength,
            label = { Text(text = "Maximum characters") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatableQuestionSettingsUi.NumberQuestionSettings(
    numberQuestionUiState: QuestionTypeUiState.Number,
) {
    Row {
        Checkbox(
            checked = numberQuestionUiState.useMin,
            onCheckedChange = numberQuestionUiState::updateUseMin.callback()
        )
        TextField(
            value = numberQuestionUiState.min,
            enabled = numberQuestionUiState.useMin,
            onValueChange = numberQuestionUiState::updateMin.callback(),
//            isError = wrongMinLength,
            label = { Text(text = "Minimum value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

    }
    Row {
        Checkbox(
            checked = numberQuestionUiState.useMax,
            onCheckedChange = numberQuestionUiState::updateUseMax.callback()
        )
        TextField(
            value = numberQuestionUiState.max,
            enabled = numberQuestionUiState.useMax,
            onValueChange = numberQuestionUiState::updateMax.callback(),
//            isError = wrongMaxLength,
            label = { Text(text = "Maximum value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

    }
}

