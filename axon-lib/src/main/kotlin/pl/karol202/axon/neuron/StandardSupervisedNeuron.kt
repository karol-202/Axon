package pl.karol202.axon.neuron

import pl.karol202.axon.specification.NeuronSpecification

open class StandardSupervisedNeuron(activation: Activation,
                                    bias: Float,
                                    weights: FloatArray) : AbstractSupervisedNeuron(activation, bias, weights)
{
	class Specification(private val activation: Activation,
	                    private val bias: Float) : NeuronSpecification<StandardSupervisedNeuron>()
	{
		override fun createNeuron(neuronData: NeuronData) =
				StandardSupervisedNeuron(activation, bias, neuronData.getWeights().toFloatArray())
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
		fun error(): Nothing = throw IllegalStateException("Cannot learn without calling calculate() first")

		val input = this.input ?: error()
		val output = this.output ?: error()
		learn(input, output, error, learnRate)
	}
}