package com.example.iiph.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.iiph.data.InternshipOpportunity
import com.example.iiph.data.PlacementOpportunity

@Composable
fun <T> AdminOpportunityCard(opportunity: T, onVerify: () -> Unit, onRemove: () -> Unit) {
    val (title, company, location, stipend, verified) = when (opportunity) {
        is InternshipOpportunity -> {
            listOf(opportunity.title, opportunity.company, opportunity.location, opportunity.stipend, opportunity.verified.toString())
        }
        is PlacementOpportunity -> {
            listOf(opportunity.title, opportunity.company, opportunity.location, opportunity.stipend, opportunity.verified.toString())
        }
        else -> {
            emptyList()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (verified == "true") Color.Green.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = company, style = MaterialTheme.typography.bodyMedium)
            Text(text = location, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stipend: $stipend", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = onVerify, enabled = verified == "false") {
                    Text("Verify")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onRemove) {
                    Text("Remove")
                }
            }
        }
    }
}
