package com.example.nuevocomienzo.registro.presentation


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nuevocomienzo.registro.data.model.CreateUserRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val username: String by registerViewModel.username.observeAsState("")
    val nombre: String by registerViewModel.nombre.observeAsState("")
    val apellidos: String by registerViewModel.apellidos.observeAsState("")
    val email: String by registerViewModel.email.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val tipoUser: String by registerViewModel.tipoUser.observeAsState("")
    val success: Boolean by registerViewModel.success.observeAsState(false)
    val error: String by registerViewModel.error.observeAsState("")
    val isRegistered: Boolean by registerViewModel.isRegistered.observeAsState(false)
    var isPasswordVisible by remember { mutableStateOf(false) }
    var expandedTipoUser by remember { mutableStateOf(false) }
    val tiposUsuario = listOf("Cliente","vendedor")

    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp, top = 32.dp),
            textAlign = TextAlign.Center,
            text = "Create Account",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Username field
        TextField(
            value = username,
            onValueChange = { registerViewModel.onChangeUsername(it) },
            label = { Text("Username", color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("username", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = Color.Gray
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF0A84FF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && username.isNotEmpty()) {
                        registerViewModel.viewModelScope.launch {
                            registerViewModel.onFocusChanged()
                        }
                    }
                }
        )

        Spacer(Modifier.height(16.dp))

        // Nombre field
        TextField(
            value = nombre,
            onValueChange = { registerViewModel.onChangeNombre(it) },
            label = { Text("Nombre", color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("Alicia", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = Color.Gray
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF0A84FF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Apellidos field
        TextField(
            value = apellidos,
            onValueChange = { registerViewModel.onChangeApellidos(it) },
            label = { Text("Apellidos", color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("López García", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = Color.Gray
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF0A84FF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Email field
        TextField(
            value = email,
            onValueChange = { registerViewModel.onChangeEmail(it) },
            label = { Text("Email", color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("alicia@example.com", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF0A84FF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Password field
        TextField(
            value = password,
            onValueChange = { registerViewModel.onChangePassword(it) },
            label = { Text("Password", color = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("Password", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Lock Icon",
                    tint = Color.Gray
                )
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                cursorColor = Color(0xFF0A84FF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Tipo de usuario dropdown
        ExposedDropdownMenuBox(
            expanded = expandedTipoUser,
            onExpandedChange = { expandedTipoUser = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            TextField(
                value = tipoUser,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Usuario", color = Color.Gray) },
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoUser)
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Tipo Usuario Icon",
                        tint = Color.Gray
                    )
                },
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF1C1C1E),
                    focusedContainerColor = Color(0xFF1C1C1E),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color(0xFF0A84FF),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expandedTipoUser,
                onDismissRequest = { expandedTipoUser = false },
                modifier = Modifier.background(Color(0xFF1C1C1E))
            ) {
                tiposUsuario.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo, color = Color.White) },
                        onClick = {
                            registerViewModel.onChangeTipoUser(tipo)
                            expandedTipoUser = false
                        },
                        modifier = Modifier.background(Color(0xFF1C1C1E))
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color(0xFFFF453A),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val user = CreateUserRequest(
                    username = username,
                    nombre = nombre,
                    apellidos = apellidos,
                    email = email,
                    password = password,
                    tipoUser = tipoUser
                )
                registerViewModel.viewModelScope.launch {
                    registerViewModel.onClick(user)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),  // Eliminado el padding bottom que causaba el colapso
            enabled = success,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A84FF),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF0A84FF).copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Sign up",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}