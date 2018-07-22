package pl.karol202.axon.network

import pl.karol202.axon.FloatRange
import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

interface Network<L : Layer<N>, N : Neuron, O>
{
	fun randomize(range: FloatRange)

	fun calculate(vector: Vector): O
}