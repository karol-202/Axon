package pl.karol202.axon.network

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.layer.LayerData

data class NetworkData(
		private val layers: List<LayerData>
) : Iterable<LayerData>
{
	companion object
	{
		fun fromLayers(layers: List<Layer<*>>) = NetworkData(layers.map { it.getLayerData() })
	}

	val size: Int
		get() = layers.size

	operator fun get(layer: Int) = layers[layer]

	override fun iterator() = layers.iterator()
}