package pl.karol202.axon.network

import pl.karol202.axon.Vector

interface UnsupervisedNetwork<O> : Network<O>
{
	fun learn(vector: Vector, learnRatio: Float)
}