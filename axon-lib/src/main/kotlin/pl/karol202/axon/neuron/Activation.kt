package pl.karol202.axon.neuron

import kotlin.math.exp
import kotlin.math.tanh

interface Activation
{
	fun calculate(value: Float): Float

	fun calculateDerivative(value: Float): Float
}

class LinearActivation : Activation
{
	override fun calculate(value: Float) = value

	override fun calculateDerivative(value: Float) = 1f
}

class SigmoidalActivation(val alpha: Float) : Activation
{
	override fun calculate(value: Float) = 1 / (1 + exp(-value * alpha))

	override fun calculateDerivative(value: Float) = value * (1 - value) * alpha
}

class TangensoidalActivation(val alpha: Float) : Activation
{
	override fun calculate(value: Float) = tanh(value * alpha)

	override fun calculateDerivative(value: Float) = (1 - (value * value)) * alpha
}