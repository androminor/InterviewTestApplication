package com.example.interviewtestapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.userlist.UserListScreen
import com.example.interviewtestapplication.ui.theme.InterviewTestApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InterviewTestApplicationTheme {
                UserListScreen(
                    onUserClick = { userId ->
                        // navigate to detail

                    }
                )
            }
        }
    }
}

