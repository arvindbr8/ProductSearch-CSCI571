package com.bright.productsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class WishlistCardAdapter extends RecyclerView.Adapter<WishlistCardAdapter.ViewHolder> {

    private List<SearchResultCard> searchResultCards;
    private Context context;
    private OnItemClickListener mListener;
    private SharedPreferences prefs;

    private JSONObject wishListItem;


    public interface OnItemClickListener {
        void onItemClick(int position);

        void updateFooter();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public WishlistCardAdapter(List<SearchResultCard> searchResultCards, Context context) {
        this.searchResultCards = searchResultCards;
        this.context = context;
        prefs = context.getSharedPreferences("WISH_LIST", Context.MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_result_card, null);
        return new WishlistCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SearchResultCard card = searchResultCards.get(i);
        Log.d("IMAGEURL", card.getProductImageURL());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build()
                .load(card.getProductImageURL())
                .error(R.drawable.ic_launcher_background)
                .fit()
                .noFade()
                .into(viewHolder.productImageView);


        if (prefs.getString(card.getId(), null) != null) {
            viewHolder.cartButton.setImageResource(R.drawable.cart_remove);
        } else {
            viewHolder.cartButton.setImageResource(R.drawable.cart_plus);
        }

        viewHolder.productTitleView.setText(card.getProductTitle());
        viewHolder.zipcodeTextView.setText(card.getZipcode());
        viewHolder.shippingTextView.setText(card.getShipping());

        viewHolder.conditionTextView.setText(card.getCondition());
        viewHolder.priceTextView.setText(card.getPrice());

        wishListItem = new JSONObject();
        try {
            wishListItem.put("title", card.getProductTitle());
            wishListItem.put("imageURL", card.getProductImageURL());
            wishListItem.put("zip", card.getZipcode());
            wishListItem.put("shipping", card.getShipping());
            wishListItem.put("condition", card.getCondition());
            wishListItem.put("price", card.getPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return searchResultCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImageView;
        public TextView productTitleView;
        public TextView zipcodeTextView;
        public TextView shippingTextView;
        public ImageButton cartButton;
        public TextView conditionTextView;
        public TextView priceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productTitleView = itemView.findViewById(R.id.productTitleView);
            zipcodeTextView = itemView.findViewById(R.id.zipcodeTextView);
            shippingTextView = itemView.findViewById(R.id.shippingView);
            cartButton = itemView.findViewById(R.id.cartButton);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }

                    }
                }
            });

            cartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SearchResultCard card = searchResultCards.get(position);
                    SharedPreferences.Editor editor = prefs.edit();
                    if (prefs.getString(card.getId(), null) != null) {
                        editor.remove(card.getId());
                        editor.apply();
                        searchResultCards.clear();
                        for (String id : prefs.getAll().keySet()) {
                            try {
                                JSONObject wishlistItem = new JSONObject(prefs.getString(id, null));
                                searchResultCards.add(new SearchResultCard(
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        cartButton.setImageResource(R.drawable.cart_plus);
                        Toast.makeText(context, card.getProductTitle() + " was removed from the wish list", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                    mListener.updateFooter();


                }
            });
        }
    }

}
