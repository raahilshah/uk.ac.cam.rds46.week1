package week1;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.exception.HttpException;

import java.util.LinkedList;

import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import java.io.FileNotFoundException;
/*
 * Scrapes a webpage located at 'url' to search for file links matching '.*title.*.filetype'. 
 */
public class Harvester
{

	public static LinkedList<String> harvest(String url, String title, String[] filetypes) throws HttpException {
		LinkedList<String> list = new LinkedList<String>();
		if (url == null || title == null) return list;
		String[] keywords = title.toLowerCase().split("[ ,;:]");
		try {

			ScraperConfiguration config = new ScraperConfiguration("/Users/Raahil/Documents/git/uk.ac.cam.rds46.week1/week1/src/main/java/week1/harvest.xml");
			Scraper scraper = new Scraper(config, "/");
			scraper.addVariableToContext("dir", url);

			// Console log.
			System.out.println(((Variable) scraper.getContext().getVar("dir")).toString());

			scraper.setDebug(true);
			scraper.execute();
			Variable links = (Variable) scraper.getContext().get("temp");
			Object[] arr = links.toArray();
			for (Object s : arr) {
				String x = s.toString().toLowerCase();
				int matchCount =0; double factor = 0.75;
				for (String k : keywords) if (x.contains(k)) matchCount++;
				if (matchCount > (int) (factor * keywords.length)) {
					boolean matches = false;
					for (String ext : filetypes) if (x.contains(ext)) matches = true;
					if (matches) list.add(url + s.toString());
				}
			}
			return list;
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("Scraper Config File not found.");
			return null;
		}
		catch (IllegalArgumentException illegal) {
			System.out.println("Illegal argument.");
			return null;
		}
	}
}
