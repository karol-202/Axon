package pl.karol202.axon.network

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.neuron.Neuron

abstract class AbstractNetwork<L : Layer<N>, N : Neuron>(override val inputs: Int,
                                                         protected val layers: List<L>) : Network<L, N>
{
	override val layersAmount = layers.size
	override val outputs = layers.lastOrNull()?.size ?: 0
	override val networkData = NetworkData.fromList(layers.map { it.layerData })

	init
	{
		checkConsistency()
	}

	private fun checkConsistency()
	{
		var currentInputs = inputs
		layers.forEach { layer ->
			if(layer.inputs != currentInputs) throw IllegalArgumentException("Inconsistent layers.")
			currentInputs = layer.size
		}
	}

	override fun calculate(input: FloatArray) =
			layers.fold(input) { layerInput, layer -> layer.calculate(layerInput) }

	protected fun checkInputSize(input: FloatArray) =
			if(input.size == inputs) Unit
			else throw IllegalArgumentException("Input array of size ${input.size} is not applicable to network with $inputs inputs.")

	// Works for errors as well
	protected fun checkOutputSize(output: FloatArray) =
			if(output.size == outputs) Unit
			else throw IllegalArgumentException("Output array of size ${output.size} is not applicable to network with $output outputs.")
}