package com.bright.productsearch;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultCardAdapter extends RecyclerView.Adapter<SearchResultCardAdapter.ViewHolder> {

    private List<SearchResultCard> searchResultCards;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SearchResultCardAdapter(List<SearchResultCard> searchResultCards, Context context) {
        this.searchResultCards = searchResultCards;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_result_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SearchResultCard card = searchResultCards.get(i);
        Log.d("IMAGEURL",card.getProductImageURL());
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

        viewHolder.productTitleView.setText(card.getProductTitle());
        viewHolder.zipcodeTextView.setText(card.getZipcode());
        viewHolder.shippingTextView.setText(card.getShipping());

        viewHolder.cartButton.setImageResource(R.drawable.cart_plus);
        viewHolder.conditionTextView.setText(card.getCondition());
        viewHolder.priceTextView.setText(card.getPrice());
    }

    @Override
    public int getItemCount() {
        return searchResultCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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


        }
    }
}
