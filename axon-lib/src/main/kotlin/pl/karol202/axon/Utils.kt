package pl.karol202.axon

import java.util.*

typealias FloatRange = ClosedFloatingPointRange<Float>

fun FloatRange.randomNonZero(): Float = (start + Random().nextFloat() * (endInclusive - start)).let {
	if(it != 0f) it else randomNonZero()
}

operator fun FloatArray.minus(other: FloatArray): FloatArray
{
	if(size != other.size) throw IllegalArgumentException("Invalid array size: $size and ${other.size}")
	return mapIndexed { i, value -> value - other[i] }.toFloatArray()
}