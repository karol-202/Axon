package pl.karol202.axon

import java.util.*

open class Vector(
		val inputs: FloatArray
) {
	override fun equals(other: Any?) = when
	{
		this === other -> true
		javaClass != other?.javaClass -> false
		else -> Arrays.equals(inputs, (other as Vector).inputs)
	}

	override fun hashCode() = Arrays.hashCode(inputs)
}

class VectorWithResponse(
		inputs: FloatArray,
		val outputs: FloatArray
) : Vector(inputs) {
	override fun equals(other: Any?) = when
	{
		this === other -> true
		javaClass != other?.javaClass -> false
		!super.equals(other) -> false
		else -> Arrays.equals(outputs, (other as VectorWithResponse).outputs)
	}

	override fun hashCode() = 31 * super.hashCode() + Arrays.hashCode(outputs)
}