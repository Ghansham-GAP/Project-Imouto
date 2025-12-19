package com.example.iiph.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.iiph.model.User

@Composable
fun UserManagementCard(user: User) {
    Card {
        Row {
            Column {
                Text(text = user.name)
                Text(text = user.email)
            }
        }
    }
}