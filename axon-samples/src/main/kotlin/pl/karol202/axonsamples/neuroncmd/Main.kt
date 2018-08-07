package pl.karol202.axonsamples.neuroncmd

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.runBlocking
import pl.karol202.axon.automation.ConstantLearnRate
import pl.karol202.axon.automation.Tester
import pl.karol202.axon.automation.Trainer
import pl.karol202.axon.layer.basicLayer
import pl.karol202.axon.network.RawOutput
import pl.karol202.axon.network.Vector
import pl.karol202.axon.network.VectorWithResponse
import pl.karol202.axon.network.basicNetwork
import pl.karol202.axon.neuron.SigmoidalActivation
import pl.karol202.axon.neuron.basicNeuron
import java.io.File
import java.util.*

fun main(args: Array<String>) = Main().mainMenu()

class Main
{
	private val vectorsFile = File("res/neuroncmd/vectors.dat")
	private val dataFile = File("res/neuroncmd/data.dat")

	private val vectors: ArrayList<VectorWithResponse> = VectorsSave.loadVector(vectorsFile) ?: throw Exception("Cannot load vectors")
	private val network = basicNetwork(22, RawOutput(), DataLoader.loadNetworkData(dataFile)) {
		basicLayer {
			repeat(20) { basicNeuron(SigmoidalActivation(1f)) }
		}
		basicLayer {
			repeat(3) { basicNeuron(SigmoidalActivation(1f)) }
		}
	}.create()

	private val scanner = Scanner(System.`in`)

	fun mainMenu()
	{
		loop@ while(true)
		{
			println("Wybierz czynność:")
			println("1. Tryb automatyczny")
			println("2. Tryb ręczny")
			println("3. Wypisz wagi")
			println("4. Resetuj sieć")
			println("5. Wyjdź")
			when(scanner.nextInt())
			{
				1 -> modeAuto()
				2 -> modeManual()
				3 -> dumpWeights()
				4 -> resetWeights()
				5 -> break@loop
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
		val trainer = Trainer(network, null)
		trainer.vectorListener = { _, _, error ->
			print("Błąd: ")
			error.forEach { print("$it ") }
			println()
		}
		runBlocking {
			trainer.trainEpoch(vectors, 0.1f)
		}
		println("Uczenie zakończone")
		saveNetworkData()
	}

	private fun autoTrainContinuous()
	{
		val trainer = Trainer(network, ConstantLearnRate(0.1f))
		trainer.epochListener = { state ->
			println("Błąd średniokwadratowy: ${state.lastSumSquaredError}")
			saveNetworkData()
			true
		}

		val trainJob = async {
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
			vector.inputs.forEach { print("$it ") }
			println()

			print("  Oczekiwane wyjścia: ")
			vector.outputs.forEach { print("$it ") }
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
		println("Podaj wejścia (${inputs}):")
		val input = (0..inputs).map { scanner.nextFloat() }.toFloatArray()

		val outputs = network.output
		println("Podaj oczekiwane wyjścia ($outputs)")
		val output = (0..outputs).map { scanner.nextFloat() }.toFloatArray()

		val trainer = Trainer(network, null)
		runBlocking {
			trainer.trainEpoch(listOf(VectorWithResponse(input, output)), 0.1f, { _, _, error ->
				print("Błąd: ")
				error.forEach { print("$it ") }
				println()
				saveNetworkData()
			}, null)
		}
	}

	private fun manualTest()
	{
		val inputs = network.inputs
		println("Podaj wejścia (${inputs}):")
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
		network.getNetworkData().forEachIndexed { i, layer ->
			println("  Warstwa $i:")
			layer.forEachIndexed { j, neuronData ->
				print("    Neuron $j: ")
				neuronData.weights.forEach { print("$it ") }
				println()
			}
		}
	}

	private fun resetWeights()
	{
		network.randomize(-0.1f..0.1f)
		saveNetworkData()
	}

	private fun saveNetworkData()
	{
		DataLoader.saveNetworkData(dataFile, network.getNetworkData())
	}
}