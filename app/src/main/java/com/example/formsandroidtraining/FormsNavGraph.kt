package com.example.formsandroidtraining

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.formsandroidtraining.FormsDestinationsArgs.FORM_ID_ARG
import com.example.formsandroidtraining.add_edit_question.CreateOrEditQuestionScreen
import com.example.formsandroidtraining.create_edit_form.CreateOrEditFormScreen
import com.example.formsandroidtraining.edit_questions_list.EditQuestionsListScreen
import com.example.formsandroidtraining.forms.FormsScreen

@Composable
fun FormsNavGraph(
    navController: NavHostController = rememberNavController(),
    navActions: FormsNavigationActions = remember(navController) {
        FormsNavigationActions(navController)
    },
    startDestination: String = FormsDestinations.FORMS_ROUTE,
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(FormsDestinations.FORMS_ROUTE) {
            FormsScreen(
                onCreateForm = {
                    navActions.navigateToCreateOrEditForm()
                },
                onFormOpen = {
                    navActions.navigateToEditQuestionsList(it)
                }
            )
        }
        composable(FormsDestinations.CREATE_OR_EDIT_QUESTION) {
            CreateOrEditQuestionScreen(
                onQuestionSaved = {
                    navController.popBackStack()
                }
            )
        }
        composable(FormsDestinations.CREATE_OR_EDIT_FORM) {
            CreateOrEditFormScreen(
                onFormSaved = { createdFormId: String? ->
                    if (createdFormId == null) {
                        navController.popBackStack()
                    } else {
                        navActions.navigateToEditQuestionsList(createdFormId, newForm = true)
                    }
                }
            )
        }
        composable(FormsDestinations.EDIT_QUESTIONS_LIST) {
            val formId = requireNotNull(it.arguments?.getString(FORM_ID_ARG))
            EditQuestionsListScreen(
                onEditForm = {
                    navActions.navigateToCreateOrEditForm(formId)
                },
                onAddOrEditQuestion = {
                    navActions.navigateToCreateOrEditQuestion(formId, it)
                }
            )
        }
    }
}