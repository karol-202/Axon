package pl.karol202.axon.specification

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.layer.LayerData
import pl.karol202.axon.neuron.Neuron

abstract class LayerSpecification<L : Layer<N>, N : Neuron> : SpecificationElement
{
	private val neurons = mutableListOf<NeuronSpecification<N>>()

	val size get() = neurons.size

	fun <NS : NeuronSpecification<N>> addNeuron(neuron: NS)
	{
		neurons.add(neuron)
	}

	abstract fun createLayer(layerData: LayerData): L

	protected fun LayerData.createNeurons(): List<N>
	{
		val neuronsData = getNeuronsData()
		if(neuronsData.size != neurons.size) throw IllegalArgumentException("Invalid layer data.")
		return neurons.mapIndexed { i, neuronSpecs -> neuronSpecs.createNeuron(neuronsData[i]) }
	}
}