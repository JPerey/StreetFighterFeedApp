package sfsubredditfeed.polyesterprogrammer.com.streetfighterfeedapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
/*
DISCLAIMER: If feed is not working, it is possible that the feed creation site (feedity.com) has deleted
the xml file as it has a 7-day window. I will keep re-uploading a new feed, or if possible apply
 for a payment plan for a better feed for this app.
 */
    private Button SFFeedUpdate;
    private ListView SFFeedView;

    private String mFileContents;

    //private TextView xmlTextView;

    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            mFileContents = downloadXMLFile(params[0]);

            if (mFileContents == null) {
                Log.d("DownloadData", "Error downloading");

            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was: " + result);
            //xmlTextView.setText(mFileContents);
        }

        private String downloadXMLFile(String urlPath) {
            StringBuilder tempBuffer = new StringBuilder();
            try {
                //this takes in the url that we want to look in and makes an object with it
                URL url = new URL(urlPath);
                // after creating the url object that will take in the url of the file we want to download
                // we want to create another object that will take that url and actually make the
                //connection to the internet
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d("DownloadData", "the response code was " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];

                while(true){
                    //this will try and read the file into the input buffer automatically
                    charRead = isr.read(inputBuffer);
                    //charRead will keep reading the # of chars in the file, going all the way to 500
                    // and repeat itself until there is no more char's to read
                    if(charRead <=0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead)); // this will copy all the values of the download to inputBuffer
                }
                return tempBuffer.toString();

            } catch (IOException ioE) {
                Log.d("DownloadData", "IO Exception reading data: " + ioE.getMessage());
                ioE.printStackTrace();

            } catch( SecurityException sE){
                Log.d("DownloadData", "Security Exception. Needs Permissions?" + sE.getMessage());
            }
            return null;
        }
    }
    /*
    methods to download and output RSS feed end here
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SFFeedUpdate = (Button) findViewById(R.id.btnUpdate);
        SFFeedView = (ListView) findViewById(R.id.lstvSFView);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("https://feedity.com/reddit-com/VFpVUFZV.rss");

        SFFeedUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add parse activation code
                DataParser parseApplications = new DataParser(mFileContents);
                parseApplications.process();
                ArrayAdapter<StoreData> arrayAdapter = new ArrayAdapter<>(
                        MainActivity.this, R.layout.list_item, parseApplications.getSfFeedData());
                SFFeedView.setAdapter(arrayAdapter);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
