package com.cjones.taskforcemamba.helper;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class XmlHandler extends DefaultHandler {
private RssFeedStructure feedStr = new RssFeedStructure();
private List<RssFeedStructure> rssList = new ArrayList<RssFeedStructure>();

private int articlesAdded = 0;

// Number of articles to download
private static final int ARTICLES_LIMIT = 25;

StringBuffer chars = new StringBuffer();

public void startElement(String uri, String localName, String qName, Attributes atts) {
chars = new StringBuffer();

 if (qName.equalsIgnoreCase("media:content"))
	
{
	 if(!atts.getValue("url").toString().equalsIgnoreCase("null")){
	 feedStr.setURLLink(atts.getValue("url").toString());
	 }
	 else{
		 feedStr.setURLLink("");
	 }
}

}

public void endElement(String uri, String localName, String qName) throws SAXException {
if (localName.equalsIgnoreCase("title"))
{
	feedStr.setTitle(chars.toString());
}
else if (localName.equalsIgnoreCase("description"))
{

    feedStr.setDescription(chars.toString());
    if (feedStr != null){
    Log.i("Mamba", "Steam" + feedStr);
    }

}
else if (localName.equalsIgnoreCase("pubDate"))
{

	feedStr.setPubDate(chars.toString());
}
else if (localName.equalsIgnoreCase("encoded"))
{

	feedStr.setEncodedContent(chars.toString());
}
else if (qName.equalsIgnoreCase("media:content"))
	
{
	
}
else if (localName.equalsIgnoreCase("link"))
{
    feedStr.setURLLink(chars.toString());

}
if (localName.equalsIgnoreCase("item")) {
rssList.add(feedStr);

feedStr = new RssFeedStructure();
articlesAdded++;
if (articlesAdded >= ARTICLES_LIMIT)
{
throw new SAXException();
}
}
}
@Override
public void characters(char ch[], int start, int length) {
    if (feedStr != null){

chars.append(new String(ch, start, length));

    }
}


public List<RssFeedStructure> getLatestArticles(String feedUrl) {
URL url = null;
try {

SAXParserFactory spf = SAXParserFactory.newInstance();
SAXParser sp = spf.newSAXParser();
XMLReader xr = sp.getXMLReader();
url = new URL(feedUrl);

xr.setContentHandler(this);
xr.parse(new InputSource(url.openStream()));
} catch (IOException e) {
    Log.i("Task Force Mamba - IO", "IOException = " + e);
} catch (SAXException e) {
    Log.i("Task Force Mamba - SAS", "SASException = " + e);

} catch (ParserConfigurationException e) {
    Log.i("Task Force Mamba - PAR", "ParserConfigurationException = " + e);

}

return rssList;
}

}