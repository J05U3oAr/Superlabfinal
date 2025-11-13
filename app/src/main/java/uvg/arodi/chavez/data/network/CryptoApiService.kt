package uvg.arodi.chavez.data.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import uvg.arodi.chavez.data.model.AssetDetailResponse
import uvg.arodi.chavez.data.model.AssetsResponse

class CryptoApiService {
    private val client = KtorClient.client

    suspend fun getAssets(): AssetsResponse {
        return client.get("assets").body()
    }

    suspend fun getAssetById(id: String): AssetDetailResponse {
        return client.get("assets/$id").body()
    }
}