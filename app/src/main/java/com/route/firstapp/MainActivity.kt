package com.route.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.route.firstapp.ui.AppRoutes
import com.route.firstapp.ui.auth.signIn.SignInScreen
import com.route.firstapp.ui.auth.signUp.SignUpScreen
import com.route.firstapp.ui.categories.CategoriesScreen
import com.route.firstapp.ui.home.CategoriesTab
import com.route.firstapp.ui.home.FavoritesTab
import com.route.firstapp.ui.home.HomeScreen
import com.route.firstapp.ui.home.HomeTab
import com.route.firstapp.ui.home.HomeTabs
import com.route.firstapp.ui.home.ProfileTab
import com.route.firstapp.ui.productDetails.ProductDetailsScreen
import com.route.firstapp.ui.productsCatalog.ProductsCatalogScreen
import com.route.firstapp.ui.theme.FirstAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RouteEcommerceApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteEcommerceApp(){
    // 1. Create and remember the NavController
    val navController = rememberNavController()

    FirstAppTheme {
        Scaffold(
            contentColor = Color.White
        ){ _ ->
            AppNavHost(
                navController = navController,
                startDestination = AppRoutes.SignIn
            )
        }
    }

}

@Composable
fun AppNavHost(modifier: Modifier = Modifier,
               navController: NavHostController,
               startDestination: AppRoutes = AppRoutes.SignIn
               ) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<AppRoutes.SignIn> {
            SignInScreen(
                onNavigateToSignUp = {
                    navController.navigate(AppRoutes.SignUp)
                },
                onNavigateToHome = {
                    navController.navigate(AppRoutes.Home) {
                        popUpTo(AppRoutes.SignIn) { inclusive = true }
                    }
                },
            )
        }
        composable<AppRoutes.SignUp> {
            SignUpScreen(
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(AppRoutes.Home) {
                        popUpTo(AppRoutes.SignIn) { inclusive = true }
                    }
                },
            )
        }
        composable<AppRoutes.Home> {
            HomeScreen(
                appNavController = navController,
            )
        }
        composable<AppRoutes.ProductDetails> {
            ProductDetailsScreen()
        }
        composable<AppRoutes.ProductsCatalog> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoutes.ProductsCatalog>()
            ProductsCatalogScreen(
                categoryId = route.categoryId,
                subCategoryId = route.subCategoryId,
            )
        }

    }

}



