package pl.karol202.axon.network

interface Output<O>
{
	fun transform(rawOutput: FloatArray): O
}

class RawOutput : Output<FloatArray>
{
	override fun transform(rawOutput: FloatArray) = rawOutput
}