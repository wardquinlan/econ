package econ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Importer {
	private static Log log = LogFactory.getFactory().getInstance(Importer.class);
	public static void main(String[] args) {
		if (args.length != 2) {
			log.error("Importer: usage: Importer NAME ID");
			System.exit(1);
		}
		String name = args[0];
		int id = 0;
		try {
			id = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			log.error("Importer: usage: Importer NAME ID");
			System.exit(1);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			 String line;
			 while ((line = reader.readLine()) != null) {
				 StringTokenizer st = new StringTokenizer(line, ",");
				 if (!st.hasMoreTokens()) {
					 continue;
				 }
				 String date = st.nextToken();
				 if (date.length() != 8) {
					 log.warn("ignoring invalid date: " + date);
					 continue;
				 }
				 date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
				 
				 if (!st.hasMoreTokens()) {
					 log.warn("ignoring incomplete line: " + line);
					 continue;
				 }
				 String scope = st.nextToken();
				 
				 if (!st.hasMoreTokens()) {
					 log.warn("ignoring incomplete line: " + line);
					 continue;
				 }
				 String label = st.nextToken();
				 if (!name.equals(label)) {
					 log.debug("ignoring unmatched label: " + label);
					 continue;
				 }
				 
				 if (!st.hasMoreTokens()) {
					 log.warn("ignoring incomplete line: " + line);
					 continue;
				 }
				 String value = st.nextToken();
				 
				 if (st.hasMoreTokens()) {
					 log.warn("ignoring invalid line:" + line);
					 continue;
				 }

				 System.out.println("INSERT INTO TIME_SERIES_DATA(ID, DATESTAMP, VALUE) VALUES(" + id + ", '" + date + "', " + value + ");");
			 }
		} catch(IOException e) {
			
		}
	}
}
