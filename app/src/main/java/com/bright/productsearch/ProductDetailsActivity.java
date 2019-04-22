package com.bright.productsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private String id;
    private String title;
    private JSONObject shippingDetails;
    private ProductTabFragment productTabFragment;
    private ShippingTabFragment shippingTabFragment;
    private GoogleTabFragment googleTabFragment;
    private SimilarTabFragment similarTabFragment;
    public String URL;
    public String imageURL;
    public FloatingActionButton cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.productDetailsContainer);

        tabLayout = findViewById(R.id.productDetailsTabs);
        tabLayout.setupWithViewPager(mViewPager);

        cart = findViewById(R.id.fab);

        try {
            id = getIntent().getStringExtra("id");
            shippingDetails = new JSONObject(getIntent().getStringExtra("shippingDetails"));
            title = getIntent().getStringExtra("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SharedPreferences sharedPreferences = getSharedPreferences("WISH_LIST", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getString(id, null) != null) {
            cart.setImageResource(R.drawable.cart_remove);
        } else {
            cart.setImageResource(R.drawable.cart_plus);
        }

        productTabFragment = new ProductTabFragment();
        shippingTabFragment = new ShippingTabFragment();
        googleTabFragment = new GoogleTabFragment();
        similarTabFragment = new SimilarTabFragment();

        try {
            setupViewPager(mViewPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createTabIcons();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(id, null) != null) {
                    editor.remove(id);
                    cart.setImageResource(R.drawable.cart_plus);
                    Toast.makeText(getApplicationContext(), title + " was removed from the wish list", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString(id, getIntent().getExtras().getString("wishList"));
                    cart.setImageResource(R.drawable.cart_remove);
                    Toast.makeText(getApplicationContext(), title + " was added to the wish list", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.product_details_custom_tab, null);
        tabOne.setText("PRODUCT");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.information_variant, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.product_details_custom_tab, null);
        tabTwo.setText("SHIPPING");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.truck_delivery, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.product_details_custom_tab, null);
        tabThree.setText("PHOTOS");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.google, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.product_details_custom_tab, null);
        tabFour.setText("SIMILAR");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.equal, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }

    private void setupViewPager(ViewPager viewPager) throws JSONException {
//        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("shipping", shippingDetails.getString("ShippingCost"));
        bundle.putString("shippingDetail", shippingDetails.toString());
        bundle.putString("title", title);
        productTabFragment.setArguments(bundle);
        shippingTabFragment.setArguments(bundle);
        googleTabFragment.setArguments(bundle);
        similarTabFragment.setArguments(bundle);

        mSectionsPageAdapter.addFragment(productTabFragment, "PRODUCT");
        mSectionsPageAdapter.addFragment(shippingTabFragment, "SHIPPING");
        mSectionsPageAdapter.addFragment(googleTabFragment, "PHOTOS");
        mSectionsPageAdapter.addFragment(similarTabFragment, "SIMILAR");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fb) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://www.facebook.com/sharer.php?s=100" +
                            "&quote=" + title +
                            "&u=" + URL +
                            "&picture=" + imageURL +
                            "&hashtag=" + "%23CSCI571Spring2019Ebay"
            ));
            startActivity(browserIntent);

        }
        finish();
        return true;
    }

}
