package pl.karol202.axon.neuron

import pl.karol202.axon.specification.NeuronSpecification

open class ReinforcementNeuron(activation: Activation,
                               bias: Float,
                               weights: FloatArray) : AbstractSupervisedNeuron(activation, bias, weights)
{
	class Specification(private val activation: Activation,
	                    private val bias: Float) : NeuronSpecification<ReinforcementNeuron>()
	{
		override fun createNeuron(neuronData: NeuronData) =
				ReinforcementNeuron(activation, bias, neuronData.getWeights().toFloatArray())
	}
}