package pl.karol202.axon.specification

import pl.karol202.axon.network.Output
import pl.karol202.axon.layer.Layer
import pl.karol202.axon.network.Network
import pl.karol202.axon.neuron.Neuron

fun <NS : NetworkSpecification<*, *, *, *>> createNetwork(network: NS, init: NS.() -> Unit) = network.apply(init)

abstract class NetworkSpecification<T : Network<L, N, O>, L : Layer<N>, N : Neuron, O>(
		val inputs: Int,
		val outputType: Output<O>
) : SpecificationElement
{
	private val layers = mutableListOf<LayerSpecification<L, N>>()

	fun <LS : LayerSpecification<L, N>> addLayer(layer: LS, init: LS.() -> Unit)
	{
		layer.init()
		layers.add(layer)
	}

	fun create() = createNetwork(createLayers())

	abstract fun createNetwork(layers: List<L>): T

	private fun createLayers(): List<L>
	{
		var layerInputs = inputs
		return layers.map { layerSpecs ->
			layerSpecs.create(layerInputs).also { layerInputs = it.size }
		}
	}
}