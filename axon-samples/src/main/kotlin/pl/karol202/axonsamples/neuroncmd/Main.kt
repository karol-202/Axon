package pl.karol202.axonsamples.neuroncmd

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import pl.karol202.axon.layer.standardSupervisedNeuron
import pl.karol202.axon.network.standardSupervisedLayer
import pl.karol202.axon.network.standardSupervisedNetwork
import pl.karol202.axon.neuron.SigmoidalActivation
import pl.karol202.axon.specification.createNetworkRandomly
import pl.karol202.axonsamples.neuroncmd.automation.Tester
import pl.karol202.axonsamples.neuroncmd.automation.Trainer
import java.io.File
import java.util.*

fun main() = Main().mainMenu()

class Main
{
	private val vectorsFile = File("res/neuroncmd/vectors.dat")
	private val dataFile = File("res/neuroncmd/data.dat")

	private val vectors = VectorsSave.loadVector(vectorsFile) ?: throw Exception("Cannot load vectors")
	private val network = standardSupervisedNetwork(22) {
		standardSupervisedLayer {
			repeat(20) { standardSupervisedNeuron(SigmoidalActivation(1f)) }
		}
		standardSupervisedLayer {
			repeat(3) { standardSupervisedNeuron(SigmoidalActivation(1f)) }
		}
	}.createNetworkRandomly(-0.1f..0.1f)

	private val scanner = Scanner(System.`in`)

	fun mainMenu()
	{
		loop@ while(true)
		{
			println("Wybierz czynność:")
			println("1. Tryb automatyczny")
			println("2. Tryb ręczny")
			println("3. Wypisz wagi")
			println("4. Wyjdź")
			when(scanner.nextInt())
			{
				1 -> modeAuto()
				2 -> modeManual()
				3 -> dumpWeights()
				4 -> break@loop
				else -> println("Niewłaściwy wybór")
			}
		}
	}

	private fun modeAuto()
	{
		loop@ while(true)
		{
			println("Tryb automatyczny:")
			println("1. Trenuj raz")
			println("2. Trenuj do skutku")
			println("3. Testuj")
			println("4. Wypisz wektory")
			println("5. Wyjdź")
			when(scanner.nextInt())
			{
				1 -> autoTrainOnce()
				2 -> autoTrainContinuous()
				3 -> autoTest()
				4 -> dumpVectors()
				5 -> break@loop
			}
		}
	}

	private fun autoTrainOnce()
	{
		val vectorListener = { _: VectorWithResponse, _: FloatArray, error: FloatArray ->
			print("Błąd: ")
			error.forEach { print("$it ") }
			println()
		}
		val trainer = Trainer(network, { 0.1f }, vectorListener, { true })
		runBlocking {
			trainer.trainEpoch(vectors)
		}
		println("Uczenie zakończone")
		saveNetworkData()
	}

	private fun autoTrainContinuous()
	{
		val epochListener = { state: Trainer.LearningState ->
			println("Błąd średniokwadratowy: ${state.lastSumSquaredError}")
			saveNetworkData()
		true
		}

		val trainer = Trainer(network, { 0.1f }, { _, _, _ -> Unit}, epochListener)

		val trainJob = GlobalScope.async {// TODO Remove GlobalScope
			trainer.train(vectors)
		}
		runBlocking {
			scanner.nextLine()
			scanner.nextLine()
			trainJob.cancelAndJoin()
			println("Uczenie zakończone")
		}

		/*var interruptJob: Deferred<Unit>? = null
		val trainJob = async {
			trainer.train(vectors)
		}
		val waitJob = async {
			trainJob.await()
			yield()
			interruptJob?.cancelAndJoin()
			println("Uczenie zakończone")
		}
		interruptJob = async {
			var input = false
			InputWait().waitForInput(scanner) { input = true }
			while(!input) yield()
			scanner.nextLine()

			trainJob.cancelAndJoin()
			yield()
			waitJob.cancelAndJoin()
			println("Uczenie przerwane")
		}
		runBlocking {
			trainJob.join()
			waitJob.join()
			interruptJob.join()
		}*/
	}

	private fun autoTest()
	{
		val tester = Tester(network)
		runBlocking {
			tester.test(vectors) { _, output ->
				print("Wyjście: ")
				output.forEach { print("$it ") }
				println()
			}
		}
	}

	private fun dumpVectors()
	{
		vectors.forEach { vector ->
			println("Wektor:")

			print("  Wejścia: ")
			vector.input.forEach { print("$it ") }
			println()

			print("  Oczekiwane wyjścia: ")
			vector.output.forEach { print("$it ") }
			println()
		}
	}

	private fun modeManual()
	{
		loop@ while(true)
		{
			println("Tryb ręczny:")
			println("1. Trenuj")
			println("2. Testuj")
			println("3. Wyjdź")
			when(scanner.nextInt())
			{
				1 -> manualTrain()
				2 -> manualTest()
				3 -> break@loop
			}
		}
	}

	private fun manualTrain()
	{
		val inputs = network.inputs
		println("Podaj wejścia ($inputs):")
		val input = (0..inputs).map { scanner.nextFloat() }.toFloatArray()

		val outputs = network.outputs
		println("Podaj oczekiwane wyjścia ($outputs)")
		val output = (0..outputs).map { scanner.nextFloat() }.toFloatArray()

		val trainer = Trainer(network, { 0.1f }, { _, _, _ -> Unit }, { true })
		runBlocking {
			trainer.trainEpoch(listOf(VectorWithResponse(input, output)), 0.1f, { _, _, error ->
				print("Błąd: ")
				error.forEach { print("$it ") }
				println()
				saveNetworkData()
			})
		}
	}

	private fun manualTest()
	{
		val inputs = network.inputs
		println("Podaj wejścia ($inputs):")
		val input = (0 until inputs).map { scanner.nextFloat() }.toFloatArray()

		val tester = Tester(network)
		runBlocking {
			tester.test(listOf(Vector(input))) { _, output ->
				print("Wyjście: ")
				output.forEach { print("$it ") }
				println()
			}
		}
	}

	private fun dumpWeights()
	{
		println("Dane sieci:")
		network.networkData.getLayersData().forEachIndexed { i, layer ->
			println("  Warstwa $i:")
			layer.getNeuronsData().forEachIndexed { j, neuronData ->
				print("    Neuron $j: ")
				neuronData.getWeights().forEach { print("$it ") }
				println()
			}
		}
	}

	private fun saveNetworkData()
	{
		DataLoader.saveNetworkData(dataFile, network.networkData)
	}
}
/*
fun waitForInput(scanner: Scanner, listener: () -> Unit)
	{
		thread {
			scanner.nextLine()
			listener()
		}
	}
 */