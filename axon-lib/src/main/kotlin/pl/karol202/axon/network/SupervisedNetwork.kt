package pl.karol202.axon.network

import pl.karol202.axon.VectorWithResponse
import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

interface SupervisedNetwork<L : Layer<N>, N : Neuron, O> : Network<L, N, O>
{
	//Returns error of network before learning
	fun learn(vector: VectorWithResponse, learnRatio: Float): FloatArray
}