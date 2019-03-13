package pl.karol202.axon.network

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

	fun calculateAndGetIntermediateOutputs(input: FloatArray): List<FloatArray>
	{
		var currentInput = input
		return layers.map { layer ->
			layer.calculate(currentInput).also { output ->
				currentInput = output
			}
		}
	}

	fun backpropagateErrorAndGetIntermediateErrors(error: FloatArray): List<FloatArray>
	{
		val intermediateErrors = mutableListOf(error)
		var currentError = error

		for(layerIndex in layersAmount - 1 downTo 1)
		{
			val layer = layers[layerIndex]
			val previousLayer = layers[layerIndex - 1]
			currentError = layer.backpropagateError(currentError, previousLayer.size)
			intermediateErrors.add(currentError)
		}
		return intermediateErrors.apply { reverse() }
	}

	fun learn(input: FloatArray, intermediateOutputs: List<FloatArray>, intermediateErrors: List<FloatArray>, learnRate: Float)
	{
		checkInputSize(input)

		var currentInput = input
		layers.forEachIndexed { layerIndex, layer ->
			layer.learn(input = currentInput, output = intermediateOutputs[layerIndex],
			            error = intermediateErrors[layerIndex], learnRate = learnRate)
			currentInput = intermediateOutputs[layerIndex]
		}
	}
}