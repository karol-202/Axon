package pl.karol202.axon.network

import pl.karol202.axon.FloatRange
import pl.karol202.axon.Vector

interface Network<O>
{
	fun randomize(range: FloatRange)

	fun calculate(vector: Vector): O
}