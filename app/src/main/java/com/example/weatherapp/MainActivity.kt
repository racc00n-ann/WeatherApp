package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.api.CityClient
import com.example.weatherapp.data.api.WeatherClient
import com.example.weatherapp.data.repositories.CityRepository
import com.example.weatherapp.data.repositories.WeatherRepository
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.screens.CityListScreen
import com.example.weatherapp.ui.screens.WeatherScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppTheme {
                WeatherAppNavigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "cityList") {
        composable("cityList") {
            CityListScreen(
                onCityClick = { cityName, cityRu ->
                    navController.navigate("weatherScreen/$cityName/$cityRu")
                }
            )
        }
        composable("weatherScreen/{cityName}/{cityRu}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            val cityRu = backStackEntry.arguments?.getString("cityRu") ?: ""
            WeatherScreen(
                cityRepository = CityRepository(CityClient.instance),
                weatherRepository = WeatherRepository(WeatherClient.instance),
                cityName = cityName,
                cityRu = cityRu,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}