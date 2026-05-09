package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.SetupNavGraph
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Login(modifier: Modifier = Modifier) {
//    var name by remember { mutableStateOf("") }
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.auth_saya),
//                contentDescription = "Logo Aplikasi",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//
//            Text(
//                text = "Login",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .align(Alignment.Start)
//            )
//            Text(
//                text = "Please login using your username and password to continue!",
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Gray,
//                modifier = Modifier
//                    .align(Alignment.Start)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                value = name,
//                onValueChange = { newText ->
//                    name = newText
//                },
//                label = { Text(text = "Username") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//            OutlinedTextField(
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                value = name,
//                onValueChange = { newText ->
//                    name = newText
//                },
//                label = { Text(text = "Password") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//
//                },
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2196F3)
//                )
//
//            ) {
//                Text(text = "Login")
//                CornerRadius(4)
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Don't Have an Account?",
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(
//                    text = "Sign In!",
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Blue,
//                    modifier = Modifier.clickable{
//
//                    }
//                )
//            }
//        }
//    }
//
//

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
        SetupNavGraph(navController = navController)
        }
    }
}