package pl.karol202.axon.network

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

interface SupervisedNetwork<L : Layer<N>, N : Neuron, O> : Network<L, N, O>
{
	data class LearnOutput<O>(
			val output: O,
			val errors: FloatArray
	)

	//Returns error of network before learning
	fun learn(vector: VectorWithResponse, learnRate: Float): LearnOutput<O>
}