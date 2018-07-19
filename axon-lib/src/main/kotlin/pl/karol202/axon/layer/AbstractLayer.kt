package pl.karol202.axon.layer

import pl.karol202.axon.FloatRange
import pl.karol202.axon.neuron.Neuron

abstract class AbstractLayer<N : Neuron> protected constructor(
		protected val neurons: List<N>
) : Layer {
	override fun randomizeWeights(range: FloatRange)
	{
		neurons.forEach { it.randomizeWeights(range) }
	}

	override fun calculate(input: FloatArray) = neurons.map { it.calculate(input) }.toFloatArray()

	override fun learn(errors: FloatArray, learnRate: Float)
	{
		neurons.forEachIndexed { i, neuron -> neuron.learn(errors[i], learnRate) }
	}
}