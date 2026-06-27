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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityRegistry

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

                CapabilityCategoryCard(category)

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

                    text =
                    if (expanded)
                        "▼ ${category.name}"
                    else
                        "▶ ${category.name}",

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

                    HorizontalDivider()

                    Spacer(Modifier.height(12.dp))

                    Text(

                        capability.manifest.name,

                        style = MaterialTheme.typography.titleMedium,

                        fontWeight = FontWeight.Bold

                    )

                    Text(capability.manifest.description)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Developer",
                        fontWeight = FontWeight.Bold
                    )

                    Text("ID: ${capability.manifest.id}")
                    Text("Version: ${capability.manifest.version}")
                    Text("Category: ${capability.manifest.category}")
                    Text("Status: ${capability.manifest.status}")

                    Spacer(Modifier.height(16.dp))

                    capability.Demo()

                    Spacer(Modifier.height(16.dp))

                }

            }

        }

    }

}