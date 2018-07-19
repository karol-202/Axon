package pl.karol202.axon.layer

import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.BasicNeuron

class BasicLayer private constructor(
		neurons: Int,
		private val inputs: Int,
		activation: Activation
) : AbstractLayer<BasicNeuron>(List(neurons) { BasicNeuron(inputs, activation) }),
    BackpropagationLayer {
	override fun backpropagateError(errors: FloatArray) = FloatArray(inputs) { i ->
		neurons.mapIndexed { neuronIndex, neuron ->
			neuron.backpropagateErrorForInput(errors[neuronIndex], i)
		}.sum()
	}
}