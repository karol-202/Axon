package pl.karol202.axon.automation

import kotlinx.coroutines.experimental.yield
import pl.karol202.axon.AxonException
import pl.karol202.axon.network.SupervisedNetwork
import pl.karol202.axon.network.VectorWithResponse
import kotlin.math.abs
import kotlin.math.pow

class Trainer<O>(
		private val network: SupervisedNetwork<*, *, O>,
		var learnRateSupplier: LearnRateSupplier?//Must be not null in order to use train() or trainEpoch() without passing learn rate
)
{
	data class LearningState(
			val lastLearnRate: Float,
			val lastHighestError: Float,
			val lastSumSquaredError: Float
	)

	var vectorListener: ((VectorWithResponse, O, FloatArray) -> Unit)? = null
	var epochListener: ((LearningState) -> Boolean)? = null

	suspend fun train(vectors: List<VectorWithResponse>)
	{
		if(vectors.isEmpty()) throw AxonException("No vectors to learn.")

		val learnRateSupplier = learnRateSupplier ?: throw AxonException("learnRateSupplier cannot be null.")
		val epochListener = epochListener ?: throw AxonException("epochListener cannot be null, because it provides needed feedback.")

		var learningState: LearningState? = null
		var stop = false
		while(!stop)
		{
			trainEpoch(vectors.shuffled(), learnRateSupplier.getLearnRate(learningState), vectorListener) { state ->
				learningState = state
				if(!epochListener(state)) stop = true
			}
		}
	}

	suspend fun trainEpoch(vectors: List<VectorWithResponse>,
	                       learnRate: Float = learnRateSupplier?.getLearnRate(null) ?: throw AxonException("learnRateSupplier cannot be null."),
	                       vectorListener: ((VectorWithResponse, O, FloatArray) -> Unit)? = this.vectorListener,
	                       epochListener: ((LearningState) -> Unit)? = createEpochListenerReturningUnit()
	)
	{
		val errors = vectors.map { vector ->
			val (output, error) = network.learn(vector, learnRate)
			vectorListener?.invoke(vector, output, error)
			yield()
			error
		}
		val highestError = errors.flatMap { it.asIterable() }.maxBy { abs(it) } ?: 0f
		val sumSquaredError = errors.sumByDouble { it.sumByDouble { it.pow(2).toDouble() } }.toFloat() / (network.output * vectors.size)
		epochListener?.invoke(LearningState(learnRate, highestError, sumSquaredError))
	}

	private fun createEpochListenerReturningUnit(): ((LearningState) -> Unit)? = epochListener?.let { globalListener ->
		{ state -> globalListener(state) }
	}
}