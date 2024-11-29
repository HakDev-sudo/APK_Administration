package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterSheet(
    categories: Map<Int, String>,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit,
    onApplyFilter: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Filtrar por Categoría", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Listar categorías con RadioButton
        categories.forEach { (id, name) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategorySelected(id) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedCategory == id,
                    onClick = { onCategorySelected(id) }
                )
                Text(name, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Botón para aplicar filtro
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onApplyFilter, modifier = Modifier.align(Alignment.End)) {
            Text("Aplicar Filtro")
        }
    }
}
