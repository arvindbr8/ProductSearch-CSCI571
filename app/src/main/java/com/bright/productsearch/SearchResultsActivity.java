package com.bright.productsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressTitle;
    private TextView resultNumberTitle;
    private TextView resultKeywordTitle;
    private RecyclerView searchResultRecyclerView;
    private SearchResultCardAdapter searchResultCardAdapter;
    private LinearLayout topOfResultsLayout;

    private JSONObject wishListItem;

    private List<SearchResultCard> searchResultCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search Results");
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchResultRecyclerView = findViewById(R.id.searchResultView);
        searchResultRecyclerView.setHasFixedSize(true);
        searchResultRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        resultNumberTitle = findViewById(R.id.resultNumTextView);
        resultKeywordTitle = findViewById(R.id.resultKeywordTextView);
        topOfResultsLayout = findViewById(R.id.topOfResultsLayouy);

        searchResultRecyclerView.setVisibility(View.GONE);
        topOfResultsLayout.setVisibility(View.GONE);
        findViewById(R.id.noresults).setVisibility(View.GONE);


        searchResultCards = new ArrayList<>();

        searchResultCardAdapter = new SearchResultCardAdapter(searchResultCards, this);
        searchResultRecyclerView.setAdapter(searchResultCardAdapter);

        searchResultCardAdapter.setOnItemClickListener(new SearchResultCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = searchResultCards.get(position).getId();
                JSONObject shippingdetails = searchResultCards.get(position).getShippingDetail();
                Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("shippingDetails", shippingdetails.toString());
                intent.putExtra("title", searchResultCards.get(position).getProductFullTitle());
                wishListItem = new JSONObject();
                try {
                    wishListItem.put("title", searchResultCards.get(position).getProductTitle());
                    wishListItem.put("imageURL", searchResultCards.get(position).getProductImageURL());
                    wishListItem.put("zip", searchResultCards.get(position).getZipcode());
                    wishListItem.put("shipping", searchResultCards.get(position).getShipping());
                    wishListItem.put("condition", searchResultCards.get(position).getCondition());
                    wishListItem.put("price", searchResultCards.get(position).getPrice());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("wishList", wishListItem.toString());

                startActivity(intent);
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressTitle = findViewById(R.id.progressTitle);

        progressBar.setVisibility(View.VISIBLE);
        progressTitle.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("URL")) {

            String url = getIntent().getExtras().getString("URL");

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        resultNumberTitle.setText(String.valueOf(response.length()) + " ");
                        Log.d("keyword", getIntent().getExtras().getString("keyword"));
                        resultKeywordTitle.setText(getIntent().getExtras().getString("keyword"));

                        if (response.length() == 0) {
                            findViewById(R.id.noresults).setVisibility(View.VISIBLE);
                            return;
                        }
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject searchResultIter = response.getJSONObject(i).getJSONObject("searchResult");
                            SearchResultCard searchResultCard = new SearchResultCard(
                                    searchResultIter.getString("Image"),
                                    searchResultIter.getString("Title"),
                                    (searchResultIter.has("FullTitle") ? searchResultIter.getString("FullTitle") : searchResultIter.getString("Title")),
                                    searchResultIter.getString("Zip"),
                                    searchResultIter.getString("Shipping"),
//                                    isInCart(searchResultIter.getString("Id"),
                                    "cart_plus.xml",
                                    searchResultIter.getString("Condition"), //need to implement condition,
                                    "$" + searchResultIter.getString("Price"),
                                    searchResultIter.getString("Id"),
                                    response.getJSONObject(i).getJSONObject("shippingDetail")

                            );
                            searchResultCards.add(searchResultCard);
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        return;
                    }

                    topOfResultsLayout.setVisibility(View.VISIBLE);
                    searchResultRecyclerView.setVisibility(View.VISIBLE);
                    searchResultCardAdapter.notifyDataSetChanged();
                    searchResultRecyclerView.invalidate();
                    progressBar.setVisibility(View.GONE);
                    progressTitle.setVisibility(View.GONE);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    findViewById(R.id.noresults).setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    progressTitle.setVisibility(View.GONE);
                    Log.i("RESPONSE", error.getMessage());
                }
            });

            queue.add(getRequest);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
