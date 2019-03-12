package pl.karol202.axonsamples.neuroncmd

import java.util.*

open class Vector(val input: FloatArray)
{
	override fun equals(other: Any?) = when
	{
		this === other -> true
		javaClass != other?.javaClass -> false
		else -> Arrays.equals(input, (other as Vector).input)
	}

	override fun hashCode() = Arrays.hashCode(input)
}

class VectorWithResponse(input: FloatArray,
                         val output: FloatArray) : Vector(input)
{
	override fun equals(other: Any?) = when
	{
		this === other -> true
		javaClass != other?.javaClass -> false
		!super.equals(other) -> false
		else -> Arrays.equals(output, (other as VectorWithResponse).output)
	}

	override fun hashCode() = 31 * super.hashCode() + Arrays.hashCode(output)
}