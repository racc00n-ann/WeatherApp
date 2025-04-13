package com.example.weatherapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.models.CityFromNet
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.repositories.CityRepository
import com.example.weatherapp.data.repositories.WeatherRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TopAppBarDefaults


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    cityRepository: CityRepository,
    weatherRepository: WeatherRepository,
    cityName: String,
    cityRu: String,
    navigateBack: () -> Unit
) {
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var cityCoordinates by remember { mutableStateOf<CityFromNet?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(cityName) {
        isLoading = true
        val cityResponse = cityRepository.getCityByName(cityName)
        cityCoordinates = cityResponse.firstOrNull { it.name == cityName }

        cityCoordinates?.let {
            val weatherResponse = weatherRepository.getWeather(it.latitude, it.longitude)
            weatherData = weatherResponse
        }

        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cityRu,
                            style = MaterialTheme.typography.headlineSmall.copy(color = Color.Black),
                            modifier = Modifier.padding(end = 48.dp) // Компенсируем ширину кнопки назад
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = Color.Black
                )
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black
                )
            } else if (weatherData != null) {
                WeatherContent(weatherData = weatherData!!)
            } else {
                Text(
                    text = "Ошибка загрузки данных",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherContent(weatherData: WeatherResponse) {
    val hourlyData = weatherData.hourly
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        items(hourlyData.time.zip(hourlyData.temperature2m)) { (time, temp) ->
            WeatherHourCard(time = time, temperature = temp)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherHourCard(time: String, temperature: Double) {
    val formattedTime = formatDateTime(time)

    // Цвет фона в зависимости от температуры
    val cardColor = when {
        temperature > 30 -> Color(0xCCE53935)  // Очень жарко
        temperature > 25 -> Color(0xCCFF7043)  // Жарко
        temperature > 20 -> Color(0xCCFFA726)  // Тепло
        temperature > 15 -> Color(0xCCFFCA28)  // Умеренно тепло
        temperature > 10 -> Color(0xCC66BB6A)  // Свежо
        temperature > 5  -> Color(0xCC42A5F5)  // Прохладно
        temperature > 0  -> Color(0xCC29B6F6)  // Легкий мороз
        temperature > -10 -> Color(0xCC81D4FA) // Мороз
        temperature > -20 -> Color(0xCCB3E5FC) // Сильный мороз
        else -> Color(0xCCE1F5FE)   // Экстремальный холод
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Время с иконкой часов
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🕒 ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 24.sp,
                    color = Color.White
                )
                Text(
                    text = formattedTime,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }

            // Температура
            Text(
                text = "%.1f°C".format(temperature),
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTime: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val parsedDate = LocalDateTime.parse(dateTime, formatter)
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        parsedDate.format(outputFormatter)
    } catch (e: Exception) {
        "Неизвестное время"
    }
}