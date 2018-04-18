package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.model.CartItemsModel;
import com.cloverinfosoft.fooddelivery.model.MyOrderModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * Created by Admin on 15-04-2018.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    private Context mContext;
    private HashMap<Integer, MyOrderModel> hashMap;
    int cnt = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_item;
        private TextView tv_orderid, tv_orderdate, tv_item_name, tv_orderstatus, tv_ordertotal, tv_item_price, tv_item_qty;
        private ImageView rate1, rate2, rate3, rate4, rate5;

        public MyViewHolder(View view) {
            super(view);
            img_item = (ImageView) view.findViewById(R.id.img_item);
            tv_orderid = (TextView) view.findViewById(R.id.tv_orderid);
            tv_orderdate = (TextView) view.findViewById(R.id.tv_orderdate);
            tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
            tv_item_price = (TextView) view.findViewById(R.id.tv_item_price);
            tv_item_qty = (TextView) view.findViewById(R.id.tv_item_qty);
            tv_orderstatus = (TextView) view.findViewById(R.id.tv_orderstatus);
            tv_ordertotal = (TextView) view.findViewById(R.id.tv_ordertotal);
            rate1 = (ImageView) view.findViewById(R.id.rate1);
            rate2 = (ImageView) view.findViewById(R.id.rate2);
            rate3 = (ImageView) view.findViewById(R.id.rate3);
            rate4 = (ImageView) view.findViewById(R.id.rate4);
            rate5 = (ImageView) view.findViewById(R.id.rate5);

        }
    }


    public MyOrdersAdapter(Context mContext, HashMap<Integer, MyOrderModel> hashMap) {//List<Movie> moviesList) {
        this.mContext = mContext;
        this.hashMap = hashMap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyOrderModel myOrderModel = hashMap.get(position);
        Picasso.with(mContext).load(myOrderModel.getImage())
                .transform(new RoundedCornersTransformation(10, 0)).into(holder.img_item);
        holder.tv_orderid.setText(myOrderModel.getOrderNo());
        holder.tv_orderdate.setText(myOrderModel.getOrderedDate());
        holder.tv_item_name.setText(myOrderModel.getProductName());
        holder.tv_item_price.setText("₹" + myOrderModel.getSellingPrice());
        holder.tv_item_qty.setText(myOrderModel.getQuantity());
        holder.tv_orderstatus.setText(myOrderModel.getStatus());
        holder.tv_ordertotal.setText("₹" + myOrderModel.getSellingPrice());
        if (myOrderModel.getStatus().equals("Delivered")) {
            holder.tv_orderstatus.setTextColor(Color.parseColor("#01579B"));
        } else if (myOrderModel.getStatus().equals("Ordered")) {
            holder.tv_orderstatus.setTextColor(Color.parseColor("#4CAF50"));
        } else if (myOrderModel.getStatus().equals("Shipped")) {
            holder.tv_orderstatus.setTextColor(Color.parseColor("#FF9800"));
        }

        if(!myOrderModel.getRating().equals(""))
        switch (Integer.parseInt(myOrderModel.getRating()))
        {
            case 0:
//                hashMap.get(position).setFlag1(false);
//                hashMap.get(position).setFlag2(false);
//                hashMap.get(position).setFlag3(false);
//                hashMap.get(position).setFlag4(false);
//                hashMap.get(position).setFlag5(false);
                holder.rate1.setImageResource(R.mipmap.icn_star_gray);
                holder.rate2.setImageResource(R.mipmap.icn_star_gray);
                holder.rate3.setImageResource(R.mipmap.icn_star_gray);
                holder.rate4.setImageResource(R.mipmap.icn_star_gray);
                holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                break;
            case 1:
//                hashMap.get(position).setFlag1(true);
//                hashMap.get(position).setFlag2(false);
//                hashMap.get(position).setFlag3(false);
//                hashMap.get(position).setFlag4(false);
//                hashMap.get(position).setFlag5(false);
                holder.rate1.setImageResource(R.mipmap.icn_rating);
                holder.rate2.setImageResource(R.mipmap.icn_star_gray);
                holder.rate3.setImageResource(R.mipmap.icn_star_gray);
                holder.rate4.setImageResource(R.mipmap.icn_star_gray);
                holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                break;
            case 2:
//                hashMap.get(position).setFlag1(true);
//                hashMap.get(position).setFlag2(true);
//                hashMap.get(position).setFlag3(false);
//                hashMap.get(position).setFlag4(false);
//                hashMap.get(position).setFlag5(false);
                holder.rate1.setImageResource(R.mipmap.icn_rating);
                holder.rate2.setImageResource(R.mipmap.icn_rating);
                holder.rate3.setImageResource(R.mipmap.icn_star_gray);
                holder.rate4.setImageResource(R.mipmap.icn_star_gray);
                holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                break;
            case 3:
//                hashMap.get(position).setFlag1(true);
//                hashMap.get(position).setFlag2(true);
//                hashMap.get(position).setFlag3(true);
//                hashMap.get(position).setFlag4(false);
//                hashMap.get(position).setFlag5(false);
                holder.rate1.setImageResource(R.mipmap.icn_rating);
                holder.rate2.setImageResource(R.mipmap.icn_rating);
                holder.rate3.setImageResource(R.mipmap.icn_rating);
                holder.rate4.setImageResource(R.mipmap.icn_star_gray);
                holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                break;
            case 4:
//                hashMap.get(position).setFlag1(true);
//                hashMap.get(position).setFlag2(true);
//                hashMap.get(position).setFlag3(true);
//                hashMap.get(position).setFlag4(true);
//                hashMap.get(position).setFlag5(false);
                holder.rate1.setImageResource(R.mipmap.icn_rating);
                holder.rate2.setImageResource(R.mipmap.icn_rating);
                holder.rate3.setImageResource(R.mipmap.icn_rating);
                holder.rate4.setImageResource(R.mipmap.icn_rating);
                holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                break;
            case 5:
//                hashMap.get(position).setFlag1(true);
//                hashMap.get(position).setFlag2(true);
//                hashMap.get(position).setFlag3(true);
//                hashMap.get(position).setFlag4(true);
//                hashMap.get(position).setFlag5(true);
                holder.rate1.setImageResource(R.mipmap.icn_rating);
                holder.rate2.setImageResource(R.mipmap.icn_rating);
                holder.rate3.setImageResource(R.mipmap.icn_rating);
                holder.rate4.setImageResource(R.mipmap.icn_rating);
                holder.rate5.setImageResource(R.mipmap.icn_rating);
                break;
        }


        holder.rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hashMap.get(position).isFlag1())//false
                {
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag2(false);
                    hashMap.get(position).setFlag3(false);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate3.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 1;
                } else {
                    hashMap.get(position).setFlag1(false);
                    hashMap.get(position).setFlag2(false);
                    hashMap.get(position).setFlag3(false);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate2.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate3.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 0;
                }
                MyOrderModel myOrderModel1=hashMap.get(position);
                myOrderModel1.setRating(cnt + "");
                hashMap.put(position,myOrderModel1);
                ((MyOrderActiviy)mContext).callRatingWS(hashMap.get(position).getCode(),cnt+"",position);
