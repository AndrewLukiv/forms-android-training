package com.example.formsandroidtraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.formsandroidtraining.add_edit_question.QuestionTypeUiState
import com.example.formsandroidtraining.add_edit_question.TextQuestionSettings
import com.example.formsandroidtraining.ui.theme.FormsAndroidTrainingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v1: QuestionTypeUiState = QuestionTypeUiState.Text()
        val v2: QuestionTypeUiState = QuestionTypeUiState.Text()
        println(v1 == v2)
        setContent {
            FormsAndroidTrainingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormsNavGraph()
                }
            }
        }
    }
}