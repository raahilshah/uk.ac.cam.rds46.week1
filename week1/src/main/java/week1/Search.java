package week1;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.net.HttpURLConnection;

import javax.ws.rs.*;

@Path("/")
public class Search
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
	@Path("/search")
	public Response getResults(@FormParam("artist") String artist, @FormParam("song") String song) throws IOException {

		String string = "<html>", q = artist + " " + song;
		string += "<head><title>Search Results</title></head>";
		string += "<body><h1>Search Results</h1>";
		String[] filetypes = {"mp3", "wav", "mp4", "m4a"};
		String temp = "intitle:\"index of\"+\"parent directory\"" + q + " mp3", query = URLEncoder.encode(temp);
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
		int i = 0, j = 0;
		String[] links = new String[4];
		while ((i = res.indexOf("unescapedUrl", i)) != -1) {
			i += 15;
			links[j] = (res.substring(i, res.indexOf('\"', i)));
			i += links[j].length();
			j++;
		}
		reader.close();
		boolean noResults = true;
		for (String s : links) {
			ArrayList<String> l = Harvester.harvest(s, song, filetypes);
			if (!l.isEmpty()) noResults = false;
			for (String x : l) {
				string += "<a href=\"" + x + "\">" + x + "</a><br>";
			}
		}
		
		if (noResults) string += "No results.";

		string += "</body></html>";
		return Response.status(200).entity(string).build();
	}

	private String processLink(URL u, String song) throws IOException {
		if (u == null) return "Null Link";
		String ans = "", res = "";

		try {
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			HttpURLConnection.setFollowRedirects(false);
			huc.setConnectTimeout(15 * 1000);
			huc.setRequestMethod("GET");
			huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			huc.connect();
			huc.getInputStream();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(huc.getInputStream()));
			while((line = reader.readLine()) != null) {
				res += (line);
			}
		}
		catch(SocketTimeoutException ste) {
			return "";
		}
		catch(IOException ioe) { return ""; }
		String lRes = res.toLowerCase();

		if (!lRes.contains("index of")) return "No Index of";
		int i = 0;
		while ((i = lRes.indexOf("href=\"", i)) != -1) {
			i += 6;
			int j = lRes.indexOf("\"", i);
			String file = lRes.substring(i, j);
			if (file.contains(song) && file.contains(".mp3")) {
				String f = u.toString() + res.substring(i, j);
				ans += "<a href=\"" + (f) + "\">" + (f) + "</a><br>";
			}
			i += file.length();
		}
		return ans;
	}
}
