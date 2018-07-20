package pl.karol202.axon.neuron

import pl.karol202.axon.AxonException
import pl.karol202.axon.layer.BasicLayer
import pl.karol202.axon.network.specification.NeuronSpecification

fun BasicLayer.Specification.basicNeuron(activation: Activation) =
		addNeuron(BasicNeuron.Specification(activation))

class BasicNeuron(
		inputs: Int,
		activation: Activation
) : AbstractNeuron(inputs, activation), BackpropagationNeuron
{
	class Specification(activation: Activation) : NeuronSpecification<BasicNeuron>(activation)
	{
		override fun createNeuron(inputs: Int) = BasicNeuron(inputs, activation)
	}

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