package pl.karol202.axon.layer

import pl.karol202.axon.neuron.BackpropagationNeuron
import pl.karol202.axon.neuron.Neuron
import pl.karol202.axon.neuron.SupervisedNeuron

interface Layer<N : Neuron>
{
	val size: Int
	val inputs: Int
	val layerData: LayerData

	suspend fun calculate(input: FloatArray): FloatArray
}

interface BackpropagationLayer<N : BackpropagationNeuron> : Layer<N>
{
	//Calculates error of previous(closer to input) layer
	suspend fun backpropagateError(errors: FloatArray, previousLayerSize: Int): FloatArray
}

interface SupervisedLayer<N : SupervisedNeuron> : BackpropagationLayer<N>
{
	suspend fun learn(input: FloatArray, output: FloatArray, error: FloatArray, learnRate: Float)
}