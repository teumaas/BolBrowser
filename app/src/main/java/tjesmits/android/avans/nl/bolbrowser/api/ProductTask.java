package tjesmits.android.avans.nl.bolbrowser.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import tjesmits.android.avans.nl.bolbrowser.api.interfaces.OnProductAvailable;
import tjesmits.android.avans.nl.bolbrowser.domain.Product;

/**
 * Created by Tom Smits on 9-3-2018.
 */

public class ProductTask extends AsyncTask<String, Void, String>  {

    // Callback
    private OnProductAvailable listener = null;

    // Statics
    private static final String TAG = ProductTask.class.getSimpleName();

    // Constructor, set listener
    public ProductTask(OnProductAvailable listener) {
        this.listener = listener;
    }

    /**
     * doInBackground is de methode waarin de aanroep naar een service op
     * het Internet gedaan wordt.
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String personUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(TAG, "doInBackground - " + personUrl);
            try {
            // Maak een URL object
            URL url = new URL(personUrl);
            // Open een connection op de URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialiseer een HTTP connectie
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(TAG, "doInBackground response = " + response);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }

    /**
     * onPostExecute verwerkt het resultaat uit de doInBackground methode.
     *
     * @param response
     */
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // Check of er een response is
        if(response == null || response == "") {
            Log.e(TAG, "onPostExecute kreeg een lege response!");
            return;
        }

        // Het resultaat is in ons geval een stuk tekst in JSON formaat.
        // Daar moeten we de info die we willen tonen uit filteren (parsen).
        // Dat kan met een JSONObject.
        JSONObject jsonObject;
        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all users and start looping
            JSONArray productsArray = jsonObject.getJSONArray("products");
            for(int idx = 0; idx < productsArray.length(); idx++) {
                // array level objects and get user
                JSONObject productObject = productsArray.getJSONObject(idx);

                // Get title, first and last name
                String id = productObject.getString("id");
                String title = productObject.getString("title");
                String tag = null;
                if (productObject.has("summary")) {
                    tag = productObject.getString("summary");
                }
                else {
                    tag = productObject.getString("specsTag");
                }
                int rating = productObject.getInt("rating");
                String description = productObject.getString("longDescription");

                // Get the price of product and formats text.
                String price = productObject.getJSONObject("offerData").getJSONArray("offers").getJSONObject(0).getString("price");
                String finalPrice = null;

                if (price.endsWith(".0")) {
                    finalPrice = price.replace(".0", ",-");
                }
                else if (price.matches("(?i).*.*"))
                {
                    finalPrice = price.replace(".", ",");
                }

                Log.i(TAG, "Got product " + id + " " + title);

                // Get image url
                String imageThumbURL = productObject.getJSONArray("images").getJSONObject(0).getString("url");
                String imageURL = productObject.getJSONArray("images").getJSONObject(1).getString("url");
                Log.i(TAG, imageURL);

                // Create new Product object
                // Builder Design Pattern
                Product product = new Product.ProductBuilder(title, tag, rating, finalPrice)
                        .setID(id)
                        .setDescription(description)
                        .setImageURL(imageThumbURL, imageURL)
                        .build();

                //
                // call back with new product data
                //

                listener.OnProductAvailable(product);
            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }

    //
    // convert InputStream to String
    //
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

}