package pl.karol202.axon.layer

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

	override fun calculate(input: FloatArray) = neurons.map { it.calculate(input) }.toFloatArray()

	protected fun checkInputSize(input: FloatArray) =
			if(input.size == inputs) Unit
			else throw IllegalArgumentException("Input array of size ${input.size} is not applicable to layer with $inputs inputs.")

	// Works for errors as well
	protected fun checkOutputSize(output: FloatArray) =
			if(output.size == size) Unit
			else throw IllegalArgumentException("Output array of size ${output.size} is not applicable to layer with $size neurons.")
}