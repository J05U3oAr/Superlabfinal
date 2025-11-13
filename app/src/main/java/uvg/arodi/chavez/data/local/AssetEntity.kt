package uvg.arodi.chavez.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import uvg.arodi.chavez.data.model.Asset

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey
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
    val explorer: String?,
    val savedTimestamp: Long
)

fun AssetEntity.toAsset(): Asset {
    return Asset(
        id = id,
        rank = rank,
        symbol = symbol,
        name = name,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd,
        volumeUsd24Hr = volumeUsd24Hr,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        vwap24Hr = vwap24Hr,
        explorer = explorer
    )
}

fun Asset.toEntity(savedTimestamp: Long): AssetEntity {
    return AssetEntity(
        id = id,
        rank = rank,
        symbol = symbol,
        name = name,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd,
        volumeUsd24Hr = volumeUsd24Hr,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        vwap24Hr = vwap24Hr,
        explorer = explorer,
        savedTimestamp = savedTimestamp
    )
}