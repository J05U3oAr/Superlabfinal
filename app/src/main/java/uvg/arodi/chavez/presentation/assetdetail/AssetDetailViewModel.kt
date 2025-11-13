package uvg.arodi.chavez.presentation.assetdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.arodi.chavez.data.model.Asset
import uvg.arodi.chavez.data.repository.CryptoRepository
import uvg.arodi.chavez.data.repository.Result
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AssetDetailState(
    val isLoading: Boolean = false,
    val asset: Asset? = null,
    val error: String? = null,
    val isFromCache: Boolean = false,
    val lastUpdateTimestamp: Long? = null
)

class AssetDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CryptoRepository(application)

    private val _state = MutableStateFlow(AssetDetailState())
    val state: StateFlow<AssetDetailState> = _state.asStateFlow()

    fun loadAsset(assetId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getAssetById(assetId)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        asset = result.data,
                        isFromCache = result.isFromCache,
                        lastUpdateTimestamp = result.timestamp,
                        error = null
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Error desconocido"
                    )
                }
                else -> {}
            }
        }
    }

    fun formatTimestamp(timestamp: Long?): String {
        if (timestamp == null) return ""
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}