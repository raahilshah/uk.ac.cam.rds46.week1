package week1;

import java.io.IOException;

import javax.ws.rs.core.Response;

import java.util.List;

import javax.ws.rs.*;

import com.googlecode.htmleasy.View;

@Path("/")
public class SearchView
{
	@GET
	public View displaySearch() {
//		String html = "<html><head><title>Music Search</title>"
//				+ "<style>"
//				+ ".center { margin: auto; width: 50%; text-align:center; vertical-align:middle}"
//				+ "</style>"
//				+ "</head>"
//				+ "<body>"
//				+ "<div class=\"center\">"
//				+ "<h1>Music Search</h1><br>"
//				+ "<form action=\"musicsearch\" method=\"post\">"
//				+ "<label> Artist:"
//				+ "<input type=\"text\" name=\"artist\" size=50>"
//				+ "</label><br>"
//				+ "<label> Song:"
//				+ "<input type=\"text\" name=\"song\" size=50>"
//				+ "</label>"
//				+ "<br><br>input type=\"submit\" value=\"Search\"></form>"
//				+ "</div>";
//		return Response.status(200).entity(html).build();
		return new View("/MusicSearch.html");
	}

	@POST
	@Path("/musicsearch")
	public Response postResults(@QueryParam("artist") String artist, @QueryParam("song") String song) throws IOException {

		String string = "";

		SearchService ss = new SearchService();
		List<SearchResult> results = ss.musicSearch(artist.trim(), song.trim());

		boolean noResults = true;
		for (SearchResult r : results) {
			if (!r.noFiles()) noResults = false; 
		}

		if (noResults) string += "No results.";
		else {
			for (SearchResult r : results) {
				if (!r.noFiles())
					for (String link : r.getFiles()) {
						string += "<a href=\"" + link + "\">" + link + "</a>";
						string += "</br>";
					}
			}
		}
		return Response.status(200).entity(string).build();
	}
}