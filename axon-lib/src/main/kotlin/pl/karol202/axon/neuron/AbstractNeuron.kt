package pl.karol202.axon.neuron

import pl.karol202.axon.AxonException
import pl.karol202.axon.FloatRange
import pl.karol202.axon.randomNonZero

abstract class AbstractNeuron(
		val inputs: Int,
		val activation: Activation
) : Neuron {
	protected val weights = FloatArray(inputs + 1)

	protected var input: FloatArray? = null

	override fun randomizeWeights(range: FloatRange)
	{
		repeat(inputs) { weights[it] = range.randomNonZero() }
	}

	override fun calculate(input: FloatArray): Float
	{
		if(input.size != inputs)
			throw AxonException("Input array of size ${input.size} is not applicable to neuron with $inputs inputs.")
		this.input = input
		val sum = (input + 1f).mapIndexed { i, value -> value * weights[i] }.sum()
		return activation.calculate(sum)
	}
}