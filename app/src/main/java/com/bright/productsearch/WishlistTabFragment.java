package com.bright.productsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WishlistTabFragment extends Fragment {
    private static final String TAG = "WishlistTab";

    private RecyclerView wishListRecycler;
    private WishlistCardAdapter wishCardAdapter;

    private List<SearchResultCard> wishList;

    private SharedPreferences sharedPreferences;
    private JSONObject wishListItem;

    private TextView wishItemTotalView;
    private TextView wishListFooterView;
    private float totalPrice;

    private String pound = "$";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wishlist_fragment, container, false);

        wishListRecycler = view.findViewById(R.id.wishListRecycler);
        wishListRecycler.setHasFixedSize(true);
        wishListRecycler.setLayoutManager(new GridLayoutManager(container.getContext(), 2));

        wishItemTotalView = view.findViewById(R.id.wishlistTotal);
        wishListFooterView = view.findViewById(R.id.wishlistFooter);


        totalPrice = 0;
        wishList = new ArrayList<>();

        wishCardAdapter = new WishlistCardAdapter(wishList, container.getContext());
        wishListRecycler.setAdapter(wishCardAdapter);

        sharedPreferences = container.getContext().getSharedPreferences("WISH_LIST", Context.MODE_PRIVATE);
//
//        private String productImageURL;
//        private String productTitle;
//        private String productFullTitle;
//        private String zipcode;
//        private String shipping;
//        private String cart;
//        private String condition;
//        private String price;
//        private String Id;
//        private JSONObject ShippingDetail;

        wishCardAdapter.setOnItemClickListener(new WishlistCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = wishList.get(position).getId();
                JSONObject shippingdetails = wishList.get(position).getShippingDetail();
                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("shippingDetails", shippingdetails.toString());
                intent.putExtra("title", wishList.get(position).getProductFullTitle());
                wishListItem = new JSONObject();
                try {
                    wishListItem.put("title", wishList.get(position).getProductTitle());
                    wishListItem.put("imageURL", wishList.get(position).getProductImageURL());
                    wishListItem.put("zip", wishList.get(position).getZipcode());
                    wishListItem.put("shipping", wishList.get(position).getShipping());
                    wishListItem.put("condition", wishList.get(position).getCondition());
                    wishListItem.put("price", wishList.get(position).getPrice());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("wishList", wishListItem.toString());

                startActivity(intent);
            }
        });

        for (String id : sharedPreferences.getAll().keySet()) {
            try {
                JSONObject wishlistItem = new JSONObject(sharedPreferences.getString(id, null));
                wishList.add(new SearchResultCard(
                        wishlistItem.getString("imageURL"),
                        wishlistItem.getString("title"),
                        wishlistItem.getString("fullTitle"),
                        wishlistItem.getString("zip"),
                        wishlistItem.getString("shipping"),
                        wishlistItem.getString("title"),
                        wishlistItem.getString("condition"),
                        wishlistItem.getString("price"),
                        id,
                        new JSONObject(wishlistItem.getString("shippingDetail"))
                ));
                totalPrice += Float.parseFloat(wishlistItem.getString("price").replace("$", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String footerText = "Wishlist total(" + String.valueOf(sharedPreferences.getAll().size()) + "items):";
        wishListFooterView.setText(footerText);
        wishItemTotalView.setText(pound.concat(String.valueOf(totalPrice)));


        wishCardAdapter.notifyDataSetChanged();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        wishList.clear();
        totalPrice = 0;
        for (String id : sharedPreferences.getAll().keySet()) {
            try {
                JSONObject wishlistItem = new JSONObject(sharedPreferences.getString(id, null));
                wishList.add(new SearchResultCard(
                        wishlistItem.getString("imageURL"),
                        wishlistItem.getString("title"),
                        wishlistItem.getString("fullTitle"),
                        wishlistItem.getString("zip"),
                        wishlistItem.getString("shipping"),
                        wishlistItem.getString("title"),
                        wishlistItem.getString("condition"),
                        wishlistItem.getString("price"),
                        id,
                        new JSONObject(wishlistItem.getString("shippingDetail"))
                ));
                totalPrice += Float.parseFloat(wishlistItem.getString("price").replace("$", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String footerText = "Wishlist total(" + String.valueOf(sharedPreferences.getAll().size()) + "items):";
        wishListFooterView.setText(footerText);
        wishItemTotalView.setText(pound.concat(String.valueOf(totalPrice)));
        wishCardAdapter.notifyDataSetChanged();
    }

}
