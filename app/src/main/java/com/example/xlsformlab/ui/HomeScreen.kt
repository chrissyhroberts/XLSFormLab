package com.example.xlsformlab.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityRegistry
import com.example.xlsformlab.ui.components.CapabilityCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("XLSForm Lab Workbench")
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(CapabilityCategory.entries) { category ->
                CapabilityCategoryCard(category = category)
            }
        }
    }
}

@Composable
private fun CapabilityCategoryCard(
    category: CapabilityCategory
) {
    val capabilities = CapabilityRegistry.byCategory(category)

    var expanded by remember {
        mutableStateOf(true)
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Text(
                    text = if (expanded) {
                        "▼ ${category.name}"
                    } else {
                        "▶ ${category.name}"
                    },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = capabilities.size.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                if (capabilities.isEmpty()) {
                    Text("No capabilities installed.")
                }

                capabilities.forEach { capability ->
                    CapabilityCard(
                        capability = capability,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}