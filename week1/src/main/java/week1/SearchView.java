package week1;

import java.io.IOException;
import javax.ws.rs.core.Response;

import java.util.List;

import javax.ws.rs.*;

@Path("/")
public class SearchView
{
	@GET
	public Response displaySearch() {
		String html = "<html><head><title>Music Search</title>"
				+ "<style>"
				+ ".center { margin: auto; width: 50%; text-align:center; vertical-align:middle}"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<div class=\"center\">"
				+ "<h1>Music Search</h1><br>"
				+ "<form action=\"search\" method=\"post\">"
				+ "<label> Artist:"
				+ "<input type=\"text\" name=\"artist\" size=50>"
				+ "</label><br>"
				+ "<label> Song:"
				+ "<input type=\"text\" name=\"song\" size=50>"
				+ "</label>"
				+ "<br><br><input type=\"submit\" value=\"Search\"></form>"
				+ "</div>";
		return Response.status(200).entity(html).build();
	}

	@POST
	@Path("/musicsearch")
	public Response postResults(@FormParam("artist") String artist, @FormParam("song") String song) throws IOException {

		String string = "<html>";
		string += "<head><title>Search Results</title></head>";
		string += "<body><h1>Search Results</h1>";
		
		SearchService ss = new SearchService();
		List<SearchResult> results = ss.musicSearch(artist.trim(), song.trim());
		
		boolean noResults = true;
		for (SearchResult r : results) {
			if (!r.noFiles()) noResults = false; 
		}
		
		if (noResults) string += "No results.";
		else {
			for (SearchResult r : results) {
				for (String link : r.getFiles()) {
					string += "<a href=\"" + link + "\">" + link + "</a>";
					string += "</br>";
				}
			}
		}

		string += "</body></html>";
		return Response.status(200).entity(string).build();
	}
}