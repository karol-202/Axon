package pl.karol202.axon.neuron

abstract class AbstractNeuron(protected val activation: Activation,
                              protected val bias: Float,
                              protected val weights: FloatArray) : Neuron
{
	override val inputs = weights.size - 1
	override val neuronData = NeuronData.fromArray(weights)

	override suspend fun calculate(input: FloatArray): Float
	{
		checkInputSize(input)
		val sum = (input + bias).mapIndexed { i, value -> value * weights[i] }.sum()
		return activation.calculate(sum)
	}

	protected fun checkInputSize(input: FloatArray) =
			if(input.size == inputs) Unit
			else throw IllegalArgumentException("Input array of size ${input.size} is not applicable to neuron with $inputs inputs.")
}