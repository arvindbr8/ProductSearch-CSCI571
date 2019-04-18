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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SearchTabFragment extends Fragment {
    private static final String TAG = "Search Tab";

    private EditText keywordText;
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

    private EditText zipcodeEditText;
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
                if(isChecked)
                    nearbySearchLayout.setVisibility(View.VISIBLE);
                else
                    nearbySearchLayout.setVisibility(View.INVISIBLE);
            }
        });

        currentLocationRadioButton = view.findViewById(R.id.currentLocationRadioButton);
        zipcodeRadioButton = view.findViewById(R.id.zipcodeRadioButton);
        zipcodeEditText = view.findViewById(R.id.zipcodeEditText);

        currentLocationRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    zipcodeEditText.setEnabled(false);
                    zipcodeRadioButton.setChecked(false);
                }
            }
        });

        zipcodeRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    zipcodeEditText.setEnabled(true);
                    currentLocationRadioButton.setChecked(false);
                }
            }
        });

        searchButton = view.findViewById(R.id.searchButton);
        keywordText = view.findViewById(R.id.keywordEditText);
        newCheckBox = view.findViewById(R.id.conditionNewCheckBox);
        usedCheckBox = view.findViewById(R.id.conditionUsedCheckBox);
        unspecifiedCheckBox = view.findViewById(R.id.conditionUnspecifiedCheckBox);
        localPickupCheckBox = view.findViewById(R.id.shippingLPCheckBox);
        freeShippingCheckBox = view.findViewById(R.id.shippingFSCheckBox);
        milesFromText = view.findViewById(R.id.milesFromEditText);

        if(currentLocationRadioButton.isChecked()){
            zipcode = "90007";
        } else zipcode = new String(zipcodeEditText.getText().toString());
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder url = new StringBuilder("https://csci571-hw8.azurewebsites.net/api/search?");
                url.append("keywords="+keywordText.getText().toString());
                url.append("&zipcode="+zipcode);

                if(!categories.get(categoryDropdown.getSelectedItem().toString()).equals("0")){
                    url.append("&category="+categories.get(categoryDropdown.getSelectedItem().toString()));
                }
                if(!nearbySearchCheckBox.isChecked()){
                    url.append("&maxDistance="+"10000");
                } else url.append("&maxDistance="+milesFromText.getText().toString());

                if(freeShippingCheckBox.isChecked()) url.append("&freeShippingOnly=true");
                if(localPickupCheckBox.isChecked()) url.append("&localPickupOnly=true");
                if(newCheckBox.isChecked()) url.append("&new=true");
                if(usedCheckBox.isChecked()) url.append("&used=true");
                if(unspecifiedCheckBox.isChecked()) url.append("&unspecified=true");
                Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                intent.putExtra("URL", url.toString());
                startActivity(intent);
            }
        });

        return view;
    }
}
