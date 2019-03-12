package pl.karol202.axon.layer

import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.StandardSupervisedNeuron
import pl.karol202.axon.specification.LayerSpecification

fun StandardSupervisedLayer.Specification.standardSupervisedNeuron(activation: Activation) =
		addNeuron(StandardSupervisedNeuron.Specification(activation))

open class StandardSupervisedLayer(neurons: List<StandardSupervisedNeuron>) :
		AbstractSupervisedLayer<StandardSupervisedNeuron>(neurons)
{
	class Specification : LayerSpecification<StandardSupervisedLayer, StandardSupervisedNeuron>()
	{
		override fun createLayer(layerData: LayerData) = StandardSupervisedLayer(layerData.createNeurons())
	}

	open fun learn(error: FloatArray, learnRate: Float)
	{
		checkOutputSize(error)
		neurons.forEachIndexed { i, neuron -> neuron.learn(error = error[i], learnRate = learnRate) }
	}
}