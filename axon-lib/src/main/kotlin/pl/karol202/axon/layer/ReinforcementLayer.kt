package pl.karol202.axon.layer

import pl.karol202.axon.neuron.Activation
import pl.karol202.axon.neuron.ReinforcementNeuron
import pl.karol202.axon.specification.LayerSpecification

fun ReinforcementLayer.Specification.reinforcementNeuron(activation: Activation, bias: Float) =
		addNeuron(ReinforcementNeuron.Specification(activation, bias))

open class ReinforcementLayer(neurons: List<ReinforcementNeuron>) :
		AbstractSupervisedLayer<ReinforcementNeuron>(neurons)
{
	class Specification : LayerSpecification<ReinforcementLayer, ReinforcementNeuron>()
	{
		override fun createLayer(layerData: LayerData) = ReinforcementLayer(layerData.createNeurons())
	}
}