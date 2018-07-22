package pl.karol202.axon.automation

import kotlinx.coroutines.experimental.yield
import pl.karol202.axon.network.Network
import pl.karol202.axon.network.Vector

class Tester<O>(
		private val network: Network<*, *, O>
)
{
	suspend fun test(vectors: List<Vector>, listener: (Vector, O) -> Unit)
	{
		vectors.forEach { vector ->
			val output = network.calculate(vector)
			listener(vector, output)
			yield()
		}
	}
}