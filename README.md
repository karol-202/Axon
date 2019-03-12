# Axon
Axon is a easy to use open source library for creating and using simple artificial neural networks.
Axon is fully written in Kotlin.

Axon supports supervised learning, multi layered networks and backpropagation,
but is still under development thus new types of networks and new features will be added.
Axon is extensible so you can create your own type of network using this library.

## How to install?
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.karol-202:Axon:0.3'
}
```

## How to use?
Axon's network consists of layers which consist of neurons.

### Creating network
You can define structure of network
using code like that:
```
val network = standardSupervisedNetwork(inputs = 3) {
    standardSupervisedLayer { 
        repeat(10) { standardSupervisedNeuron(activation = SigmoidalActivation(1f)) }
    }
    standardSupervisedLayer { 
        standardSupervisedNeuron(activation = SigmoidalActivation(0.8f))
    }
}.createNetworkRandomly(-0.1f..0.1f)
```
While creating network you have to specify amount of inputs (amount of neurons in implicit input layer).

Each neuron has its activation function that you have to choose while defining a neuron.