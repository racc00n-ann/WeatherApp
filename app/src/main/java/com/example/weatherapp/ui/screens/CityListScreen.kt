package com.example.weatherapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CityListScreen(
    onCityClick: (String, String) -> Unit
) {
    val cities = listOf(
        Pair("Москва", "Moscow"),
        Pair("Санкт-Петербург", "Saint Petersburg"),
        Pair("Казань", "Kazan"),
        Pair("Сочи", "Sochi"),
        Pair("Ижевск", "Izhevsk"),
        Pair("Уфа", "Ufa"),
        Pair("Калининград", "Kaliningrad"),
        Pair("Норильск", "Norilsk"),
        Pair("Мурманск", "Murmansk")
    )

    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
    val formattedDate = currentDate.format(formatter)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6A11CB),
                        Color(0xFF2575FC),
                        Color(0xFF1488CC),
                        Color(0xFF2B32B2)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Прогноз погоды на\n$formattedDate",  // Перенос строки
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 32.sp  // Контроль межстрочного интервала
                    ),
                    textAlign = TextAlign.Center  // Выравнивание по центру
                )
            }


            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(cities) { city ->
                    CityCard(city, onCityClick)
                }
            }
        }
    }
}

@Composable
fun CityCard(city: Pair<String, String>, onCityClick: (String, String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 6.dp)
            .clickable { onCityClick(city.second, city.first) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = city.first,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}