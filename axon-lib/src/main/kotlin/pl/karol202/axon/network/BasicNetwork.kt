package pl.karol202.axon.network

import pl.karol202.axon.AxonException
import pl.karol202.axon.Output
import pl.karol202.axon.VectorWithResponse
import pl.karol202.axon.layer.BasicLayer
import pl.karol202.axon.minus

class BasicNetwork<O>(
		layers: List<BasicLayer>,
		outputType: Output<O>
) : AbstractNetwork<BasicLayer, O>(layers, outputType), SupervisedNetwork<O> {
	override fun learn(vector: VectorWithResponse, learnRatio: Float): FloatArray
	{
		if(vector.outputs.size != layers.last().size) throw AxonException("Invalid vector size")
		val output = calculateRaw(vector)
		var error = vector.outputs - output
		((layers.size - 1) downTo 0).forEach { i ->
			val layer = layers[i]
			val currentError = error
			error = layer.backpropagateError(currentError)
			layer.learn(currentError, learnRatio)
		}
		return error
	}
}