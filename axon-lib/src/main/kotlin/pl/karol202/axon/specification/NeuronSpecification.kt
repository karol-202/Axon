package pl.karol202.axon.specification

import pl.karol202.axon.neuron.Neuron
import pl.karol202.axon.neuron.NeuronData

abstract class NeuronSpecification<N : Neuron> : SpecificationElement
{
	abstract fun createNeuron(neuronData: NeuronData): N
}