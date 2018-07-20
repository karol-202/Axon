#Axon
Axon is a easy to use open source library for creating and using simple artificial neural networks.
Axon is fully written in Kotlin.

Axon supports supervised learning, multi layered networks and backpropagation,
but is still under development thus new types of networks and new features will be being added.
Axon is extensible so you can create your own type of network using this library.

##How to install?
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.karol-202:Axon:0.1'
}
```

##How to create?
Axon's network consists of layers which consist of neurons. You can define structure of network
using code like that:
```
val network = basicNetwork(inputs = 3, outputType = RawOutput()) {
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
You can also create neurons with predefined weights what is useful for persistence of data.

##How to use?
When you have already prepared network, you have 2 options: test or train.
Testing means calculating output for given input values.
Training involves calculating output, comparing it with expected response and
modifying weights of neurons according to the error of the network.

You can test the network using calculate() function and passing a vector
which is a set of data used by one test or train operation:
```
val vector = Vector(floatArrayOf(0.4f, -1f, 0.1f))
val output: FloatArray = network.calculate(vector)
```

You can train the network using learn() function and passing a learn rate and vector with response
that besides input data has expected output data:
```
val vector = VectorWithResponse(floatArrayOf(0.4f, -1f, 0.1f), floatArrayOf(0.75f))
val output: FloatArray = network.learn(vector, 0.1f)
```