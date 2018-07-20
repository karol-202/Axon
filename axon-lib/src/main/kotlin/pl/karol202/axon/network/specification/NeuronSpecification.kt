package pl.karol202.axon.network.specification

import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.Neuron

abstract class NeuronSpecification<N : Neuron>(
		val activation: Activation
) : SpecificationElement
{
	abstract fun createNeuron(inputs: Int): N
}