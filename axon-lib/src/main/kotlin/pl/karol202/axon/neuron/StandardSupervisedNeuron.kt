package pl.karol202.axon.neuron

import pl.karol202.axon.specification.NeuronSpecification

open class StandardSupervisedNeuron(weights: FloatArray,
                                    activation: Activation) : AbstractSupervisedNeuron(weights, activation)
{
	class Specification(private val activation: Activation) : NeuronSpecification<StandardSupervisedNeuron>()
	{
		override fun createNeuron(neuronData: NeuronData) =
				StandardSupervisedNeuron(neuronData.getWeights().toFloatArray(), activation)
	}

	private var input: FloatArray? = null
	private var output: Float? = null

	override fun calculate(input: FloatArray): Float
	{
		val output = super.calculate(input)
		this.input = input
		this.output = output
		return output
	}

	open fun learn(error: Float, learnRate: Float)
	{
		fun <T> error(): T = throw IllegalStateException("Cannot learn without calling calculate() first")
		val input = this.input ?: error()
		val output = this.output ?: error()
		learn(input, output, error, learnRate)
	}
}