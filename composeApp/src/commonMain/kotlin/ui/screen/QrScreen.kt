package ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinqrcodeproject.composeapp.generated.resources.Res
import kotlinqrcodeproject.composeapp.generated.resources.scanning_icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun QrScreen(navController: NavController) {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "QR Code Project",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                backgroundColor = Color(0xFF4A90E2),
                contentColor = Color.White,
                elevation = 4.dp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Welcome!",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(all = 15.dp),
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black
                    )
                    Text(
                        text = "What do you want to do?!",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(all = 15.dp),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black
                    )

                    Image(
                        painter = painterResource(
                            resource = Res.drawable.scanning_icon
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 20.dp)
                            .width(160.dp)
                            .height(200.dp)
                    )
                }
            }
            // Second box at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { navController.navigate("scanner") },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF4A90E2),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 20.dp),
                        ) {
                            Text(
                                text = "Scan QR",
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = {
                                navController.navigate("generator")
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF4A90E2),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 20.dp),
                        ) {
                            Text(
                                text = "Generate QR",
                                modifier = Modifier.background(Color.Transparent)
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}