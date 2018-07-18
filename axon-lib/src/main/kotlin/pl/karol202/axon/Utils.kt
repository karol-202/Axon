package pl.karol202.axon

import java.util.*

typealias FloatRange = ClosedFloatingPointRange<Float>

fun FloatRange.randomNonZero(): Float = (start + Random().nextFloat() * (endInclusive - start)).let {
	if(it != 0f) it else randomNonZero()
}