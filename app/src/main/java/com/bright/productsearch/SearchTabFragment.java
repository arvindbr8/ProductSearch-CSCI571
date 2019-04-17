package com.bright.productsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SearchTabFragment extends Fragment {
    private static final String TAG = "Search Tab";

    private Spinner categoryDropdown;
    private CheckBox nearbySearchCheckBox;
    private LinearLayout nearbySearchLayout;
    private RadioButton currentLocationRadioButton;
    private RadioButton zipcodeRadioButton;
    private EditText zipcodeEditText;

    private Map<String, String> categories = new HashMap<String, String>() {{
        put("All Categories", "0");
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

        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryDropdownItems);

        categoryDropdown.setAdapter(categorySpinnerAdapter);
        return view;
    }
}
