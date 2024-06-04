package ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import kotlinqrcodeproject.composeapp.generated.resources.Res
import kotlinqrcodeproject.composeapp.generated.resources.profile_photo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.elements.TransactionCard

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Home(navController: NavController) {
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
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("screen") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
                    }
                },
                backgroundColor = Color(0xFF4A90E2),
                contentColor = Color.White,
                elevation = 4.dp
            )

            // User Card with Details
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(resource = Res.drawable.profile_photo),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Nome Cognome", fontWeight = FontWeight.Bold)
                        Text("Numero Conto: 1234-5678-9012")
                        Text("Stato Attuale: Attivo")
                        Text("Ammontare: € 10,000")
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
                items(20) { index ->
                    TransactionCard(transaction = "Transazione $index", amount = "€${index * 100}", index)
                }
            }
        }
    }
}

