package com.cjones.taskforcemamba.helper;

import android.util.Log;

import java.net.URL;


public class RssFeedStructure {

private long articleId;
private long feedId;
private String title;
private String description;
private String imgLink;
private String pubDate;
private URL url;
private String encodedContent;
private String URLLink;
private String YouTubeLink;

public long getArticleId() {
return articleId;
}

public void setArticleId(long articleId) {
this.articleId = articleId;
}

public long getFeedId() {
return feedId;
}
/**
* @param feedId the feedId to set
*/
public void setFeedId(long feedId) {
this.feedId = feedId;
}
/**
* @return the title
*/
public String getTitle() {
return title;
}
/**
* @param title the title to set
*/
public void setTitle(String title) {
this.title = title;
}
/**
* @return the url
*/
public URL getUrl() {
return url;
}
/**
* @param url the url to set
*/
public void setUrl(URL url) {
this.url = url;
}
/**
* @param description the description to set
*/
public void setDescription(String description) {
    this.description = description;

    if (description.contains("<img ")){
        String img = description.substring(description.indexOf("<img "));
        String cleanUp = img.substring(0, img.indexOf(">")+1);
        img = cleanUp.substring(img.indexOf("src=") + 5);
        String s = "\" border=\"0\" alt=\"\" />";
        if(img.contains("\" border=\"0\" alt=\"\" />")){
            img = img.replace("\" border=\"0\" alt=\"\" />", "");
        }
        int indexOf = img.indexOf("");
        if (indexOf==-1){
            indexOf = img.indexOf("\"");
        }


        setImgLink(img);

        this.description = this.description.replace(cleanUp, "");
    }
    if (description.contains("src=\"//www.youtube.com/embed/")){
        String YouT = description.substring(description.indexOf("src=\"//www.youtube.com/embed/"));
        String cleanUp = YouT.substring(0, YouT.indexOf("?"));
        YouT = cleanUp.substring(YouT.indexOf("src=//") + 5);
        String s = "\"//www.youtube.com/embed/";
        if(YouT.contains("\"//www.youtube.com/embed/")){
            YouT = YouT.replace("\"//www.youtube.com/embed/", "");
        }
        int indexOf = YouT.indexOf("");
        if (indexOf==-1){
            indexOf = YouT.indexOf("\"");
        }


        setYouTubeLink(YouT);

    }
}
/**
* @return the description
*/
public String getDescription() {
return description;
}
/**
* @param pubDate the pubDate to set
*/

public void setPubDate(String pubDate) {
this.pubDate = pubDate;
}
/**
* @return the pubDate
*/
public String getPubDate() {
return pubDate;
}
/**
* @param encodedContent the encodedContent to set
*/
public void setEncodedContent(String encodedContent) {
this.encodedContent = encodedContent;
}
/**
* @return the encodedContent
*/
public String getEncodedContent() {
return encodedContent;
}
/**
 * @param imgLink the imgLink to set
 */
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
        Log.i("Mamba", "imgLink =" + this.imgLink);
    }
    /**
     * @param YouTubeLink the imgLink to set
     */
    public void setYouTubeLink(String YouTubeLink) {
        this.YouTubeLink = YouTubeLink;
        Log.i("Mamba", "YouTubeLink =" + this.YouTubeLink);
    }
/**
* @return the imgLink
*/
public String getYouTubeLink() {
    return YouTubeLink;
}
public String getImgLink() {
return imgLink;
}
public String getURLLink() {
    return URLLink;
}
public void setURLLink(String URLLink) {
    this.URLLink = URLLink;
}


}


