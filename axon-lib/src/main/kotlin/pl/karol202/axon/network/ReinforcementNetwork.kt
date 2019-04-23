package pl.karol202.axon.network

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import pl.karol202.axon.layer.ReinforcementLayer
import pl.karol202.axon.neuron.ReinforcementNeuron
import pl.karol202.axon.specification.NetworkSpecification
import pl.karol202.axon.specification.networkBuilder

fun reinforcementNetwork(inputs: Int, init: ReinforcementNetwork.Specification.() -> Unit) =
		networkBuilder(ReinforcementNetwork.Specification(inputs), init)

fun ReinforcementNetwork.Specification.reinforcementLayer(init: ReinforcementLayer.Specification.() -> Unit) =
		addLayer(ReinforcementLayer.Specification(), init)

open class ReinforcementNetwork(inputs: Int,
                                layers: List<ReinforcementLayer>) :
		AbstractNetwork<ReinforcementLayer, ReinforcementNeuron>(inputs, layers)
{
	class Specification(inputs: Int) :
			NetworkSpecification<ReinforcementNetwork, ReinforcementLayer, ReinforcementNeuron>(inputs)
	{
		override fun createNetwork(networkData: NetworkData) = ReinforcementNetwork(inputs, networkData.createLayers())
	}

	suspend fun calculateAndGetIntermediateOutputs(input: FloatArray): List<FloatArray>
	{
		var currentInput = input
		return layers.map { layer ->
			layer.calculate(currentInput).also { output ->
				currentInput = output
			}
		}
	}

	suspend fun backpropagateErrorAndGetIntermediateErrors(error: FloatArray): List<FloatArray>
	{
		val intermediateErrors = mutableListOf(error)
		var currentError = error

		for(layerIndex in layersAmount - 1 downTo 1)
		{
			val layer = layers[layerIndex]
			val previousLayer = layers[layerIndex - 1]
			currentError = layer.backpropagateError(currentError, previousLayer.size)
			intermediateErrors.add(0, currentError) // Insert at beginning
		}
		return intermediateErrors
	}

	suspend fun learn(input: FloatArray, intermediateOutputs: List<FloatArray>,
	                  intermediateErrors: List<FloatArray>, learnRate: Float) = coroutineScope {
		checkInputSize(input)

		fun getInputOfLayer(layer: Int) = if(layer == 0) input else intermediateOutputs[layer]

		layers.mapIndexed { layerIndex, layer ->
			launch {
				layer.learn(input = getInputOfLayer(layerIndex), output = intermediateOutputs[layerIndex],
				            error = intermediateErrors[layerIndex], learnRate = learnRate)
			}
		}.forEach { it.join() }
	}
}