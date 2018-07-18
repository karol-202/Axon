package pl.karol202.axon.neuron

import pl.karol202.axon.AxonException

//TODO Find better name
class DeltaNeuron(
		inputs: Int,
		activation: Activation
) : AbstractNeuron(inputs, activation)
{
	override fun learn(error: Float, learnRate: Float)
	{
		val input = input ?: throw AxonException("Cannot learn neuron without calculating")
		repeat(weights.size) { i ->
			val inputFactor = if(i < inputs) input[i] else 1f
			weights[i] += learnRate * error * inputFactor
		}
	}
}