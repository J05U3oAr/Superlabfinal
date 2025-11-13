package uvg.arodi.chavez.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse(
    val data: List<Asset>,
    val timestamp: Long
)

@Serializable
data class AssetDetailResponse(
    val data: Asset,
    val timestamp: Long
)

@Serializable
data class Asset(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String?,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String?,
    val explorer: String?
)