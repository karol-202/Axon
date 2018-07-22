package pl.karol202.axon.automation

//Determines whether training can stop
interface TrainingStop
{
	fun shouldStop(state: Trainer.LearningState?): Boolean
}

class TrainingStopHighestError(val thresholdError: Float) : TrainingStop
{
	override fun shouldStop(state: Trainer.LearningState?): Boolean
	{
		if(state == null) return false
		return state.lastHighestError < thresholdError
	}
}

class TrainingStopSumSquaredError(val thresholdError: Float) : TrainingStop
{
	override fun shouldStop(state: Trainer.LearningState?): Boolean
	{
		if(state == null) return false
		return state.lastSumSquaredError < thresholdError
	}
}