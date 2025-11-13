package uvg.arodi.chavez.data.repository

import android.content.Context
import kotlinx.coroutines.flow.first
import uvg.arodi.chavez.data.local.AppDatabase
import uvg.arodi.chavez.data.local.PreferencesManager
import uvg.arodi.chavez.data.local.toAsset
import uvg.arodi.chavez.data.local.toEntity
import uvg.arodi.chavez.data.model.Asset
import uvg.arodi.chavez.data.network.CryptoApiService

sealed class Result<out T> {
    data class Success<T>(val data: T, val isFromCache: Boolean = false, val timestamp: Long? = null) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class CryptoRepository(context: Context) {
    private val apiService = CryptoApiService()
    private val database = AppDatabase.getDatabase(context)
    private val assetDao = database.assetDao()
    private val preferencesManager = PreferencesManager(context)

    suspend fun getAssets(forceRefresh: Boolean = false): Result<List<Asset>> {
        return try {
            if (!forceRefresh) {
                // Intentar obtener de internet primero
                try {
                    val response = apiService.getAssets()
                    Result.Success(response.data, isFromCache = false)
                } catch (e: Exception) {
                    // Si falla, intentar obtener de cache
                    val cachedAssets = assetDao.getAllAssets()
                    if (cachedAssets.isNotEmpty()) {
                        val timestamp = assetDao.getSavedTimestamp()
                        Result.Success(
                            cachedAssets.map { it.toAsset() },
                            isFromCache = true,
                            timestamp = timestamp
                        )
                    } else {
                        Result.Error(e)
                    }
                }
            } else {
                val response = apiService.getAssets()
                Result.Success(response.data, isFromCache = false)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getAssetById(id: String): Result<Asset> {
        return try {
            // Intentar obtener de internet primero
            try {
                val response = apiService.getAssetById(id)
                Result.Success(response.data, isFromCache = false)
            } catch (e: Exception) {
                // Si falla, intentar obtener de cache
                val cachedAsset = assetDao.getAssetById(id)
                if (cachedAsset != null) {
                    val timestamp = assetDao.getSavedTimestamp()
                    Result.Success(
                        cachedAsset.toAsset(),
                        isFromCache = true,
                        timestamp = timestamp
                    )
                } else {
                    Result.Error(e)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun saveAssetsOffline(assets: List<Asset>) {
        val currentTimestamp = System.currentTimeMillis()
        assetDao.deleteAllAssets()
        assetDao.insertAssets(assets.map { it.toEntity(currentTimestamp) })
        preferencesManager.saveLastUpdateTimestamp(currentTimestamp)
    }

    suspend fun getLastUpdateTimestamp(): Long? {
        return preferencesManager.lastUpdateTimestamp.first()
    }
}