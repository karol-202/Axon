package pl.karol202.axon.specification

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

abstract class LayerSpecification<L : Layer<N>, N : Neuron> : SpecificationElement
{
	private val neurons = mutableListOf<NeuronSpecification<N>>()

	fun <NS : NeuronSpecification<N>> addNeuron(neuron: NS)
	{
		neurons.add(neuron)
	}

	internal fun create(inputs: Int) = createLayer(createNeurons(inputs))

	abstract fun createLayer(neurons: List<N>): L

	private fun createNeurons(inputs: Int) = neurons.map { it.create(inputs) }
}