//                ((MyOrderActiviy)mContext).HashmapValue(hashMap);


            }
        });

        holder.rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!myOrderModel.isFlag2())//false
                {
                    hashMap.get(position).setFlag2(true);
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag3(false);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 2;
                } else {
                    myOrderModel.setFlag1(true);
                    myOrderModel.setFlag2(false);
                    myOrderModel.setFlag3(false);
                    myOrderModel.setFlag4(false);
                    myOrderModel.setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate3.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 1;
                }
                MyOrderModel myOrderModel1=hashMap.get(position);
                myOrderModel1.setRating(cnt + "");
                hashMap.put(position,myOrderModel1);
                ((MyOrderActiviy)mContext).callRatingWS(hashMap.get(position).getCode(),cnt+"",position);
//                ((MyOrderActiviy)mContext).HashmapValue(hashMap);
            }
        });

        holder.rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!myOrderModel.isFlag3())//false
                {
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag2(true);
                    hashMap.get(position).setFlag3(true);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_rating);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 3;
                } else {
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag2(true);
                    hashMap.get(position).setFlag3(false);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 2;
                }
                MyOrderModel myOrderModel1=hashMap.get(position);
                myOrderModel1.setRating(cnt + "");
                hashMap.put(position,myOrderModel1);
                ((MyOrderActiviy)mContext).callRatingWS(hashMap.get(position).getCode(),cnt+"",position);
//                ((MyOrderActiviy)mContext).HashmapValue(hashMap);
            }
        });

        holder.rate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hashMap.get(position).isFlag4())//false
                {
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag2(true);
                    hashMap.get(position).setFlag3(true);
                    hashMap.get(position).setFlag4(true);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_rating);
//                    holder.rate4.setImageResource(R.mipmap.icn_rating);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 4;
                } else {
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag2(true);
                    hashMap.get(position).setFlag3(true);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_rating);
//                    holder.rate4.setImageResource(R.mipmap.icn_star_gray);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 3;
                }
                MyOrderModel myOrderModel1=hashMap.get(position);
                myOrderModel1.setRating(cnt + "");
                hashMap.put(position,myOrderModel1);
                ((MyOrderActiviy)mContext).callRatingWS(hashMap.get(position).getCode(),cnt+"",position);
//                ((MyOrderActiviy)mContext).HashmapValue(hashMap);
            }
        });

        holder.rate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hashMap.get(position).isFlag5())//false
                {
                    hashMap.get(position).setFlag1(true);
                    hashMap.get(position).setFlag2(true);
                    hashMap.get(position).setFlag3(true);
                    hashMap.get(position).setFlag4(true);
                    hashMap.get(position).setFlag5(true);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_rating);
//                    holder.rate4.setImageResource(R.mipmap.icn_rating);
//                    holder.rate5.setImageResource(R.mipmap.icn_rating);
                    cnt = 5;
                } else {
                    hashMap.get(position).setFlag1(false);
                    hashMap.get(position).setFlag2(false);
                    hashMap.get(position).setFlag3(false);
                    hashMap.get(position).setFlag4(false);
                    hashMap.get(position).setFlag5(false);
//                    holder.rate1.setImageResource(R.mipmap.icn_rating);
//                    holder.rate2.setImageResource(R.mipmap.icn_rating);
//                    holder.rate3.setImageResource(R.mipmap.icn_rating);
//                    holder.rate4.setImageResource(R.mipmap.icn_rating);
//                    holder.rate5.setImageResource(R.mipmap.icn_star_gray);
                    cnt = 4;
                }
                MyOrderModel myOrderModel1=hashMap.get(position);
                myOrderModel1.setRating(cnt + "");
                hashMap.put(position,myOrderModel1);
                ((MyOrderActiviy)mContext).callRatingWS(hashMap.get(position).getCode(),cnt+"",position);
//                ((MyOrderActiviy)mContext).HashmapValue(hashMap);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }
}