package pl.karol202.axon.network.specification

import pl.karol202.axon.AxonException
import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.Neuron

abstract class NeuronSpecification<N : Neuron>(
		val weights: FloatArray? = null,
		val activation: Activation
) : SpecificationElement
{
	internal fun create(inputs: Int): N
	{
		if(weights == null) return createNeuron(inputs)
		val neuron = createNeuron(weights)
		if(neuron.inputs != inputs) throw AxonException("Expected neuron inputs: $inputs, actual: ${neuron.inputs}")
		return neuron
	}

	abstract fun createNeuron(inputs: Int): N

	abstract fun createNeuron(weights: FloatArray): N
}