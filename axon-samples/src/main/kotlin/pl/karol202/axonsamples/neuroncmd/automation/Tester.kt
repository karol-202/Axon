package pl.karol202.axonsamples.neuroncmd.automation

import kotlinx.coroutines.yield
import pl.karol202.axon.network.Network
import pl.karol202.axonsamples.neuroncmd.Vector

class Tester(private val network: Network<*, *>)
{
	suspend fun test(vectors: List<Vector>, listener: (Vector, FloatArray) -> Unit)
	{
		vectors.forEach { vector ->
			val output = network.calculate(vector.input)
			listener(vector, output)
			yield()
		}
	}
}