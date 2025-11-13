package uvg.arodi.chavez.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uvg.arodi.chavez.presentation.assetdetail.AssetDetailScreen
import uvg.arodi.chavez.presentation.assetlist.AssetListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AssetListDestination
    ) {
        composable<AssetListDestination> {
            AssetListScreen(
                onNavigateToDetail = { assetId ->
                    navController.navigate(AssetDetailDestination(assetId))
                }
            )
        }

        composable<AssetDetailDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<AssetDetailDestination>()
            AssetDetailScreen(
                assetId = args.assetId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}