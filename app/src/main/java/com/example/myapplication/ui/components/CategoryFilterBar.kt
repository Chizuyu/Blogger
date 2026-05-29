package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Category
import com.example.myapplication.ui.viewModel.PostViewModel

// UI Component: CategoryFilterBar.kt
@Composable
fun CategoryFilterBar(
    categories: List<Category>,
    selectedId: String?,
    onCategoryClick: (String?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Item "All" untuk reset filter
        item {
            FilterChip(
                selected = selectedId == null,
                onClick = { onCategoryClick(null) },
                label = { Text("All") }
            )
        }

        items(categories) { category ->
            FilterChip(
                selected = selectedId == category.id,
                onClick = { onCategoryClick(category.id) },
                label = { Text(category.name) }
            )
        }
    }
}