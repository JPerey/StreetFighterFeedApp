package sfsubredditfeed.polyesterprogrammer.com.streetfighterfeedapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Jam Jam James Perey on 2/16/2016.
 * This will go through the read xml file, and every time it reaches a tag that I have designated as
 * an important tag, it will send that data to the arraylist and store it for "StoreData" Class
 */
public class DataParser {
    private String xmlData;
    private ArrayList<StoreData>  sfFeedData;

    public DataParser(String xmlData){
        /*what this does:
        creates the ArrayList that will hold data extracted from xml file.
        Takes in the actual data from the xml file into xmlData so that this class can go through it
        */
        this.xmlData = xmlData;
        sfFeedData = new ArrayList<>();

    }

    public ArrayList<StoreData> getSfFeedData() {
        return sfFeedData;
    }

    public boolean process(){
        boolean status = true;
        StoreData currentRecord = null;
        boolean inItem = false;
        String textValue = "";

        try{
            //initializing parser to access the code
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType(); // helps tell the parser when there is a value we want
            // i.e. name, artist, release date, or when there is a new entry


            while(eventType != XmlPullParser.END_DOCUMENT) {// as long as it isn't at the end of the document
                String tagName = xpp.getName(); // will get the tag name from xml files
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        //Log.d("ParseApplications", "Starting tag for " + tagName);
                        if(tagName.equalsIgnoreCase("Item")){
                            inItem = true;
                            currentRecord = new StoreData();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //Log.d("ParseApplications", "ending tag for " + tagName);
                        if(inItem){
                            if(tagName.equalsIgnoreCase("item")){
                                sfFeedData.add(currentRecord); // this will save all the data we've pulled from one entry so that it is now part of the array
                                inItem=false;

                            }else if(tagName.equalsIgnoreCase("title")){
                                currentRecord.setFeedTitle(textValue);
                            }else if(tagName.equalsIgnoreCase("link")){
                                currentRecord.setFeedLink(textValue);
                            //}else if(tagName.equalsIgnoreCase("releaseDate")){
                              //  currentRecord.setReleaseDate(textValue);
                            }

                        }
                        break;

                }
                eventType = xpp.next();
            }

        }catch(Exception e){
            status = false;
            e.printStackTrace();
        }

        for(StoreData app: sfFeedData){
            Log.d("ParseApplications", "**********");
            Log.d("ParseApplications", "Title: " + app.getFeedTitle());
            Log.d("ParseApplications", "Link: " + app.getFeedLink());
            //Log.d("ParseApplications", "Release Date: " + app.getReleaseDate());
        }




        return true;
    }


}
