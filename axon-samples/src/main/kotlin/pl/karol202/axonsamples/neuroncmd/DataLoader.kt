package pl.karol202.axonsamples.neuroncmd

import com.squareup.moshi.Moshi
import pl.karol202.axon.network.NetworkData
import java.io.File

object DataLoader
{
	fun loadNetworkData(file: File): NetworkData?
	{
		if(!file.exists()) return null
		val adapter = Moshi.Builder().build().adapter(NetworkData::class.java)
		return adapter.fromJson(file.readText())
	}

	fun saveNetworkData(file: File, data: NetworkData)
	{
		file.writer().use { writer ->
			val adapter = Moshi.Builder().build().adapter(NetworkData::class.java)
			writer.write(adapter.toJson(data))
		}
	}
}