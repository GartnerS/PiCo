package com.nex.blub.PiCo;

import android.os.AsyncTask;
import android.util.Log;

import com.nex.blub.PiCo.interfaces.HasHistoryData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * API-Klasse, die HTTP-Requests an den Pimatic-Server sendet und das Ergebnis zurückliefern
 */
public class API extends AsyncTask<String, Void, String>  {

    // Log-Tag für diese Klasse
    private static final String TAG = "API";

    // Die IP-Adresse des Pimatic-Servers
    private static final String SERVER_IP = "192.168.0.20";

    // Port für die API-Anfrage
    private Integer Port = 80;

    // Wenn != null, dann wird nach einem Request die Methode "receiveResult(string)" des Objekts aufgerufen.
    private HasHistoryData callBackObj;


    //  Referenz, auf einen DeviceUpdater, der mit dem Ergebnis der API-Abfrage benachrichtig wird
    private DeviceUpdater deviceUpdater;


    /**
     * Konstruktor
     */
    public API(){}


    /**
     * Konstruktor
     *
     * @param deviceUpdater Updater dessen Methode "receiveResult(string)"  nach einem Request
     *                      aufgerufen werden soll
     */
    public API(DeviceUpdater deviceUpdater) {
        this.deviceUpdater = deviceUpdater;
    }


    /**
     * Konstruktor
     *
     * @param callBackObj Device-Objekt dessen Methode "receiveResult(string)"  nach einem Request
     *                    aufgerufen werden soll
     * @param useHistoryAPI Flag, die angibt, ob die API für die historischen Daten verwerndet werden soll
     */
    public API(HasHistoryData callBackObj, boolean useHistoryAPI) {
        this.callBackObj = callBackObj;
        this.Port = (useHistoryAPI ? 8181 : 80);
    }


    protected String doInBackground(String... urls) {
        try {
            String URL = "http://" + SERVER_IP + ":" + this.Port + "/" + urls[0];
            return this.doRequest(URL);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String doRequest(String myurl) throws IOException {

        Log.d(TAG, "URL ist: " + myurl);
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            if (conn.getResponseCode() != 200) {
                Log.d(TAG, "Status des HTTP-Aufrufs: " + conn.getResponseCode());
                return null;
            }

            is = conn.getInputStream();

            // Convert the InputStream into a string
            return this.readIt(is);

            // Makes sure that the InputStream is closed after the app is finished using it.
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.toString());
            return null;
        }

        finally {
            if (is != null) {
                is.close();
            }
        }
    }


    // Reads an InputStream and converts it to a String.
    private String readIt(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        while((line = reader.readLine()) != null){
            builder.append(line);
        }

        return builder.toString();
    }


    protected void onPostExecute(String result) {
        // Falls ein Callback-Objekt registriert wurde
        if (this.callBackObj != null) {
            this.callBackObj.receiveHistoryResult(result);
            return;
        }

        if (this.deviceUpdater != null) {
            this.deviceUpdater.receiveResults(result);
        }
    }
}
