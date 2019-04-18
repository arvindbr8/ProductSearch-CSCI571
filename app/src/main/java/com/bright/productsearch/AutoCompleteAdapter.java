package com.bright.productsearch;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> mdata;
    private Context context;

    public AutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        mdata = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public String getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.GET,
                            "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=" + constraint.toString() + "&username=arvindbright&country=US&maxRows=5",
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray postCodes = response.getJSONArray("postalCodes");
                                        mdata = new ArrayList<>();
                                        for (int i = 0; i < postCodes.length(); i++) {
                                            mdata.add(postCodes.getJSONObject(i).getString("postalCode"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );
                    queue.add(request);
                    filterResults.values = mdata;
                    filterResults.count = mdata.size();

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }

        };

        return myFilter;

    }
}
