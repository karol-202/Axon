package pl.karol202.axonsamples.neuroncmd

import java.util.*
import kotlin.concurrent.thread

class InputWait
{
	fun waitForInput(scanner: Scanner, listener: () -> Unit)
	{
		thread {
			scanner.nextLine()
			listener()
		}
	}
}