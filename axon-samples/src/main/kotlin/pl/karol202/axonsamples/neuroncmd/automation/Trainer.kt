package pl.karol202.axonsamples.neuroncmd.automation

import kotlinx.coroutines.yield
import pl.karol202.axon.network.StandardSupervisedNetwork
import pl.karol202.axon.util.minus
import pl.karol202.axonsamples.neuroncmd.VectorWithResponse
import kotlin.math.abs
import kotlin.math.pow

class Trainer(private val network: StandardSupervisedNetwork,
              private val learnRateSupplier: (LearningState?) -> Float,//Must be not null in order to use train() or trainEpoch() without passing learn rate
              private val vectorListener: (vector: VectorWithResponse, output: FloatArray, error: FloatArray) -> Unit,
              private val epochListener: (LearningState) -> Boolean)
{
	data class LearningState(val lastLearnRate: Float,
	                         val lastHighestError: Float,
	                         val lastSumSquaredError: Float)



	suspend fun train(vectors: List<VectorWithResponse>)
	{
		if(vectors.isEmpty()) throw IllegalArgumentException("No vectors to learn.")

		var learningState: LearningState? = null
		var stop = false
		while(!stop)
		{
			trainEpoch(vectors.shuffled(), learnRateSupplier(learningState), vectorListener) { state ->
				learningState = state
				if(!epochListener(state)) stop = true
			}
		}
	}

	suspend fun trainEpoch(vectors: List<VectorWithResponse>,
	                       learnRate: Float = learnRateSupplier(null),
	                       vectorListener: (VectorWithResponse, FloatArray, FloatArray) -> Unit = this.vectorListener,
	                       epochListener: (LearningState) -> Unit = { this.epochListener(it) })
	{
		val errors = vectors.map { vector ->
			val output = network.calculate(vector.input)
			val error = vector.output - output
			network.learn(error = error, learnRate = learnRate)
			vectorListener(vector, output, error)
			yield()
			error
		}
		val highestError = errors.flatMap { it.asIterable() }.maxBy { abs(it) } ?: 0f
		val sumSquaredError = errors.sumByDouble { it.sumByDouble { it.pow(2).toDouble() } }.toFloat() / (network.outputs * vectors.size)
		epochListener(LearningState(learnRate, highestError, sumSquaredError))
	}
}