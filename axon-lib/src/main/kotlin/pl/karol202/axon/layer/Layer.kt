package pl.karol202.axon.layer

import pl.karol202.axon.FloatRange

interface Layer
{
	fun randomizeWeights(range: FloatRange)

	fun calculate(input: FloatArray): FloatArray

	//To call only after calling calculate()
	fun learn(errors: FloatArray, learnRate: Float)
}