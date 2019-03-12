package pl.karol202.axon.layer

import pl.karol202.axon.neuron.AbstractSupervisedNeuron

abstract class AbstractSupervisedLayer<N : AbstractSupervisedNeuron>(neurons: List<N>) : AbstractLayer<N>(neurons),
                                                                                         SupervisedLayer<N>
{
	override fun backpropagateError(errors: FloatArray, previousLayerSize: Int): FloatArray
	{
		checkOutputSize(errors)
		return FloatArray(previousLayerSize) { i ->
			neurons.mapIndexed { neuronIndex, neuron ->
				neuron.backpropagateErrorForInput(errors[neuronIndex], i)
			}.sum()
		}
	}

	override fun learn(input: FloatArray, output: FloatArray, error: FloatArray, learnRate: Float)
	{
		checkInputSize(input)
		checkOutputSize(output)
		checkOutputSize(error)
		neurons.forEachIndexed { i, neuron -> neuron.learn(input = input, output = output[i], error = error[i], learnRate = learnRate) }
	}
}