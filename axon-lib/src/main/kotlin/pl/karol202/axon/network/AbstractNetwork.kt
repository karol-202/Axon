package pl.karol202.axon.network

import pl.karol202.axon.FloatRange
import pl.karol202.axon.Output
import pl.karol202.axon.Vector
import pl.karol202.axon.layer.Layer

abstract class AbstractNetwork<L : Layer, O>(
		protected val layers: List<L>,
		protected val outputType: Output<O>
) : Network<O> {
	override fun randomize(range: FloatRange)
	{
		layers.forEach { it.randomizeWeights(range) }
	}

	override fun calculate(vector: Vector) =
			outputType.transform(calculateRaw(vector))

	protected fun calculateRaw(vector: Vector) =
			layers.fold(vector.inputs) { layerInput, layer -> layer.calculate(layerInput) }
}