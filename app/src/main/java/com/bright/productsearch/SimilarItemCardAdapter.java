package com.bright.productsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarItemCardAdapter extends RecyclerView.Adapter<SimilarItemCardAdapter.ViewHolder> {

    private List<SimilarItemCard> similarItemCards;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SimilarItemCardAdapter(List<SimilarItemCard> similarItemCards, Context context) {
        this.similarItemCards = similarItemCards;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.similar_item_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SimilarItemCard card = similarItemCards.get(i);
        Picasso.with(context)
                .load(card.getImageURL())
                .noFade()
                .fit()
                .into(viewHolder.pictureView);
        viewHolder.productTextView.setText(card.getProductTitle());
        viewHolder.shippingTextView.setText(card.getShipping());
        viewHolder.daysLeftTextView.setText(card.getDaysLeft());
        viewHolder.priceTextView.setText(card.getPrice());

    }

    @Override
    public int getItemCount() {
        return similarItemCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pictureView;
        private TextView productTextView;
        private TextView shippingTextView;
        private TextView daysLeftTextView;
        private TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureView = itemView.findViewById(R.id.simItemImageView);
            productTextView = itemView.findViewById(R.id.simItemProductTitle);
            shippingTextView = itemView.findViewById(R.id.simItemsShippingTextView);
            daysLeftTextView = itemView.findViewById(R.id.simItemsDaysLeftTextView);
            priceTextView = itemView.findViewById(R.id.simItemsPriceTextView);

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
