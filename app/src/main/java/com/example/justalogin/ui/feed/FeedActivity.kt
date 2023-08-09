package com.example.justalogin.ui.feed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.justalogin.ui.login.ui.theme.JustALogin
import com.example.data.models.parcelable.UserInfoParcel
import com.example.data.utlis.findActivity
import com.example.data.utlis.retrieveParcelable

class FeedActivity : ComponentActivity() {
    private var userInfoParcel: UserInfoParcel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalContext.current.findActivity()?.intent?.let {
                userInfoParcel = it.retrieveParcelable(USER_INFO)
            }

            JustALogin {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(userInfoParcel?.userName ?: "You")
                }
            }
        }
    }

    companion object {
        const val USER_INFO = "userInfo"
    }
}

@Composable
fun Greeting(name: String) {
    Box {
        Text(
            text = "Hello $name!",
            modifier = Modifier.align(Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JustALogin {
        Greeting("Android")
    }
}