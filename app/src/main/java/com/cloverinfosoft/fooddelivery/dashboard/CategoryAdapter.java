package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.model.CategoryModel;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by admin on 2/16/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context mContext;
    private HashMap<Integer,CategoryModel> categoryModelHashMap;
    ArrayList<String> arrayList=new ArrayList<>();
    public static int selected_position=0;
    HomeFragment fragment;
    PreferencesManager preferencesManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public MyViewHolder(View view) {
            super(view);
            tv_name=(TextView) view.findViewById(R.id.tv_name);
        }
    }


    public CategoryAdapter(Context mContext, HomeFragment fragment, HashMap<Integer,CategoryModel> categoryModelHashMap) {
        this.mContext = mContext;
        this.categoryModelHashMap = categoryModelHashMap;
        this.fragment=fragment;
        preferencesManager=PreferencesManager.getInstance(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_category_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_name.setText(categoryModelHashMap.get(position).getName());
        if(selected_position==position)
        {
            holder.tv_name.setBackground(mContext.getResources().getDrawable(R.mipmap.btn_filled));
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        else
        {
            holder.tv_name.setBackground(mContext.getResources().getDrawable(R.mipmap.btn_not_selected));
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selected_position=position;
                holder.tv_name.setBackground(mContext.getResources().getDrawable(R.mipmap.btn_filled));
                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                notifyDataSetChanged();

                if(position==0)
                {
                    fragment.callProductList("",0, false);
                    preferencesManager.setselected_cat("");
                }
                else
                {
                    preferencesManager.setselected_cat(categoryModelHashMap.get(position).getName());
                    fragment.callProductList(categoryModelHashMap.get(position).getName(),0, false);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryModelHashMap.size();
    }
}
