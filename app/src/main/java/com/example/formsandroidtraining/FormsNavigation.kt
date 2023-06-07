package com.example.formsandroidtraining

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.formsandroidtraining.FormsDestinationsArgs.FORM_ID_ARG
import com.example.formsandroidtraining.FormsDestinationsArgs.QUESTION_ID_ARG
import com.example.formsandroidtraining.FormsPathParts.CREATE_OR_EDIT
import com.example.formsandroidtraining.FormsPathParts.EDIT
import com.example.formsandroidtraining.FormsPathParts.FORMS
import com.example.formsandroidtraining.FormsPathParts.QUESTIONS


private object FormsPathParts {
    const val FORMS = "forms"
    const val QUESTIONS = "questions"
    const val EDIT = "edit"
    const val CREATE_OR_EDIT = "createOrEdit"
}

object FormsDestinationsArgs {
    const val FORM_ID_ARG = "formId"
    const val QUESTION_ID_ARG = "questionId"
}

object FormsDestinations {
    const val FORMS_ROUTE = FORMS
    const val EDIT_QUESTIONS_LIST = "$FORMS/{$FORM_ID_ARG}/$QUESTIONS/$EDIT"
    const val CREATE_OR_EDIT_QUESTION =
        "$FORMS/{$FORM_ID_ARG}/$QUESTIONS/$CREATE_OR_EDIT?$QUESTION_ID_ARG={$QUESTION_ID_ARG}"
    const val CREATE_OR_EDIT_FORM = "$FORMS/$CREATE_OR_EDIT?$FORM_ID_ARG={$FORM_ID_ARG}"
}

class FormsNavigationActions(private val navController: NavHostController) {
    fun navigateToEditQuestionsList(formId: String, newForm: Boolean = false) {
        navController.navigate("$FORMS/$formId/$QUESTIONS/$EDIT") {
            if (newForm) {
                popUpTo(FormsDestinations.FORMS_ROUTE)
            }
        }
    }

    fun navigateToCreateOrEditForm(formId: String? = null) {
        navController.navigate("$FORMS/$CREATE_OR_EDIT".let {
            if (formId == null) it else "$it?$FORM_ID_ARG=$formId"
        })
    }

    fun navigateToCreateOrEditQuestion(formId: String, questionId: String?) {
        navController.navigate("$FORMS/$formId/$QUESTIONS/$CREATE_OR_EDIT".let {
            if (questionId == null) it else "$it?$QUESTION_ID_ARG=$questionId"
        })
    }
}