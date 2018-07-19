package pl.karol202.axon.network

import pl.karol202.axon.VectorWithResponse

interface SupervisedNetwork<O> : Network<O>
{
	//Returns error of network before learning
	fun learn(vector: VectorWithResponse, learnRatio: Float): FloatArray
}