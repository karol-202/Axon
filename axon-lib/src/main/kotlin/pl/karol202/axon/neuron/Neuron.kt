package pl.karol202.axon.neuron

interface Neuron
{
	val inputs: Int
	val neuronData: NeuronData

	fun calculate(input: FloatArray): Float
}

interface BackpropagationNeuron : Neuron
{
	fun backpropagateErrorForInput(error: Float, input: Int): Float
}

interface SupervisedNeuron : BackpropagationNeuron
{
	fun learn(input: FloatArray, output: Float, error: Float, learnRate: Float)
}