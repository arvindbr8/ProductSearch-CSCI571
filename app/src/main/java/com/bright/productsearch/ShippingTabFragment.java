package com.bright.productsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONException;
import org.json.JSONObject;

public class ShippingTabFragment extends Fragment {
    private TextView storeNameTextView;
    private TextView feedbackScoreTextView;
    private CircularScoreView popularityScoreView;
    private ImageView feedbackStarView;
    private JSONObject sellerInfoResponse;

    private TextView shippingCostTextView;
    private TextView globalShippingTextView;
    private TextView handlingTimeTextView;
    private TextView conditionTextView;

    private TextView policyTextView;
    private TextView returnsWithinView;
    private TextView refundsModeView;
    private TextView shippedByView;

    private JSONObject shippingDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.shipping_fragment, container, false);

        storeNameTextView = view.findViewById(R.id.storeNameTextView);
        feedbackScoreTextView = view.findViewById(R.id.feedbackScoreTextView);
        popularityScoreView = view.findViewById(R.id.popularityCircularScore);
        feedbackStarView = view.findViewById(R.id.feedbackStartImageView);

        shippingCostTextView = view.findViewById(R.id.shippingCostTextView);
        globalShippingTextView = view.findViewById(R.id.globalShippingTextView);
        handlingTimeTextView = view.findViewById(R.id.handlingTimeTextView);
        conditionTextView = view.findViewById(R.id.conditionTextView);

        policyTextView = view.findViewById(R.id.policyTextView);
        returnsWithinView = view.findViewById(R.id.returnsWithinTextView);
        refundsModeView = view.findViewById(R.id.refundModeTextView);
        shippedByView = view.findViewById(R.id.shippedByTextView);


        String id = getArguments().getString("id");
        try {
            shippingDetail = new JSONObject(getArguments().getString("shippingDetail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());

        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                "https://csci571-hw8.azurewebsites.net/api/product?id=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            sellerInfoResponse = response.getJSONObject("sellerInfo");

                            //soldBy
                            view.findViewById(R.id.soldByLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.storeName).setVisibility(View.GONE);
                            view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.feedbackScoreLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.popularityLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.feedbackStarLayout).setVisibility(View.GONE);

                            if (!sellerInfoResponse.getString("StoreName").equals("N/A")) {
                                view.findViewById(R.id.soldByLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.storeName).setVisibility(View.VISIBLE);
                                String URL = sellerInfoResponse.getString("URL");
                                Spanned html = Html.fromHtml("<a href=\"" + URL + "\">" + sellerInfoResponse.getString("StoreName") + "</a>");
                                storeNameTextView.setMovementMethod(LinkMovementMethod.getInstance());
//                                storeNameTextView.setText("<a href=\""+URL+"\">"+sellerInfoResponse.getString("StoreName")+"</a>");
                                storeNameTextView.setText(html);
                            }
                            if (sellerInfoResponse.has("FeedbackScore")) {
                                view.findViewById(R.id.soldByLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.feedbackScoreLayout).setVisibility(View.VISIBLE);
                                feedbackScoreTextView.setText(sellerInfoResponse.getString("FeedbackScore"));
                            }
                            if (sellerInfoResponse.has("Popularity")) {
                                view.findViewById(R.id.soldByLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.popularityLayout).setVisibility(View.VISIBLE);
                                popularityScoreView.setScore((int) Float.parseFloat(sellerInfoResponse.getString("Popularity")));
                            }

                            if (sellerInfoResponse.has("FeedbackRatingStar")) {
                                view.findViewById(R.id.soldByLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.soldByLayoutTable).findViewById(R.id.feedbackStarLayout).setVisibility(View.VISIBLE);
                                switch (sellerInfoResponse.getString("FeedbackRatingStar")) {
                                    case "YellowShooting":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Yellow));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    case "TurquoiseShooting":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Turquoise));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    case "PurpleShooting":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Purple));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    case "RedShooting":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Red));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    case "GreenShooting":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Green));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    case "SilverShooting":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Silver));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    case "Yellow":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Yellow));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle));
                                        break;
                                    case "Turquoise":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Turquoise));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle));
                                        break;
                                    case "Purple":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Purple));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle));
                                        break;
                                    case "Red":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Red));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle));
                                        break;
                                    case "Green":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Green));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle));
                                        break;
                                    case "Silver":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.Silver));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle));
                                        break;
                                    case "None":
                                        feedbackStarView.setColorFilter(getResources().getColor(R.color.White));
                                        feedbackStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_circle_outline));
                                        break;
                                    default:
                                        break;
                                }
                            }

                            //shippingInfo
                            view.findViewById(R.id.shippingInfoLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.shippingCostLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.globalShippingLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.handlingTimeLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.conditionLayout).setVisibility(View.GONE);

                            if (sellerInfoResponse.has("ShippingCost")) {
                                view.findViewById(R.id.shippingInfoLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.shippingCostLayout).setVisibility(View.VISIBLE);
                                shippingCostTextView.setText(
                                        shippingDetail.getString("ShippingCost").equals("$0.0") ?
                                                "Free shipping" :
                                                shippingDetail.getString("ShippingCost")
                                );
                            }
                            if (sellerInfoResponse.has("GlobalShipping")) {
                                view.findViewById(R.id.shippingInfoLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.globalShippingLayout).setVisibility(View.VISIBLE);
                                globalShippingTextView.setText(sellerInfoResponse.getString("GlobalShipping"));
                            }
                            if (sellerInfoResponse.has("HandlingTime")) {
                                view.findViewById(R.id.shippingInfoLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.handlingTimeLayout).setVisibility(View.VISIBLE);
                                handlingTimeTextView.setText(shippingDetail.getString("HandlingTime"));
                            }
                            if (sellerInfoResponse.has("Condition")) {
                                view.findViewById(R.id.shippingInfoLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.shippingInfoLayoutTable).findViewById(R.id.conditionLayout).setVisibility(View.VISIBLE);
                                conditionTextView.setText(sellerInfoResponse.getString("Condition"));
                            }

                            //returnPolicy
                            view.findViewById(R.id.refundLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.policyLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.refundswithinLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.refundModeLayout).setVisibility(View.GONE);
                            view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.shippedByLayout).setVisibility(View.GONE);

                            if (sellerInfoResponse.has("Policy")) {
                                view.findViewById(R.id.refundLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.policyLayout).setVisibility(View.VISIBLE);
                                policyTextView.setText(sellerInfoResponse.getString("Policy"));
                            }

                            if (sellerInfoResponse.has("ReturnsWithin")) {
                                view.findViewById(R.id.refundLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.refundswithinLayout).setVisibility(View.VISIBLE);
                                returnsWithinView.setText(sellerInfoResponse.getString("ReturnsWithin"));
                            }

                            if (sellerInfoResponse.has("RefundMode")) {
                                view.findViewById(R.id.refundLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.refundModeLayout).setVisibility(View.VISIBLE);
                                returnsWithinView.setText(sellerInfoResponse.getString("RefundMode"));
                            }

                            if (sellerInfoResponse.has("ShippedBy")) {
                                view.findViewById(R.id.refundLayout).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.returnPolicyLayoutTable).findViewById(R.id.shippedByLayout).setVisibility(View.VISIBLE);
                                shippedByView.setText(sellerInfoResponse.getString("ShippedBy"));
                            }


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
                });
        queue.add(getRequest);


        return view;
    }
}
