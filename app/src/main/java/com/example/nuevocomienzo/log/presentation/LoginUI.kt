package com.example.nuevocomienzo.log.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nuevocomienzo.core.services.ServicesFireBase


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (Any?) -> Unit
) {
    val username: String by loginViewModel.username.observeAsState("")
    val password: String by loginViewModel.password.observeAsState("")
    val loginState: LoginState by loginViewModel.loginState.observeAsState(LoginState.Initial)
    var isPasswordVisible by remember { mutableStateOf(false) }
    val fcmToken: String? by loginViewModel.fcmToken.observeAsState()
    val installationId: String? by loginViewModel.installationId.observeAsState()

    val backgroundColor = Color(0xFFF5F5F7)
    val primaryColor = Color(0xFF000000)
    val secondaryColor = Color(0xFF86868B)
    val accentColor = Color(0xFF007AFF)
    val surfaceColor = Color(0xFFFFFFFF)
    val errorColor = Color(0xFFFF3B30)
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            ServicesFireBase.initializeFirebaseMessaging()
            val userType = (loginState as LoginState.Success).data
            onLoginSuccess(userType.data)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome Back",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Text(
                text = "Sign in to continue",
                fontSize = 16.sp,
                color = secondaryColor,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { loginViewModel.onChangeUsername(it) },
                        label = { Text("Username", color = secondaryColor) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0xFFE5E5E5),
                            focusedTextColor = primaryColor,
                            unfocusedTextColor = primaryColor
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Person,
                                contentDescription = "Username Icon",
                                tint = secondaryColor
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = loginState is LoginState.Error && username.isBlank()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { loginViewModel.onChangePassword(it) },
                        label = { Text("Password", color = secondaryColor) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0xFFE5E5E5),
                            focusedTextColor = primaryColor,
                            unfocusedTextColor = primaryColor
                        ),
                        visualTransformation = if (isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Lock,
                                contentDescription = "Password Icon",
                                tint = secondaryColor
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible)
                                        Icons.Rounded.Visibility
                                    else
                                        Icons.Rounded.VisibilityOff,
                                    contentDescription = if (isPasswordVisible)
                                        "Hide password"
                                    else
                                        "Show password",
                                    tint = secondaryColor
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = loginState is LoginState.Error && password.isBlank()
                    )
                }
            }

            if (loginState is LoginState.Error) {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = errorColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { loginViewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    disabledContainerColor = accentColor.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = loginState !is LoginState.Loading && username.isNotBlank() && password.isNotBlank()
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Sign In",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            TextButton(
                onClick = onNavigateToRegister,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = accentColor
                )
            ) {
                Text(
                    text = "Don't have an account? Sign Up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}