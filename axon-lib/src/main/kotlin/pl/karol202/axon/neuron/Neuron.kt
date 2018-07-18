package pl.karol202.axon.neuron

import pl.karol202.axon.FloatRange

interface Neuron
{
	fun randomizeWeights(range: FloatRange)

	fun calculate(input: FloatArray): Float

	//To call only after calling calculate()
	fun learn(error: Float, learnRate: Float)
}