package pl.karol202.axon.specification

import pl.karol202.axon.AxonException
import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.Neuron
import pl.karol202.axon.neuron.NeuronData

abstract class NeuronSpecification<N : Neuron>(
		val activation: Activation
) : SpecificationElement
{
	internal fun create(inputs: Int, neuronData: NeuronData?): N
	{
		if(neuronData == null) return createNeuron(inputs)
		val neuron = createNeuron(neuronData.weights)
		if(neuron.inputs != inputs) throw AxonException("Expected neuron inputs: $inputs, actual: ${neuron.inputs}")
		return neuron
	}

	abstract fun createNeuron(inputs: Int): N

	abstract fun createNeuron(weights: FloatArray): N
}