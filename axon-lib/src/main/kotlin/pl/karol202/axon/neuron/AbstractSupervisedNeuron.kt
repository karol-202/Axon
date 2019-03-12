package pl.karol202.axon.neuron

abstract class AbstractSupervisedNeuron(weights: FloatArray,
                                        activation: Activation) : AbstractNeuron(weights, activation),
                                                           SupervisedNeuron
{
	override fun learn(input: FloatArray, output: Float, error: Float, learnRate: Float)
	{
		checkInputSize(input)
		val transformedError = error * activation.calculateDerivative(output)
		repeat(weights.size) { i ->
			val inputFactor = if(i < inputs) input[i] else 1f
			weights[i] += learnRate * transformedError * inputFactor
		}
	}

	override fun backpropagateErrorForInput(error: Float, input: Int) = error * weights[input]
}