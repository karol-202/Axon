package pl.karol202.axon.neuron

data class NeuronData(
		val weightsList: List<Float>
)
{
	val weights: FloatArray
		get() = weightsList.toFloatArray()

	constructor(weights: FloatArray) : this(weights.toList())
}