package com.bright.productsearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleTabFragment extends Fragment {

    private LinearLayout pictureGallery;
    private JSONArray responseArray;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.google_fragment, container, false);

        pictureGallery = view.findViewById(R.id.productImagesVerticalView);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest getRequest = null;
        try {
            getRequest = new JsonArrayRequest(Request.Method.GET,
                    "https://csci571-hw8.azurewebsites.net/api/google?title=" + URLEncoder.encode(getArguments().getString("title"), "utf-8"),
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    pictureGallery.addView(insertPhoto(response.get(i).toString()));
                                }
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(getRequest);

        return view;
    }

    public View insertPhoto(String p1) {
        CardView card = new CardView(getContext());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        ImageView imageView1 = new ImageView(getContext());
        imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);


        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });

        builder.build()
                .load(p1)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView1);

        layout.addView(imageView1);

        card.addView(layout);
        card.setLayoutParams(new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT));

        return card;
    }
}
