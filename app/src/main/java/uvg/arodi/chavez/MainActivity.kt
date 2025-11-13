package uvg.arodi.chavez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uvg.arodi.chavez.navigation.AppNavigation
import uvg.arodi.chavez.ui.theme.Superlaboratorio_finalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Superlaboratorio_finalTheme {
                AppNavigation()
            }
        }
    }
}