package pl.karol202.axon.layer

import pl.karol202.axon.AxonException
import pl.karol202.axon.FloatRange
import pl.karol202.axon.neuron.Neuron

abstract class AbstractLayer<N : Neuron> protected constructor(
		protected val neurons: List<N>
) : Layer<N>
{
	override val size: Int
		get() = neurons.size

	override fun getLayerData() = LayerData.fromNeurons(neurons)

	override fun randomizeWeights(range: FloatRange)
	{
		neurons.forEach { it.randomizeWeights(range) }
	}

	override fun calculate(input: FloatArray) = neurons.map { it.calculate(input) }.toFloatArray()

	override fun learn(errors: FloatArray, learnRate: Float)
	{
		if(errors.size != size)
			throw AxonException("Errors array of size ${errors.size} is not applicable to layer with $size neurons.")
		neurons.forEachIndexed { i, neuron -> neuron.learn(errors[i], learnRate) }
	}
}