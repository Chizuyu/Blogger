package com.example.myapplication.ui.screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.model.Category
import com.example.myapplication.ui.viewModel.PostViewModel
import com.example.myapplication.util.GlobalData
import com.example.myapplication.util.toMultipartBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(navController: NavController, viewModel: PostViewModel = viewModel(), postId: String) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // Launcher untuk Thumbnail
    val thumbPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.thumbnailUri = uri
        viewModel.thumbnailName = uri?.lastPathSegment ?: "Selected Thumbnail"
    }

    // Launcher untuk Image Content
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.imageContentUri = uri
        viewModel.imageContentName = uri?.lastPathSegment ?: "Selected Image"
    }

    LaunchedEffect(postId) {
        viewModel.setEditData(postId)
    }

    // Navigasi balik jika sukses update
    LaunchedEffect(viewModel.isCreateSuccess) {
        if (viewModel.isCreateSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Edit Post", color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3))
//            )
//        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Edit Post", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                "Keep your blogging profile up-to-date for a better experience",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Title Field
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Dropdown Category
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = viewModel.selectedCategory?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Text/Content Field
            OutlinedTextField(
                value = viewModel.content,
                onValueChange = { viewModel.content = it },
                label = { Text("Text...") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Thumbnail Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.thumbnailName,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { thumbPicker.launch("image/*") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("Select Thumbnail", fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            // Image Content Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.imageContentName,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { imagePicker.launch("image/*") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("Select Image", fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(32.dp))

            if (viewModel.errorMessage.isNotEmpty()) {
                Text(viewModel.errorMessage, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
            }

            // Save Button
            Button(
                onClick = {
                    // Panggil fungsi Update yang kita buat di ViewModel
                    viewModel.updateFullPost(context, postId)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                enabled = !viewModel.isLoading && viewModel.title.isNotEmpty()
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Update Post", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}