package pl.karol202.axon.layer

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import pl.karol202.axon.neuron.Neuron

abstract class AbstractLayer<N : Neuron>(protected val neurons: List<N>) : Layer<N>
{
	override val size = neurons.size
	override val inputs = neurons.map { it.inputs }.distinct().let { when(it.size)
	{
		0 -> 0
		1 -> it[0]
		else -> throw IllegalArgumentException("Inconsistent neurons")
	} }
	override val layerData = LayerData.fromList(neurons.map { it.neuronData })

	override suspend fun calculate(input: FloatArray) = coroutineScope {
		neurons.map { async { it.calculate(input) } }.map { it.await() }.toFloatArray()
	}

	protected fun checkInputSize(input: FloatArray) =
			if(input.size == inputs) Unit
			else throw IllegalArgumentException("Input array of size ${input.size} is not applicable to layer with $inputs inputs.")

	// Works for errors as well
	protected fun checkOutputSize(output: FloatArray) =
			if(output.size == size) Unit
			else throw IllegalArgumentException("Output array of size ${output.size} is not applicable to layer with $size neurons.")
}