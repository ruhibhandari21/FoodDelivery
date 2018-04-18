package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.model.ProductListModel;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * Created by admin on 2/16/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context mContext;
    private HashMap<Integer, ProductListModel> hashMapProductList;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    DisplayImageOptions imageOptions2;
    HomeFragment homeFragment;
    PreferencesManager preferencesManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image,img_veg_non_veg;
        TextView tv_name,tv_price,tv_rating;
        Button btn_add_to_cart;

        public MyViewHolder(View view) {
            super(view);
            food_image = (ImageView) view.findViewById(R.id.food_image);
            img_veg_non_veg = (ImageView) view.findViewById(R.id.img_veg_non_veg);
            tv_name=(TextView)view.findViewById(R.id.tv_name);
            tv_price=(TextView)view.findViewById(R.id.tv_price);
            tv_rating=(TextView)view.findViewById(R.id.tv_rating);
            btn_add_to_cart=(Button)view.findViewById(R.id.btn_add_to_cart);
        }
    }


    public HomeAdapter(Context mContext,HomeFragment homeFragment, HashMap<Integer, ProductListModel> hashMapProductList) {
        this.mContext = mContext;
        this.hashMapProductList = hashMapProductList;
        this.homeFragment=homeFragment;
        preferencesManager=PreferencesManager.getInstance(mContext);
        imageOptions2 = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.mipmap.ic_cart)
                .showImageOnFail(R.mipmap.ic_cart)
                .showImageOnLoading(R.mipmap.ic_cart)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.mipmap.ic_cart)
                .showImageOnFail(R.mipmap.ic_cart)
                .showImageOnLoading(R.mipmap.ic_cart)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext).build();
        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.layout_dashboard_tile, parent, false);
        return new HomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

        if(hashMapProductList.get(position).getProduct_type().equalsIgnoreCase("veg"))
        {
            holder.img_veg_non_veg.setImageResource(R.mipmap.icn_veg);
        }
        else
        {
            holder.img_veg_non_veg.setImageResource(R.mipmap.icn_non_veg);
        }

        holder.tv_name.setText(hashMapProductList.get(position).getName());
        holder.tv_price.setText("₹"+String.format("%.2f", Double.valueOf(hashMapProductList.get(position).getSellingPrice())));
        holder.tv_rating.setText(Double.valueOf(hashMapProductList.get(position).getRating())+"");

        Picasso.with(mContext).load(hashMapProductList.get(position).getImage())
                .transform(new RoundedCornersTransformation(5, 1)).into(holder.food_image);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                homeFragment.callAddToCartWS(hashMapProductList.get(position).getSlug(),position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return hashMapProductList.size();//classList.size();

    }
}