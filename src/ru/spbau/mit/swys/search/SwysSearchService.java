package ru.spbau.mit.swys.search;

import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.spbau.mit.swys.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SwysSearchService extends SearchService {
    private HttpClient client = new DefaultHttpClient();
    private Context context;

    public SwysSearchService(Context context) {
        this.context = context;
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    }

    private String getSearchMethodUrl() {
        return context.getString(R.string.API_URL) + "/" + context.getString(R.string.API_SEARCH_METHOD);
    }

    @Override
    protected void fillSearchResult(Image im, SearchResult result) throws SearchQueryException {
        HttpPost post = new HttpPost(getSearchMethodUrl());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addPart("image", new FileBody(im.getFile()));

        post.setEntity(builder.build());

        HttpResponse response = null;

        try {
            response = client.execute(post);
        } catch (ClientProtocolException e) {
            throw new SearchQueryException("Client: " + e.getMessage());
        } catch (IOException e) {
            throw new SearchQueryException("IO: " + e.getMessage());
        }

        fillResultFromJSON(readJSONFromResponse(response), result);
    }

    private String readJSONFromResponse(HttpResponse response) throws SearchQueryException {
        try {
            HttpEntity entity = response.getEntity();

            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (IOException e) {
            throw new SearchQueryException("IO: " + e.getMessage());
        }
    }

    private void fillResultFromJSON(String jsonString, SearchResult result) throws SearchQueryException {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getInt("result") != 1) {
                throw new SearchQueryException(context.getString(R.string.search_query_unsuccessful_result));
            }

            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject imgObj = data.getJSONObject(i);

                result.addItem(new SearchResultItem(
                        imgObj.getString("title"),
                        imgObj.getString("image_url"),
                        imgObj.getString("description_url")
                ));
            }

        } catch (JSONException e) {
            throw new SearchQueryException("JSON: " + e.getMessage());
        }
    }
}
