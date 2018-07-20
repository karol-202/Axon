package pl.karol202.axon.layer

import pl.karol202.axon.neuron.Neuron

interface BackpropagationLayer<N : Neuron> : Layer<N>
{
	//Calculates error of previous(closer to input) layer
	fun backpropagateError(errors: FloatArray, previousLayerSize: Int): FloatArray
}