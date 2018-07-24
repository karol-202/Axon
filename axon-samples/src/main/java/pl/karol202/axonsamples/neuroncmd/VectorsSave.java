package pl.karol202.axonsamples.neuroncmd;

import pl.karol202.axon.network.VectorWithResponse;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

//TODO Replace this old, awful code with a class in Kotlin
public class VectorsSave
{
	private enum VectorsRS
	{
		NONE, VECTORS, VECTOR, INPUT, REQ_OUTPUT
	}

	static ArrayList<VectorWithResponse> loadVector(File file) throws XMLStreamException, FileNotFoundException
	{
		if(!file.exists()) return null;
		FileInputStream fis = new FileInputStream(file);
		XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(fis);

		VectorsRS level = VectorsRS.NONE;
		ArrayList<VectorWithResponse> vectors = null;
		ArrayList<Float> inputs = null;
		ArrayList<Float> outputs = null;
		float input = 0;
		float output = 0;

		while(reader.hasNext())
		{
			XMLEvent event = reader.nextEvent();
			if(event.isStartElement())
			{
				StartElement startElement = event.asStartElement();
				if(startElement.getName().toString().equals("vectors"))
				{
					if(level != VectorsRS.NONE)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: vectors");
					level = VectorsRS.VECTORS;

					vectors = new ArrayList<>();
				}
				else if(startElement.getName().toString().equals("vector"))
				{
					if(level != VectorsRS.VECTORS)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: vector");
					level = VectorsRS.VECTOR;

					inputs = new ArrayList<>();
					outputs = new ArrayList<>();
				}
				else if(startElement.getName().toString().equals("input"))
				{
					if(level != VectorsRS.VECTOR)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: input");
					level = VectorsRS.INPUT;
				}
				else if(startElement.getName().toString().equals("reqOutput"))
				{
					if(level != VectorsRS.VECTOR)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: reqOutput");
					level = VectorsRS.REQ_OUTPUT;
				}
			}
			else if(event.isEndElement())
			{
				EndElement endElement = event.asEndElement();
				if(endElement.getName().toString().equals("vectors"))
				{
					if(level != VectorsRS.VECTORS)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: vectors");
					break;
				}
				else if(endElement.getName().toString().equals("vector"))
				{
					if(level != VectorsRS.VECTOR)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: vector");
					level = VectorsRS.VECTORS;

					//if(inputs.size() != network.getInputsAmount())
					// throw new RuntimeException("Bład parsowania pliku: nieprawidłowa ilość wejść wektora.");
					float[] inputsArray = new float[inputs.size()];
					for(int i = 0; i < inputs.size(); i++) inputsArray[i] = inputs.get(i);

					//if(outputs.size() != network.getOutputsAmount())
					// throw new RuntimeException("Bład parsowania pliku: nieprawidłowa ilość wyjść wektora.");
					float[] outputsArray = new float[outputs.size()];
					for(int i = 0; i < outputs.size(); i++) outputsArray[i] = outputs.get(i);

					vectors.add(new VectorWithResponse(inputsArray, outputsArray));
					inputs = null;
					outputs = null;
				}
				else if(endElement.getName().toString().equals("input"))
				{
					if(level != VectorsRS.INPUT)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: input");
					level = VectorsRS.VECTOR;
					inputs.add(input);
					input = 0;
				}
				else if(endElement.getName().toString().equals("reqOutput"))
				{
					if(level != VectorsRS.REQ_OUTPUT)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: reqOutput");
					level = VectorsRS.VECTOR;
					outputs.add(output);
					output = 0;
				}
			}
			else if(event.isCharacters())
			{
				Characters chars = event.asCharacters();
				if(level == VectorsRS.INPUT) input = Float.parseFloat(chars.getData());
				if(level == VectorsRS.REQ_OUTPUT) output = Float.parseFloat(chars.getData());
			}
		}
		reader.close();
		return vectors;
	}
}