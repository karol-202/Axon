package pl.karol202.axon.specification

import pl.karol202.axon.AxonException
import pl.karol202.axon.layer.Layer
import pl.karol202.axon.layer.LayerData
import pl.karol202.axon.neuron.Neuron

abstract class LayerSpecification<L : Layer<N>, N : Neuron> : SpecificationElement
{
	private val neurons = mutableListOf<NeuronSpecification<N>>()

	fun <NS : NeuronSpecification<N>> addNeuron(neuron: NS)
	{
		neurons.add(neuron)
	}

	internal fun create(inputs: Int, layerData: LayerData?) = createLayer(createNeurons(inputs, layerData))

	abstract fun createLayer(neurons: List<N>): L

	private fun createNeurons(inputs: Int, layerData: LayerData?): List<N>
	{
		layerData?.let { if(it.size != neurons.size) throw AxonException("Invalid layer data.") }
		return neurons.mapIndexed { i, neuronSpecs -> neuronSpecs.create(inputs, layerData?.get(i)) }
	}
}