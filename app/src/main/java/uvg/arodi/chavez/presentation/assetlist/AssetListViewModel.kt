package uvg.arodi.chavez.presentation.assetlist

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

data class AssetListState(
    val isLoading: Boolean = false,
    val assets: List<Asset> = emptyList(),
    val error: String? = null,
    val isFromCache: Boolean = false,
    val lastUpdateTimestamp: Long? = null,
    val showSaveSuccess: Boolean = false
)

class AssetListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CryptoRepository(application)

    private val _state = MutableStateFlow(AssetListState())
    val state: StateFlow<AssetListState> = _state.asStateFlow()

    init {
        loadAssets()
    }

    fun loadAssets() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getAssets()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        assets = result.data,
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

    fun saveOffline() {
        viewModelScope.launch {
            try {
                val currentAssets = _state.value.assets
                if (currentAssets.isNotEmpty()) {
                    repository.saveAssetsOffline(currentAssets)
                    val timestamp = repository.getLastUpdateTimestamp()
                    _state.value = _state.value.copy(
                        showSaveSuccess = true,
                        lastUpdateTimestamp = timestamp,
                        isFromCache = true
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al guardar datos: ${e.message}"
                )
            }
        }
    }

    fun dismissSaveSuccess() {
        _state.value = _state.value.copy(showSaveSuccess = false)
    }

    fun formatTimestamp(timestamp: Long?): String {
        if (timestamp == null) return ""
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}