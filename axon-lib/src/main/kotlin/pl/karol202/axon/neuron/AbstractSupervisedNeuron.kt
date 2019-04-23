package pl.karol202.axon.neuron

abstract class AbstractSupervisedNeuron(activation: Activation,
                                        bias: Float,
                                        weights: FloatArray) : AbstractNeuron(activation, bias, weights),
                                                               SupervisedNeuron
{
	override suspend fun learn(input: FloatArray, output: Float, error: Float, learnRate: Float)
	{
		checkInputSize(input)
		val transformedError = error * activation.calculateDerivative(output)
		repeat(weights.size) { i ->
			val inputFactor = if(i < inputs) input[i] else 1f
			weights[i] += learnRate * transformedError * inputFactor
		}
	}

	override suspend fun backpropagateErrorForInput(error: Float, input: Int) = error * weights[input]
}