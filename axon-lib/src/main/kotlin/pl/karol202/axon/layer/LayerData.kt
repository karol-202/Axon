package pl.karol202.axon.layer

import pl.karol202.axon.neuron.Neuron
import pl.karol202.axon.neuron.NeuronData

data class LayerData(
		private val neurons: List<NeuronData>
) : Iterable<NeuronData>
{
	companion object
	{
		fun fromNeurons(neurons: List<Neuron>) = LayerData(neurons.map { it.getNeuronData() })
	}

	val size: Int
		get() = neurons.size

	operator fun get(neuron: Int) = neurons[neuron]

	override fun iterator() = neurons.iterator()
}