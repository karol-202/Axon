package pl.karol202.axon.automation

interface LearnRateSupplier
{
	fun getLearnRate(state: Trainer.LearningState?): Float
}

class ConstantLearnRate(val learnRate: Float) : LearnRateSupplier
{
	override fun getLearnRate(state: Trainer.LearningState?) = learnRate
}