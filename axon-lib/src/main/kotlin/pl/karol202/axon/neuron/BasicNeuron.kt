package pl.karol202.axon.neuron

import pl.karol202.axon.AxonException
import pl.karol202.axon.layer.BasicLayer
import pl.karol202.axon.specification.NeuronSpecification

fun BasicLayer.Specification.basicNeuron(activation: Activation) = addNeuron(BasicNeuron.Specification(activation))

class BasicNeuron(
		weights: FloatArray,
		activation: Activation
) : AbstractNeuron(weights, activation),
    BackpropagationNeuron
{
	class Specification(
			activation: Activation
	) : NeuronSpecification<BasicNeuron>(activation)
	{
		override fun createNeuron(inputs: Int) = BasicNeuron(inputs, activation)

		override fun createNeuron(weights: FloatArray) = BasicNeuron(weights, activation)
	}

	constructor(inputs: Int, activation: Activation) : this(FloatArray(inputs + 1), activation)

	override fun learn(error: Float, learnRate: Float)
	{
		val input = input ?: throw AxonException("Cannot learn neuron without calculating.")
		val output = output ?: throw AxonException("Cannot learn neuron without calculating.")
		val transformedError = error * activation.calculateDerivative(output)
		repeat(weights.size) { i ->
			val inputFactor = if(i < inputs) input[i] else 1f
			weights[i] += learnRate * transformedError * inputFactor
		}
	}

	override fun backpropagateErrorForInput(error: Float, input: Int): Float
	{
		if(input >= inputs) throw AxonException("Too few inputs.")
		return error * weights[input]
	}
}