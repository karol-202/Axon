package pl.karol202.axonsamples.neuroncmd

import com.squareup.moshi.Moshi
import pl.karol202.axon.layer.LayerData
import pl.karol202.axon.network.NetworkData
import pl.karol202.axon.neuron.NeuronData
import java.io.File

private typealias NetworkArrays = Array<Array<FloatArray>>

object DataLoader
{
	fun loadNetworkData(file: File): NetworkData?
	{
		if(!file.exists()) return null
		val adapter = Moshi.Builder().build().adapter(NetworkArrays::class.java)
		val networkArrays = adapter.fromJson(file.readText()) ?: return null
		return NetworkData.fromList(networkArrays.map { layerData ->
			LayerData.fromList(layerData.map { neuronData ->
				NeuronData.fromArray(neuronData)
			})
		})
	}

	fun saveNetworkData(file: File, data: NetworkData)
	{
		file.writer().use { writer ->
			val adapter = Moshi.Builder().build().adapter(NetworkArrays::class.java)
			val networkArrays = data.getLayersData().map { layerData ->
				layerData.getNeuronsData().map { neuronData ->
					neuronData.getWeights().toFloatArray()
				}.toTypedArray()
			}.toTypedArray()
			writer.write(adapter.toJson(networkArrays))
		}
	}
}