package com.bright.productsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimilarTabFragment extends Fragment {
    private RecyclerView similarItemsRecycler;
    private SimilarItemCardAdapter similarItemCardAdapter;
    private List<SimilarItemCard> similarItemCards;
    private Spinner sortItemSpinner;
    private Spinner sortOrderSpinner;
    private JSONArray jsonResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.similar_fragment, container, false);
        similarItemsRecycler = view.findViewById(R.id.similarItemRecycler);
        similarItemCards = new ArrayList<>();

        sortItemSpinner = view.findViewById(R.id.sortTypeSpinner);
        sortOrderSpinner = view.findViewById(R.id.sortOrderSpinner);

        view.findViewById(R.id.noresultsimilarpage).setVisibility(View.GONE);

        jsonResponse = new JSONArray();


        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://csci571-hw8.azurewebsites.net/api/similarItems?id=" + getArguments().getString("id"),
//                "http://csci571-hw8.azurewebsites.net/api/similarItems?id=183454743771",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            jsonResponse = response;
                            if (response.length() == 0) {
                                view.findViewById(R.id.noresultsimilarpage).setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < response.length(); i++) {
                                similarItemCards.add(new SimilarItemCard(
                                        response.getJSONObject(i).getString("ImageURL"),
                                        response.getJSONObject(i).getString("ProductName"),
                                        response.getJSONObject(i).getString("ShippingCost"),
                                        response.getJSONObject(i).getString("DaysLeft"),
                                        response.getJSONObject(i).getString("Price"),
                                        response.getJSONObject(i).getString("URL")

                                ));
                            }
                            similarItemCardAdapter = new SimilarItemCardAdapter(similarItemCards, getContext());
                            similarItemsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            similarItemsRecycler.setAdapter(similarItemCardAdapter);

                            similarItemCardAdapter.setOnItemClickListener(new SimilarItemCardAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String URL = similarItemCards.get(position).getURL();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                    startActivity(browserIntent);
                                }
                            });

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
                }
        );

        queue.add(jsonArrayRequest);

        similarItemCardAdapter = new SimilarItemCardAdapter(similarItemCards, getContext());
        similarItemsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        similarItemsRecycler.setAdapter(similarItemCardAdapter);

        sortItemSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        "Default",
                        "Name",
                        "Price",
                        "Days"
                }));

        sortOrderSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        "Ascending",
                        "Descending"
                }));

        final Comparator<SimilarItemCard> sortAscending = new Comparator<SimilarItemCard>() {
            @Override
            public int compare(SimilarItemCard o1, SimilarItemCard o2) {
                switch (sortItemSpinner.getSelectedItemPosition()) {
                    case 1:
                        return o1.getProductTitle().compareTo(o2.getProductTitle());
                    case 2:
                        return o1.getPriceNumeric() > o2.getPriceNumeric() ? 1 : -1;
                    case 3:
                        return o1.getDaysNumeric() > o2.getDaysNumeric() ? 1 : -1;
                    default:
                        return 0;
                }
            }
        };

        final Comparator<SimilarItemCard> sortDescending = new Comparator<SimilarItemCard>() {
            @Override
            public int compare(SimilarItemCard o1, SimilarItemCard o2) {
                switch (sortItemSpinner.getSelectedItemPosition()) {
                    case 1:
                        return o2.getProductTitle().compareTo(o1.getProductTitle());
                    case 2:
                        return o1.getPriceNumeric() < o2.getPriceNumeric() ? 1 : -1;
                    case 3:
                        return o1.getDaysNumeric() < o2.getDaysNumeric() ? 1 : -1;
                    default:
                        return 0;
                }
            }
        };

        sortItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    try {
                        sortOrderSpinner.setEnabled(false);
                        similarItemCards.clear();
                        for (int i = 0; i < jsonResponse.length(); i++) {
                            similarItemCards.add(new SimilarItemCard(
                                    jsonResponse.getJSONObject(i).getString("ImageURL"),
                                    jsonResponse.getJSONObject(i).getString("ProductName"),
                                    jsonResponse.getJSONObject(i).getString("ShippingCost"),
                                    jsonResponse.getJSONObject(i).getString("DaysLeft"),
                                    jsonResponse.getJSONObject(i).getString("Price"),
                                    jsonResponse.getJSONObject(i).getString("URL")

                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    sortOrderSpinner.setEnabled(true);
                    if (sortOrderSpinner.getSelectedItemPosition() == 0) {
                        Collections.sort(similarItemCards, sortAscending);
                    } else {
                        Collections.sort(similarItemCards, sortDescending);
                    }
                }
                similarItemCardAdapter.notifyDataSetChanged();
            }
        });

        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Collections.sort(similarItemCards, sortAscending);
                } else {
                    Collections.sort(similarItemCards, sortDescending);
                }

                similarItemCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        return view;
    }

}
