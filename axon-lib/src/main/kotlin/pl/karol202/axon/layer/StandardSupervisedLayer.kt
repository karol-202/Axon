package pl.karol202.axon.layer

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.StandardSupervisedNeuron
import pl.karol202.axon.specification.LayerSpecification

fun StandardSupervisedLayer.Specification.standardSupervisedNeuron(activation: Activation, bias: Float) =
		addNeuron(StandardSupervisedNeuron.Specification(activation, bias))

open class StandardSupervisedLayer(neurons: List<StandardSupervisedNeuron>) :
		AbstractSupervisedLayer<StandardSupervisedNeuron>(neurons)
{
	class Specification : LayerSpecification<StandardSupervisedLayer, StandardSupervisedNeuron>()
	{
		override fun createLayer(layerData: LayerData) = StandardSupervisedLayer(layerData.createNeurons())
	}

	open suspend fun learn(error: FloatArray, learnRate: Float) = coroutineScope {
		checkOutputSize(error)
		neurons.mapIndexed { i, neuron ->
			launch { neuron.learn(error = error[i], learnRate = learnRate) }
		}.forEach { it.join() }
	}
}