package com.bignerdranch.android.blognerdranch.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.bignerdranch.android.blognerdranch.controller.theme.BBNExampleTheme
import com.bignerdranch.android.blognerdranch.viewmodel.BlogViewModelImpl

class MainActivityCompose : ComponentActivity() {
    private val blogViewModel: BlogViewModelImpl by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BBNExampleTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
////                    Greeting("Android")
//                }

                MainNavGraph( blogViewModel = blogViewModel )
            }
        }
    }


}