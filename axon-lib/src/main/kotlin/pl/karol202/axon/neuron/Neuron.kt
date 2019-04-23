package pl.karol202.axon.neuron

interface Neuron
{
	val inputs: Int
	val neuronData: NeuronData

	suspend fun calculate(input: FloatArray): Float
}

interface BackpropagationNeuron : Neuron
{
	suspend fun backpropagateErrorForInput(error: Float, input: Int): Float
}

interface SupervisedNeuron : BackpropagationNeuron
{
	suspend fun learn(input: FloatArray, output: Float, error: Float, learnRate: Float)
}