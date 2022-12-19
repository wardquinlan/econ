package econ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Importer {
	private static Log log = LogFactory.getFactory().getInstance(Importer.class);
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			 String line;
			 while ((line = reader.readLine()) != null) {
				 System.out.println(line);
			 }
		} catch(IOException e) {
			
		}
	}
}
