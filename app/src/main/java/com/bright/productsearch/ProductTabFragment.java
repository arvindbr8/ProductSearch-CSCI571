package com.bright.productsearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductTabFragment extends Fragment {

    private LinearLayout pictureGallery;
    private TextView productTitle;
    private TextView priceTextView;
    private TextView shippingTextView;
    private ExpandableHeightListView highlightsListView;
    private ExpandableHeightListView specificationsListView;

    private JSONObject responseObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.product_fragment, container, false);

        view.findViewById(R.id.progressBarInProduct).setVisibility(View.VISIBLE);
        view.findViewById(R.id.progressTitleInProduct).setVisibility(View.VISIBLE);

        pictureGallery = view.findViewById(R.id.productImagesHorizontalView);
        productTitle = view.findViewById(R.id.productTitle);
        priceTextView = view.findViewById(R.id.priceTextView);
        shippingTextView = view.findViewById(R.id.shippingTextView);

        String id = getArguments().getString("id");

        final ArrayList<DataModel> highlights = new ArrayList<>();
        final TwoColumnListViewAdapter headingAdapter = new TwoColumnListViewAdapter(getContext(), highlights);

        highlightsListView = view.findViewById(R.id.hightlightListView);
        highlightsListView.setAdapter(headingAdapter);
        highlightsListView.setExpanded(true);

        final ArrayList<String> specifications = new ArrayList<>();
        specificationsListView = view.findViewById(R.id.specificationListView);
        final ArrayAdapter<String> specsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, specifications);
        specificationsListView.setAdapter(specsAdapter);
        specificationsListView.setExpanded(true);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                "https://csci571-hw8.azurewebsites.net/api/product?id=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseObject = response.getJSONObject("jsonResponse");

                            if (responseObject.has("Brand"))
                                specifications.add("\u2022 " + responseObject.getString("Brand"));

                            Iterator<String> iter = responseObject.keys();

                            while (iter.hasNext()) {
                                String key = iter.next();
                                Log.d("KEY", key);
                                switch (key) {
                                    case "ProductImages":
                                        List<String> urls = new ArrayList<>();

                                        //loading images
                                        JSONArray urlArray = responseObject.getJSONArray("ProductImages");
                                        for (int i = 0; i < urlArray.length(); i++) {
                                            urls.add(urlArray.getString(i));


                                        }
                                        for (int i = 0; i < urls.size(); i++)
                                            pictureGallery.addView(insertPhoto(urls.get(i)));
                                        break;
                                    case "Title":
                                        productTitle.setText(responseObject.getString("Title"));
                                        break;
                                    case "Price":
                                        priceTextView.setText(responseObject.getString("Price").replace(" ", ""));
                                        highlights.add(new DataModel("Price", responseObject.getString("Price").replace(" ", "")));
                                        break;
                                    case "Subtitle":
                                        highlights.add(new DataModel("Subtitle", responseObject.getString("Subtitle")));
                                        break;
                                    case "Id":
                                    case "Brand":
                                    case "URL":
                                        break;
                                    default:
                                        specifications.add("\u2022 " + responseObject.get(key).toString());
                                        break;
                                }
                            }
                            view.findViewById(R.id.progressBarInProduct).setVisibility(View.GONE);
                            view.findViewById(R.id.progressTitleInProduct).setVisibility(View.GONE);

                            view.findViewById(R.id.layout).setVisibility(View.VISIBLE);

                            headingAdapter.notifyDataSetChanged();
                            specsAdapter.notifyDataSetChanged();
                            String price = getArguments().getString("shipping").equals("$0.0") ? "Free" : getArguments().getString("shipping");
                            shippingTextView.setText("With " + price + " Shipping");


                            //rest


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(getRequest);


        return view;
    }


    public View insertPhoto(String path) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build()
                .load(path)
                .into(imageView);
        layout.addView(imageView);
        return layout;
    }


}
