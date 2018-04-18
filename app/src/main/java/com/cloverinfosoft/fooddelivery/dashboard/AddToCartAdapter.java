package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.model.CartItemsModel;

import java.util.HashMap;

/**
 * Created by Admin on 15-04-2018.
 */

public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.MyViewHolder> {

    private Context mContext;
    private HashMap<Integer,CartItemsModel> hashMap=new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_qty, tv_item_name, tv_item_price;
        private ImageView img_minus,img_plus;

        public MyViewHolder(View view) {
            super(view);
            tv_qty = (TextView) view.findViewById(R.id.tv_qty);
            tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
            tv_item_price = (TextView) view.findViewById(R.id.tv_item_price);
            img_minus=(ImageView)view.findViewById(R.id.img_minus);
            img_plus=(ImageView)view.findViewById(R.id.img_plus);
        }
    }


    public AddToCartAdapter(Context mContext,HashMap<Integer,CartItemsModel> hashMap){//List<Movie> moviesList) {
        this.mContext=mContext;
        this.hashMap=hashMap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CartItemsModel cartItemsModel = hashMap.get(position);
        holder.tv_item_name.setText(cartItemsModel.getName());
        holder.tv_item_price.setText("₹"+String.format("%.2f", Double.valueOf(cartItemsModel.getSellingPrice())));
        holder.tv_qty.setText(cartItemsModel.getQuantity());
        holder.img_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ((AddToCartActivity)mContext).callCartUpdateQuantityWS(cartItemsModel.getCode(),"inc");
            }
        });
        holder.img_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(cartItemsModel.getQuantity().equals("1"))
                {
                    ((AddToCartActivity)mContext).callRemoveItemWS(cartItemsModel.getCode());
                   // Toast.makeText(mContext, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ((AddToCartActivity)mContext).callCartUpdateQuantityWS(cartItemsModel.getCode(),"dec");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return hashMap.size();//moviesList.size();
    }
}