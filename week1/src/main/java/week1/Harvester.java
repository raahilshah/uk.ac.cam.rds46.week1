package week1;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.exception.HttpException;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import java.io.FileNotFoundException;

public class Harvester
{
	
	public static ArrayList<String> harvest(String url, String title, String[] filetypes) {
		String[] keywords = title.toLowerCase().split("[ ,;:]");
		ArrayList<String> list = new ArrayList<String>();
		if (url == null) return list;
		try {
			ScraperConfiguration config = new ScraperConfiguration("/Users/Raahil/Documents/workspace/week1/src/main/java/week1/harvest.xml");
			Scraper scraper = new Scraper(config, "/");
			scraper.addVariableToContext("dir", url);
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
			System.out.println("File not found.");
			return null;
		}
		catch (HttpException httpe) {
			System.out.println("Http Exception");
			return new ArrayList<String>();
		}
		catch (IllegalArgumentException illegal) {
			System.out.println("Illegal argument.");
			return new ArrayList<String>();
		}
	}
}
