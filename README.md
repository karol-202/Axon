# Axon
Axon is a easy to use open source library for creating and using simple artificial neural networks.
Axon is fully written in Kotlin.

Axon supports supervised learning, multi layered networks and backpropagation,
but is still under development thus new types of networks and new features will be being added.
Axon is extensible so you can create your own type of network using this library.

## How to install?
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.karol-202:Axon:0.2.1'
}
```

## How to use?
Axon's network consists of layers which consist of neurons.

### Creating network
You can define structure of network
using code like that:
```
val network = basicNetwork(inputs = 3, outputType = RawOutput(), null) {
    basicLayer { 
        repeat(10) { basicNeuron(activation = SigmoidalActivation(1f)) }
    }
    basicLayer { 
        basicNeuron(activation = SigmoidalActivation(0.8f))
    }
}.create()
```
While creating network you have to specify amount of inputs (amount of neurons in implicit input layer)
and output type, which defines way the output data from last layer (array of numbers) is converted to
output value from network.

Each neuron has its activation function that you have to choose while defining a neuron.

### Training and testing
When you have already prepared network, you have two options: test or train.
Testing means calculating output for given input values.
Training involves calculating output, comparing it with expected response and
modifying weights of neurons according to the error of the network.

To test network, you should create instance of Tester. Tester has one method - test() which accepts
list of vectors (each vector consists of array of input values and optionally
of array of expected output values) and listener called after testing each vector.

**Tester.test() is a suspend method thus should be called from coroutine or other suspend method**
```
val vector1 = Vector(floatArrayOf(0.4f, -1f, 0.1f))
val vector2 = Vector(floatArrayOf(0f, 0.2f, 0.95f))
val vectors = listOf(vector1, vector2)

val tester = Tester(network)
runBlocking {
    tester.test(vectors) { vector, output ->
        print("Output: ")
        output.forEach { print("$it ") }
        println()
    }
}
```

To train network, you should create instance of Trainer.
Constructor of Trainer besides network needs learn rate supplier and training stop object.

The first one determines current learn rate used to train network. You can use one of predefined
suppliers (currently the only one that is available gives constant learn rate) or create your own
by extending LearnRateSupplier interface.

The second one, TrainingStop, is used in continuous mode to determine whether training can stop.
You can use one of predefined (currently there are three of them available: never stop,
stop when highest error is lower than given threshold and stop when sum squared error is lower than
given threshold) or create your own by extending TrainingStop interface.

Trainer has two ways of use:
1. One epoch training (trainEpoch()) - trains network using all of supplied vectors
2. Continuous training (train()) - trains network as long as TrainingStop' shouldStop() method
returns false
```
val vector1 = VectorWithResponse(floatArrayOf(0.4f, -1f, 0.1f), floatArrayOf(0.75f))
val vector2 = VectorWithResponse(floatArrayOf(0f, 0.2f, 0.95f), floatArrayOf(-0.1f))
val vectors = listOf(vector1, vector2)

val trainer = Trainer(network, ConstantLearnRate(0.1f), TrainingStopNever())
trainer.epochListener = { state ->
    println("Sum squared error: ${state.lastSumSquaredError}")
}

val trainJob = async {
    trainer.train(vectors)
}
runBlocking {
    scanner.nextLine()
    scanner.nextLine()
    trainJob.cancelAndJoin()
}
```