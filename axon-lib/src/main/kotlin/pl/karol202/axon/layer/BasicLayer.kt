package pl.karol202.axon.layer

import pl.karol202.axon.AxonException
import pl.karol202.axon.network.BasicNetwork
import pl.karol202.axon.network.specification.LayerSpecification
import pl.karol202.axon.neuron.BasicNeuron

fun BasicNetwork.Specification<*>.basicLayer(init: BasicLayer.Specification.() -> Unit) =
		addLayer(BasicLayer.Specification(), init)

class BasicLayer(
		neurons: List<BasicNeuron>
) : AbstractLayer<BasicNeuron>(neurons),
    BackpropagationLayer<BasicNeuron> {
	class Specification : LayerSpecification<BasicLayer, BasicNeuron>()
	{
		override fun createLayer(neurons: List<BasicNeuron>) = BasicLayer(neurons)
	}

	override fun backpropagateError(errors: FloatArray, previousLayerSize: Int): FloatArray
	{
		if(errors.size != size)
			throw AxonException("Errors array of size ${errors.size} is not applicable to layer with $size neurons.")
		return FloatArray(previousLayerSize) { i ->
			neurons.mapIndexed { neuronIndex, neuron ->
				neuron.backpropagateErrorForInput(errors[neuronIndex], i)
			}.sum()
		}
	}
}