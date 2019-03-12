package pl.karol202.axon.specification

import pl.karol202.axon.layer.Layer
import pl.karol202.axon.network.Network
import pl.karol202.axon.network.NetworkData
import pl.karol202.axon.neuron.Neuron
import pl.karol202.axon.util.FloatRange

fun <NS : NetworkSpecification<*, *, *>> networkBuilder(network: NS, init: NS.() -> Unit) = network.apply(init)

fun <T : Network<L, N>, L : Layer<N>, N : Neuron> NetworkSpecification<T, L, N>.createNetworkRandomly(range: FloatRange) =
		createNetwork(NetworkData.random(this, range))

abstract class NetworkSpecification<T : Network<L, N>, L : Layer<N>, N : Neuron>(val inputs: Int) :
		SpecificationElement
{
	private val layers = mutableListOf<LayerSpecification<L, N>>()

	val size get() = layers.size

	fun <LS : LayerSpecification<L, N>> addLayer(layer: LS, init: LS.() -> Unit)
	{
		layer.init()
		layers.add(layer)
	}

	fun getLayerSpecification(index: Int) = layers[index]

	abstract fun createNetwork(networkData: NetworkData): T

	protected fun NetworkData.createLayers(): List<L>
	{
		val layersData = getLayersData()
		if(layersData.size != layers.size) throw IllegalArgumentException("Invalid network data.")
		return layers.mapIndexed { i, layerSpecs -> layerSpecs.createLayer(layersData[i]) }
	}
}