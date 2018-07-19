package pl.karol202.axon.neuron

interface BackpropagationNeuron : Neuron
{
	fun backpropagateErrorForInput(error: Float, input: Int): Float
}