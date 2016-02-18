package sfsubredditfeed.polyesterprogrammer.com.streetfighterfeedapp;

/**
 * Created by Jam Jam James Perey on 2/16/2016.
 * This class will take in the values of the xml file, and will let it be read
 */
public class StoreData {
    private String feedTitle;//this is the title of each item
    private String feedLink; //link to each item

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public void setFeedLink(String feedLink) {
        this.feedLink = feedLink;
    }

    @Override
    public String toString() {
        return "Title: " + getFeedTitle() + "\n" +
                "Link: " + getFeedLink() + "\n";
    }
}
