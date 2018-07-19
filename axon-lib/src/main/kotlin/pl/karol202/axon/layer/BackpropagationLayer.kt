package pl.karol202.axon.layer

interface BackpropagationLayer : Layer
{
	//Calculates error of previous(closer to input) layer
	fun backpropagateError(errors: FloatArray): FloatArray
}

/*class BackpropagationLayer private constructor(
		neurons: Array<BasicNeuron>
) : AbstractLayer<BasicNeuron>(neurons) {
	constructor(neurons: Int, inputs: Int, activation: Activation) :
			this(Array(neurons) { BasicNeuron(inputs, activation) })

	//Returns error of this layer
	fun backpropagateError(nextLayerErrors: FloatArray): FloatArray
	{
		neurons.mapIndexed { i, neuron ->

		}
	}
}*/