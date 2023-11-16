package fr.eric.tp2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.eric.tp2.R
import fr.eric.tp2.viewModels.LoginViewModel


@Composable
fun ProfileScreen(loginViewModel: LoginViewModel = viewModel(),
                  modifier: Modifier = Modifier){
    var username by rememberSaveable {
        mutableStateOf("Test")
    }
    var password by rememberSaveable {
        mutableStateOf("Test")
    }

    val loginState by loginViewModel.uiState.collectAsState()
    val smallPadding = dimensionResource(id = R.dimen.small_padding)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(smallPadding)
    ) {
        LoginScreen(
            username = loginState.username,
            password = loginState.password,

            error = loginState.error,
            errorMessage = loginState.errorMessage,

            onUsernameChanged = {
                loginViewModel.changeUsername(it)
            },

            onPasswordChanged = {
                loginViewModel.changePassword(it)
            },
            onDone = { loginViewModel.login() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    username: String,
    password: String,
    error: Boolean,
    errorMessage: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
){
    val addingPadding = dimensionResource(id = R.dimen.medium_padding)
    var passwordVisible by remember { mutableStateOf(false) }


    Card(modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {

        Column(
            verticalArrangement = Arrangement.spacedBy(addingPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(addingPadding)
        ) {
            OutlinedTextField(
                value = username,
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = modifier.fillMaxWidth(),
                label = {Text(stringResource(R.string.username))},
                placeholder = {Text(stringResource(R.string.username))},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                onValueChange = onUsernameChanged)

            OutlinedTextField(value = password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

                isError = error,

                supportingText = {
                    if (error){
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },

                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    var description = if (passwordVisible)
                        stringResource(R.string.hide_password)
                    else stringResource(R.string.show_password)

                    IconButton(onClick = { passwordVisible  = !passwordVisible}) {
                        Icon(imageVector = image, description)
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = modifier.fillMaxWidth(),
                label = {Text(stringResource(R.string.password))},
                placeholder = {Text(stringResource(R.string.password))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(
                    onDone = {onDone()}
                ),
                onValueChange = onPasswordChanged
            )

            Button(onClick = onDone) {
                Text(text = "Login")
            }
        }

    }
}