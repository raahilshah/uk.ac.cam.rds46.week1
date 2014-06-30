package week1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;

import org.webharvest.exception.HttpException;

import javax.ws.rs.*;

@Path("/json")
public class SearchService
{
	public List<SearchResult> googleSearch (String creator, String title, String[] filetypes) throws IOException {
		if (creator == null || title == null) return new LinkedList<SearchResult>();	
		String query = "intitle:\"index of\"+\"parent directory\" " + creator + " " + title + " (";
		String OR = "|";
		for (String ext : filetypes) query += ext + OR;
		query = query.substring(0, query.length() - 1) + ")";
		
		// Console log.
		System.out.println(query);
		
		query = URLEncoder.encode(query, "UTF-8");
		
		URL url = new URL(
				"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
						+ "q=" + query + "&userip=USERS-IP-ADDRESS");
		URLConnection connection = url.openConnection();
		String line;
		String res = new String();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null) {
			res += (line);
		}
		System.out.println(System.getProperty("user.dir"));
		int i = 0;
		LinkedList<SearchResult> links = new LinkedList<SearchResult>();
		while ((i = res.indexOf("unescapedUrl", i)) != -1) {
			i += 15;
			String host = res.substring(i, res.indexOf('\"', i));
			links.add(new SearchResult(host, "Google"));
			i += host.length();
		}
		reader.close();
		
		// Console log.
		System.out.println("Engine Results:");
		for (SearchResult s : links) System.out.println(s.getHostLink() + " : " + s.getEngine());
		
		
		for (SearchResult s : links) {
			try {
			s.setFiles(Harvester.harvest(s.getHostLink(), title, filetypes));
			}
			catch (HttpException httpe) {
				s.setFiles(null);
				s.setTimeout(true);
			}
		}
		
		// Console log.
		System.out.println("--Done--");
		
		return links;
	}
	
	@GET
	@Path("/musicsearch")
	@Produces("application/json")
	public List<SearchResult> musicSearch(@QueryParam("artist") String artist, @QueryParam("song") String song) throws IOException {
		String[] filetypes = {"mp3", "wav", "mp4", "m4a"};
		return googleSearch(artist, song, filetypes);
	}
	
	public List<SearchResult> pdfSearch(String author, String title) throws IOException {
		String[] filetypes = {"pdf"};
		return googleSearch(author, title, filetypes);
	}
}
