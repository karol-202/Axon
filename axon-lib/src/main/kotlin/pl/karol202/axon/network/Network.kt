package pl.karol202.axon.network

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

interface Network<L : Layer<N>, N : Neuron>
{
	val layersAmount: Int
	val inputs: Int
	val outputs: Int
	val networkData: NetworkData

	fun calculate(input: FloatArray): FloatArray
}