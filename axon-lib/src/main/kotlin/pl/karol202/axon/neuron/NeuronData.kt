package pl.karol202.axon.neuron

import pl.karol202.axon.util.FloatRange
import pl.karol202.axon.util.randomNonZero

interface NeuronData
{
	companion object
	{
		fun random(inputs: Int, range: FloatRange): NeuronData = RandomNeuronData(inputs, range)

		fun fromArray(array: FloatArray): NeuronData = ArrayNeuronData(array)

		fun fromList(list: List<Float>): NeuronData = ArrayNeuronData(list.toFloatArray())
	}

	fun getWeights(): List<Float>
}

private class ArrayNeuronData(private val array: FloatArray) : AbstractList<Float>(), NeuronData
{
	override val size = array.size

	override operator fun get(index: Int) = array[index]

	override fun getWeights() = array.toList()
}

private class RandomNeuronData(private val inputs: Int,
                               private val range: FloatRange) : NeuronData
{
	override fun getWeights() = List(inputs + 1) { range.randomNonZero() }
}