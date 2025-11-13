package uvg.arodi.chavez.navigation

import kotlinx.serialization.Serializable

@Serializable
object AssetListDestination

@Serializable
data class AssetDetailDestination(val assetId: String)