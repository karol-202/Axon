package pl.karol202.axon.network

import pl.karol202.axon.layer.LayerData
import pl.karol202.axon.specification.NetworkSpecification
import pl.karol202.axon.util.FloatRange

interface NetworkData
{
	companion object
	{
		fun random(networkSpecification: NetworkSpecification<*, *, *>, range: FloatRange): NetworkData =
				RandomNetworkData(networkSpecification, range)

		fun fromList(list: List<LayerData>): NetworkData = ListNetworkData(list)
	}

	fun getLayersData(): List<LayerData>
}

private class ListNetworkData(private val list: List<LayerData>) : NetworkData
{
	override fun getLayersData() = list
}

private class RandomNetworkData(private val networkSpecification: NetworkSpecification<*, *, *>,
                                private val range: FloatRange) : NetworkData
{
	override fun getLayersData(): List<LayerData>
	{
		var currentInputs = networkSpecification.inputs
		return List(networkSpecification.size) { layerIndex ->
			val layerSpecification = networkSpecification.getLayerSpecification(layerIndex)
			val data = LayerData.random(layerSpecification, currentInputs, range)
			currentInputs = layerSpecification.size
			data
		}
	}
}