package pl.karol202.axon.layer

import pl.karol202.axon.FloatRange
import pl.karol202.axon.neuron.Neuron

interface Layer<N : Neuron>
{
	val size: Int
	val inputs: Int

	fun getLayerData(): LayerData

	fun randomizeWeights(range: FloatRange)

	fun calculate(input: FloatArray): FloatArray

	//To call only after calling calculate()
	fun learn(errors: FloatArray, learnRate: Float)
}