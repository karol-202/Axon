package pl.karol202.axon.neuron

import pl.karol202.axon.specification.NeuronSpecification

open class ReinforcementNeuron(weights: FloatArray,
                               activation: Activation) : AbstractSupervisedNeuron(weights, activation)
{
	class Specification(private val activation: Activation) : NeuronSpecification<ReinforcementNeuron>()
	{
		override fun createNeuron(neuronData: NeuronData) =
				ReinforcementNeuron(neuronData.getWeights().toFloatArray(), activation)
	}
}