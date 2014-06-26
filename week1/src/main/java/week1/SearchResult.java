package week1;

import java.util.List;

public class SearchResult
{
	private String hostLink;
	private String engine;
	private boolean timeout;
	private List<String> files;
	
	public SearchResult (String host, String searchEngine) {
		hostLink = host;
		engine = searchEngine;
	}
	
	public String getHostLink()
	{
		return hostLink;
	}
	public String getEngine()
	{
		return engine;
	}
	public boolean isTimeout()
	{
		return timeout;
	}
	public void setTimeout(boolean timeout)
	{
		this.timeout = timeout;
	}
	public List<String> getFiles()
	{
		return files;
	}
	public void setFiles(List<String> files)
	{
		this.files = files;
	}
	
	public boolean noFiles() { return files.isEmpty(); }
	
}
