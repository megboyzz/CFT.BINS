package ru.megboyzz.bin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.megboyzz.bin.entity.BINInfo
import ru.megboyzz.bin.services.APIService
import ru.megboyzz.bin.ui.theme.MainTheme

@Composable
fun Int.AsString() = stringResource(this)

@Composable
fun Int.AsPainter() = painterResource(this)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(
                                text = R.string.app_name.AsString(),
                                fontWeight = FontWeight.W400,
                                fontSize = 15.sp
                            ) }
                        )
                    }
                ) {
                    MainScreen()
                }
            }
        }
    }
}