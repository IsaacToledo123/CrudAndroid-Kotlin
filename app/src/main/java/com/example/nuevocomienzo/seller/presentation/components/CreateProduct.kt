package com.example.nuevocomienzo.seller.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.nuevocomienzo.seller.data.model.RequestProduct
import java.io.ByteArrayOutputStream

@Composable
fun CreateProductScreen(createProductViewModel: CreateProductViewModel, id:Int, navController: NavController) {
    val name: String by createProductViewModel.name.observeAsState("")
    val costo: Double by createProductViewModel.costo.observeAsState(0.0)
    val cantidad: Int by createProductViewModel.cantidad.observeAsState(0)
    val imageUrl: String by createProductViewModel.imageUrl.observeAsState("")
    val idUser: Int by createProductViewModel.idUser.observeAsState(id)
    val success: Boolean by createProductViewModel.success.observeAsState(false)
    val error: String by createProductViewModel.error.observeAsState("")

    var nameInput by remember { mutableStateOf(name) }
    var costoInput by remember { mutableStateOf(costo.toString()) }
    var cantidadInput by remember { mutableStateOf(cantidad.toString()) }
    var imageBase64 by remember { mutableStateOf(imageUrl) }

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            createProductViewModel.onChangeImageUrl("Permiso denegado")
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val base64Image = uriToBase64(context, it)
            imageBase64 = base64Image
            createProductViewModel.onChangeImageUrl(base64Image)
        }
    }

    fun requestStoragePermission() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when (ContextCompat.checkSelfPermission(context, permission)) {
            android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                imagePickerLauncher.launch("image/*")
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nameInput,
            onValueChange = {
                nameInput = it
                createProductViewModel.onChangeName(it)
            },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = costoInput,
            onValueChange = {
                costoInput = it
                val costoValue = it.toDoubleOrNull() ?: 0.0
                createProductViewModel.onChangeCoto(costoValue)
            },
            label = { Text("Costo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cantidadInput,
            onValueChange = {
                cantidadInput = it
                val cantidadValue = it.toIntOrNull() ?: 0
                createProductViewModel.onChangeCantidad(cantidadValue)
            },
            label = { Text("Cantidad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { requestStoragePermission() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (imageBase64.isNotEmpty()) {
            Text("Imagen seleccionada (Base64): ${imageBase64.take(50)}...") // Muestra los primeros 50 caracteres
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val request = RequestProduct(
                    id = 0,
                    name = nameInput,
                    costo = costoInput.toDoubleOrNull() ?: 0.0,
                    cantidad = cantidadInput.toDoubleOrNull() ?: 0.0,
                    imageUrl = imageBase64,
                    idUser = idUser
                )
                createProductViewModel.createProduct(request)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nameInput.isNotBlank() && costoInput.isNotBlank() && cantidadInput.isNotBlank()
        ) {
            Text("Crear producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (success) {
            Text("Producto creado exitosamente", color = MaterialTheme.colorScheme.primary)
        }
        if (error.isNotEmpty()) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = { navController?.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

    }
}

fun uriToBase64(context: Context, uri: android.net.Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}