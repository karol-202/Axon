package pl.karol202.axon.network

import pl.karol202.axon.AxonException
import pl.karol202.axon.Output
import pl.karol202.axon.VectorWithResponse
import pl.karol202.axon.layer.BasicLayer
import pl.karol202.axon.minus
import pl.karol202.axon.network.specification.NetworkSpecification
import pl.karol202.axon.network.specification.createNetwork
import pl.karol202.axon.neuron.BasicNeuron

fun <O> basicNetwork(inputs: Int, outputType: Output<O>, init: BasicNetwork.Specification<O>.() -> Unit) =
		createNetwork(BasicNetwork.Specification(inputs, outputType), init)

class BasicNetwork<O>(
		layers: List<BasicLayer>,
		outputType: Output<O>
) : AbstractNetwork<BasicLayer, BasicNeuron, O>(layers, outputType),
    SupervisedNetwork<BasicLayer, BasicNeuron, O>
{
	class Specification<O>(
			inputs: Int,
			outputType: Output<O>
	) : NetworkSpecification<BasicNetwork<O>, BasicLayer, BasicNeuron, O>(inputs, outputType)
	{
		override fun createNetwork(layers: List<BasicLayer>) = BasicNetwork(layers, outputType)
	}

	override fun learn(vector: VectorWithResponse, learnRatio: Float): FloatArray
	{
		if(vector.outputs.size != layers.last().size) throw AxonException("Invalid vector size")
		val output = calculateRaw(vector)
		var error = vector.outputs - output
		((layers.size - 1) downTo 0).forEach { i ->
			val layer = layers[i]
			val previousLayerSize = if(i != 0) layers[i].size else 0
			val currentError = error
			error = layer.backpropagateError(currentError, previousLayerSize)
			layer.learn(currentError, learnRatio)
		}
		return error
	}
}