package pl.karol202.axon.layer

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import pl.karol202.axon.neuron.AbstractSupervisedNeuron

abstract class AbstractSupervisedLayer<N : AbstractSupervisedNeuron>(neurons: List<N>) : AbstractLayer<N>(neurons),
                                                                                         SupervisedLayer<N>
{
	override suspend fun backpropagateError(errors: FloatArray, previousLayerSize: Int): FloatArray
	{
		checkOutputSize(errors)
		return FloatArray(previousLayerSize) { i ->
			neurons.mapIndexed { neuronIndex, neuron ->
				neuron.backpropagateErrorForInput(errors[neuronIndex], i)
			}.sum()
		}
	}

	override suspend fun learn(input: FloatArray, output: FloatArray, error: FloatArray, learnRate: Float) = coroutineScope {
		checkInputSize(input)
		checkOutputSize(output)
		checkOutputSize(error)
		neurons.mapIndexed { i, neuron ->
			launch { neuron.learn(input = input, output = output[i], error = error[i], learnRate = learnRate) }
		}.forEach { it.join() }
	}
}