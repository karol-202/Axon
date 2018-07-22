package pl.karol202.axon.network

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

interface UnsupervisedNetwork<L : Layer<N>, N : Neuron, O> : Network<L, N, O>
{
	fun learn(vector: Vector, learnRatio: Float)
}