package pl.karol202.axon.layer

import pl.karol202.axon.neuron.NeuronData
import pl.karol202.axon.specification.LayerSpecification
import pl.karol202.axon.util.FloatRange

interface LayerData
{
	companion object
	{
		fun random(layerSpecification: LayerSpecification<*, *>, inputs: Int, range: FloatRange): LayerData =
				RandomLayerData(layerSpecification, inputs, range)

		fun fromList(list: List<NeuronData>): LayerData = ListLayerData(list)
	}

	fun getNeuronsData(): List<NeuronData>
}

private class ListLayerData(private val list: List<NeuronData>) : LayerData
{
	override fun getNeuronsData() = list
}

private class RandomLayerData(private val layerSpecification: LayerSpecification<*, *>,
                              private val inputs: Int,
                              private val range: FloatRange) : LayerData
{
	override fun getNeuronsData() =
			List(layerSpecification.size) { NeuronData.random(inputs, range) }
}