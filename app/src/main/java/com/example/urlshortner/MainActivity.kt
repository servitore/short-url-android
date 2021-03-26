package com.example.urlshortner

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urlshortner.models.Link
import com.example.urlshortner.models.dto.LinkDto
import com.example.urlshortner.models.mappers.responseToLink
import com.example.urlshortner.ui.theme.URLShortnerTheme
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            URLShortnerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android", viewModel::request)
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val link: Link = Link()
    fun request(url: String, shortLink: MutableState<String>, context: Context) {
        // SOLID
        // Guideline Kotlin code style: https://developer.android.com/kotlin/style-guide
        fun toastMessage(msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        viewModelScope.launch {
            try {
                val res = ApiClient.apiService.createLink(
                    LinkDto(long_url = url)
                )
                val r = res?.responseToLink()
                link.apply {
                    link = r?.link
                }
                shortLink.value = r?.link ?: ""
                toastMessage("Link created successfully")
            } catch (err: Exception) {
                println("Request error $err")
                toastMessage("Something went wrong")
            }
        }
    }
}


@Composable
fun Greeting(
    name: String,
    request: ((text: String, shortLink: MutableState<String>, context: Context) -> Unit)
) {
    val shortLink = remember { mutableStateOf("") }
    val text = remember { mutableStateOf(value = "") }
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Short a link $name!")
        if (shortLink.value !== "") SelectionContainer {
            Text(text = "Your link: ${shortLink.value}")
        }
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            Modifier.padding(horizontal = 0.dp, vertical = 14.dp)
        )
        Button(onClick = {
            request(text.value, shortLink, context)
        }) {
            Text("Get a link")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    URLShortnerTheme {
//        Greeting("Android")
//    }
//}