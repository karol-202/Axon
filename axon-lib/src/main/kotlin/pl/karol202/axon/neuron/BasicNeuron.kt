package pl.karol202.axon.neuron

import pl.karol202.axon.AxonException

class BasicNeuron(
		inputs: Int,
		activation: Activation
) : AbstractNeuron(inputs, activation), BackpropagationNeuron
{
	override fun learn(error: Float, learnRate: Float)
	{
		val input = input ?: throw AxonException("Cannot learn neuron without calculating.")
		repeat(weights.size) { i ->
			val inputFactor = if(i < inputs) input[i] else 1f
			weights[i] += learnRate * error * inputFactor
		}
	}

	override fun backpropagateErrorForInput(error: Float, input: Int): Float
	{
		if(input >= inputs) throw AxonException("Too few inputs.")
		return error * weights[input]
	}
}