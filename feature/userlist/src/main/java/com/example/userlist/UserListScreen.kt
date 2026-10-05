package com.example.userlist

import android.R
import android.text.TextUtils.isEmpty
import android.util.Log.i
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/*Headling Text on the top - Users
* Lazycolumn
* and also retry button
* Column(horizontal stack) - Text - Box(overlap) - Row (vertical stack)*/



@Composable
fun UserListScreen(
    onUserClick: (Int) -> Unit,//retry
    viewmodel: UserListViewmodel = hiltViewModel()
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Users",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                //first error and retry button

                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.data.isEmpty() -> {
                    Text("No User found")
                }

                state.errorMessage != null -> {
                    //We want to show the error message and button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewmodel.loadUsers() }) {
                            Text(text = "Retry")
                        }
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.data, key = { it.id }) { users ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUserClick(users.id)
                                    }) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = users.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = users.username,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = users.email,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = users.city,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                        }
                    }
                }
            }

        }
    }
}
