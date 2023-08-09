package com.example.justalogin.ui.login


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.justalogin.R
import com.example.justalogin.ui.complosable.DialogContent
import com.example.justalogin.ui.complosable.DrawMessageAlertDialog
import com.example.justalogin.ui.complosable.DrawProgressIndicator
import com.example.domain.repositories.LoginErrorType


@Composable
fun LoginProcessScreen(viewModel: LoginViewModel) {
    // Evaluates view state depending of login process flow
    when (val uiState = viewModel.viewState.collectAsStateWithLifecycle().value) {
        LoginViewState.Idle -> {}/*Do nothing cause is waiting*/

        is LoginViewState.Loading -> {
            DrawProgressIndicator()
        }

        is LoginViewState.Success -> {
            DrawMessageAlertDialog(dialogContent = DialogContent(
                title = "Success",
                description = stringResource(
                    id = R.string.legend_salutation,
                    uiState.userInfo.userName,
                    uiState.userInfo.occupationInfo.name
                ),
                onConfirm = {
                    viewModel.loginSuccess(uiState.userInfo)
                }
            ))
        }

        LoginViewState.EmptyField ->
            DrawMessageAlertDialog(
                DialogContent(
                    title = stringResource(id = R.string.legend_oops),
                    description = stringResource(id = R.string.legend_incomplete_fields),
                    onConfirm = {
                        viewModel.bePrepared()
                    })
            )

        is LoginViewState.Failure ->
            /* Here we can create specific DialogContent to show as we need
            * to many tries, suspended user, generic error...
            * For this PoC only generic is used */
            DrawMessageAlertDialog(
                DialogContent(
                    title = stringResource(id = R.string.legend_oops),
                    description = stringResource(id = R.string.legend_generic_error),
                    onConfirm = {
                        (uiState.error as? LoginErrorType)?.let { viewModel.handleError(it) }
                    })
            )
    }
}
