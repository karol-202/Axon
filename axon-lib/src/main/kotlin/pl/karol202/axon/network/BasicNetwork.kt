package pl.karol202.axon.network

import pl.karol202.axon.AxonException
import pl.karol202.axon.layer.BasicLayer
import pl.karol202.axon.minus
import pl.karol202.axon.neuron.BasicNeuron
import pl.karol202.axon.specification.NetworkSpecification
import pl.karol202.axon.specification.createNetwork

fun <O> basicNetwork(inputs: Int, outputType: Output<O>, networkData: NetworkData?, init: BasicNetwork.Specification<O>.() -> Unit) =
		createNetwork(BasicNetwork.Specification(inputs, outputType, networkData), init)

class BasicNetwork<O>(
		layers: List<BasicLayer>,
		outputType: Output<O>
) : AbstractNetwork<BasicLayer, BasicNeuron, O>(layers, outputType),
    SupervisedNetwork<BasicLayer, BasicNeuron, O>
{
	class Specification<O>(
			inputs: Int,
			outputType: Output<O>,
			networkData: NetworkData?
	) : NetworkSpecification<BasicNetwork<O>, BasicLayer, BasicNeuron, O>(inputs, outputType, networkData)
	{
		override fun createNetwork(layers: List<BasicLayer>) = BasicNetwork(layers, outputType)
	}

	override fun learn(vector: VectorWithResponse, learnRate: Float): SupervisedNetwork.LearnOutput<O>
	{
		if(vector.outputs.size != layers.last().size) throw AxonException("Invalid vector size")
		val outputRaw = calculateRaw(vector)
		val outputError = vector.outputs - outputRaw
		var currentError = outputError
		((layers.size - 1) downTo 0).forEach { i ->
			val layer = layers[i]
			val previousLayerSize = if(i != 0) layers[i - 1].size else 0
			val error = currentError
			currentError = layer.backpropagateError(error, previousLayerSize)
			layer.learn(error, learnRate)
		}
		val output = outputType.transform(outputRaw)
		return SupervisedNetwork.LearnOutput(output, outputError)
	}
}