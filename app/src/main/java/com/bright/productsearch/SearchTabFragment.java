package com.bright.productsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchTabFragment extends Fragment {
    private static final String TAG = "Search Tab";

    private EditText keywordText;
    private TextView keywordError;
    private Spinner categoryDropdown;
    private CheckBox newCheckBox;
    private CheckBox usedCheckBox;
    private CheckBox unspecifiedCheckBox;
    private CheckBox localPickupCheckBox;
    private CheckBox freeShippingCheckBox;
    private EditText milesFromText;
    private CheckBox nearbySearchCheckBox;
    private LinearLayout nearbySearchLayout;
    private RadioButton currentLocationRadioButton;
    private RadioButton zipcodeRadioButton;

    private TextView zipcodeError;
    private AutoCompleteTextView zipcodeAutoText;

    private Button searchButton;
    private Button clearButton;
    private String zipcode;


    private Map<String, String> categories = new HashMap<String, String>() {{
        put("All", "0");
        put("Art", "550");
        put("Baby", "2984");
        put("Books", "267");
        put("Clothing, Shoes & Accessories", "11450");
        put("Computers/Tablets & Networking", "58058");
        put("Healthy & Beauty", "26395");
        put("Music", "11233");
        put("Video Games & Consoles", "124");
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);

        categoryDropdown = view.findViewById(R.id.categorySpinner);
        String[] categoryDropdownItems = new String[]{
                "All",
                "Art",
                "Baby",
                "Books",
                "Clothing, Shoes & Accessories",
                "Computers/Tablets & Networking",
                "Healthy & Beauty",
                "Music",
                "Video Games & Consoles"
        };
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryDropdownItems);

        categoryDropdown.setAdapter(categorySpinnerAdapter);

        nearbySearchLayout = view.findViewById(R.id.nearbySearchLayout);
        nearbySearchCheckBox = view.findViewById(R.id.nearbySearchCheckBox);
        nearbySearchCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    nearbySearchLayout.setVisibility(View.VISIBLE);
                else
                    nearbySearchLayout.setVisibility(View.INVISIBLE);
            }
        });

        currentLocationRadioButton = view.findViewById(R.id.currentLocationRadioButton);
        zipcodeRadioButton = view.findViewById(R.id.zipcodeRadioButton);
        zipcodeAutoText = view.findViewById(R.id.zipcodeAutoTextView);
        zipcodeError = view.findViewById(R.id.zipcodeError);
        zipcodeAutoText.setEnabled(false);
        currentLocationRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zipcodeAutoText.setEnabled(false);
                    zipcodeRadioButton.setChecked(false);
                }
            }
        });

        zipcodeRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zipcodeAutoText.setEnabled(true);
                    currentLocationRadioButton.setChecked(false);
                }
            }
        });

        searchButton = view.findViewById(R.id.searchButton);
        keywordText = view.findViewById(R.id.keywordEditText);
        keywordError = view.findViewById(R.id.keywordError);
        newCheckBox = view.findViewById(R.id.conditionNewCheckBox);
        usedCheckBox = view.findViewById(R.id.conditionUsedCheckBox);
        unspecifiedCheckBox = view.findViewById(R.id.conditionUnspecifiedCheckBox);
        localPickupCheckBox = view.findViewById(R.id.shippingLPCheckBox);
        freeShippingCheckBox = view.findViewById(R.id.shippingFSCheckBox);
        milesFromText = view.findViewById(R.id.milesFromEditText);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String[] ip_zipcode = new String[1];
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "http://ip-api.com/json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ip_zipcode[0] = response.getString("zip");
                    Log.d("IP", ip_zipcode[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(getRequest);

        zipcodeAutoText.setAdapter(new AutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation
                if (keywordText.getText().toString().length() == 0) {
                    keywordError.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Please fix all fields with error", Toast.LENGTH_SHORT).show();
                    return;
                } else
                    keywordError.setVisibility(View.GONE);

                if (nearbySearchCheckBox.isChecked() && zipcodeRadioButton.isChecked()) {
                    if (zipcodeAutoText.getText().toString().length() == 0) {
                        zipcodeError.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Please fix all fields with error", Toast.LENGTH_SHORT).show();
                        return;
                    } else
                        zipcodeError.setVisibility(View.GONE);
                }


                if (currentLocationRadioButton.isChecked())
                    zipcode = ip_zipcode[0];
                else
                    zipcode = new String(zipcodeAutoText.getText().toString());

//                if (milesFromText.getText().toString().length() == 0)
//                    milesFromText.setText("10");


                StringBuilder url = new StringBuilder("https://csci571-hw8.azurewebsites.net/api/search?");
                url.append("keywords=" + keywordText.getText().toString());
                url.append("&zipcode=" + zipcode);

                if (!categories.get(categoryDropdown.getSelectedItem().toString()).equals("0")) {
                    url.append("&category=" + categories.get(categoryDropdown.getSelectedItem().toString()));
                }
                if (!nearbySearchCheckBox.isChecked()) {
                    url.append("&maxDistance=" + "10000");
                } else
                    url.append("&maxDistance=" + (milesFromText.getText().toString().length() == 0 ? "10" : milesFromText.getText().toString()));

                if (freeShippingCheckBox.isChecked()) url.append("&freeShippingOnly=true");
                if (localPickupCheckBox.isChecked()) url.append("&localPickupOnly=true");
                if (newCheckBox.isChecked()) url.append("&new=true");
                if (usedCheckBox.isChecked()) url.append("&used=true");
                if (unspecifiedCheckBox.isChecked()) url.append("&unspecified=true");
                Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                intent.putExtra("URL", url.toString());
                intent.putExtra("keyword", keywordText.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
}
