package pl.karol202.axon.network

import pl.karol202.axon.FloatRange
import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

abstract class AbstractNetwork<L : Layer<N>, N : Neuron, O>(
		protected val layers: List<L>,
		protected val outputType: Output<O>
) : Network<L, N, O>
{
	override fun getNetworkData() = NetworkData.fromLayers(layers)

	override fun randomize(range: FloatRange)
	{
		layers.forEach { it.randomizeWeights(range) }
	}

	override fun calculate(vector: Vector) =
			outputType.transform(calculateRaw(vector))

	protected fun calculateRaw(vector: Vector) =
			layers.fold(vector.inputs) { layerInput, layer -> layer.calculate(layerInput) }
}