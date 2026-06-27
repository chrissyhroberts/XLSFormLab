package com.example.xlsformlab.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.Capability

@Composable
fun CapabilityCard(
    capability: Capability,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = capability.manifest.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = capability.manifest.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${capability.manifest.status} • v${capability.manifest.version}",
                style = MaterialTheme.typography.labelMedium
            )

            ExpandableSection(
                title = "Demo",
                initiallyExpanded = true
            ) {
                capability.Demo()
            }

            ExpandableSection(
                title = "Settings"
            ) {
                SettingsRenderer(
                    settings = capability.settings
                )
            }

            ExpandableSection(
                title = "Help"
            ) {
                capability.Help()
            }

            ExpandableSection(
                title = "Diagnostics"
            ) {
                Text("ID: ${capability.manifest.id}")
                Text("Version: ${capability.manifest.version}")
                Text("Category: ${capability.manifest.category}")
                Text("Status: ${capability.manifest.status}")
            }
        }
    }
}