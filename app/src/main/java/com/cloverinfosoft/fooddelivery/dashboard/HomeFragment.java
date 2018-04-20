package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.Services.AppConstants;
import com.cloverinfosoft.fooddelivery.Services.OnTaskCompleted;
import com.cloverinfosoft.fooddelivery.Services.WebService;
import com.cloverinfosoft.fooddelivery.model.CategoryModel;
import com.cloverinfosoft.fooddelivery.model.ProductListModel;
import com.cloverinfosoft.fooddelivery.utils.EndlessRecyclerViewScrollListener;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 4/10/2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnTaskCompleted {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String prev_cat = "", current_cat = "";
    public static String total_products = "";
    HomeAdapter homeAdapter;
    boolean isLoading = false;
    int mPageSize = 10;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    GridLayoutManager mLayoutManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private RelativeLayout rel_add_student, rel_add_teacher, rel_add_class, rel_add_subject;
    private Intent intent;
    private TextView tv_no_items;
    private Context mContext;
    private RecyclerView recycler_view, recycler_view_horizontal;
    private HashMap<Integer, CategoryModel> hashMapCategory = new HashMap<>();
    private HashMap<Integer, ProductListModel> hashMapProductList = new HashMap<>();
    private PreferencesManager preferencesManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    /***
     * Shadaf code for pagination
     *
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && !isLoading) {
                isLoading = true;
                ProductListModel model = new ProductListModel();
                model.setName("load");
                hashMapProductList.put(hashMapProductList.size(), model);
                callProductList(current_cat, hashMapProductList.size()-1,true);
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        preferencesManager = PreferencesManager.getInstance(mContext);
        initUI();
        initListener();
        callGetCategory();
        return view;
    }

    public void initUI() {
        tv_no_items = (TextView) view.findViewById(R.id.tv_no_items);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler_view_horizontal = (RecyclerView) view.findViewById(R.id.recycler_view_horizontal);
        mLayoutManager = new GridLayoutManager(mContext, 2);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addOnScrollListener(recyclerViewOnScrollListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_horizontal.setLayoutManager(layoutManager);
//        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                callProductList(preferencesManager.getselected_cat(), hashMapProductList.size());
//            }
//        };
//        // Adds the scroll listener to RecyclerView
//        recycler_view.addOnScrollListener(scrollListener);
    }

    public void clearAllProducts() {
        hashMapProductList.clear();

        if (homeAdapter != null)
            homeAdapter.notifyDataSetChanged();
    }

    public void callGetCategory() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        new WebService(mContext, this, hashMap, "getCategory").execute(AppConstants.BASE_URL + AppConstants.GET_CATEGORY);

    }

    public void callProductList(String cat, int endpoint, boolean isLoadMode) {
//        clearAllProducts();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("category", preferencesManager.getselected_cat());
        hashMap.put("from", endpoint + "");
        hashMap.put("limit", 10 + "");
        current_cat = cat;

        WebService webService = new WebService(mContext, this, hashMap, "productList");
        webService.setProgress(!isLoadMode);
        webService.execute(AppConstants.BASE_URL + AppConstants.PRODUCT_LIST);
    }

    public void callAddToCartWS(String slug, int endpoint) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("product", slug);
        hashMap.put("quantity", 1 + "");
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "addToCart").execute(AppConstants.BASE_URL + AppConstants.ADD_CART);
    }


    public void initListener() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if (result.equals("")) {
            return;
        }

        switch (TAG) {
            case "getCategory":
                if (jsonObject.optString("status").equals("success")) {
                    hashMapCategory.clear();
                    JSONArray jsonCat = jsonObject.optJSONArray("categories");

                    CategoryModel categoryModel1 = new CategoryModel();
                    categoryModel1.setName("All");
                    hashMapCategory.put(0, categoryModel1);

                    int k = 1;
                    for (int i = 0; i < jsonCat.length(); i++) {
                        JSONObject json1 = jsonCat.optJSONObject(i);
                        JSONObject jsonObj = json1.optJSONObject("category");
                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCode(jsonObj.optString("code"));
                        categoryModel.setId(jsonObj.optString("id"));
                        categoryModel.setImage(jsonObj.optString("image"));
                        categoryModel.setName(jsonObj.optString("name"));
                        categoryModel.setSlug(jsonObj.optString("slug"));
                        hashMapCategory.put(k, categoryModel);
                        k++;

                    }

                    CategoryAdapter categoryAdapter = new CategoryAdapter(mContext, this, hashMapCategory);
                    recycler_view_horizontal.setAdapter(categoryAdapter);
                    callProductList("", 0, false);

                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;

            case "productList":

                if (jsonObject.optString("status").equals("success")) {
                    JSONArray jsonCat = jsonObject.optJSONArray("products");
                    total_products = jsonObject.optString("total_products");
                    if (!prev_cat.equals(current_cat))
                        hashMapProductList.clear();

                    int lastSize = hashMapProductList.size();
                    try {
                        if (hashMapProductList.get(lastSize - 1).getName().equalsIgnoreCase("load")) {
                            hashMapProductList.remove(lastSize - 1);
                            lastSize = hashMapProductList.size();
                        }
                    }catch (Exception e){e.printStackTrace();}

                    for (int i = 0; i < jsonCat.length(); i++) {

                        JSONObject jsonObj = jsonCat.optJSONObject(i);
                        ProductListModel productListModel = new ProductListModel();
                        productListModel.setName(jsonObj.optString("name"));
                        productListModel.setSlug(jsonObj.optString("slug"));
                        productListModel.setAvailable(jsonObj.optString("available"));
                        productListModel.setImage(jsonObj.optString("image"));
                        productListModel.setOriginalPrice(jsonObj.optString("originalPrice"));
                        productListModel.setProduct_type(jsonObj.optString("product_type"));
                        productListModel.setRating(jsonObj.optString("rating"));
                        productListModel.setSellingPrice(jsonObj.optString("sellingPrice"));
                        productListModel.setThumb(jsonObj.optString("thumb"));
                        hashMapProductList.put(hashMapProductList.size(), productListModel);

                    }

                    tv_no_items.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
//                    if(prev_cat.equals(current_cat))
//                    {
                    if (homeAdapter != null && lastSize != 0) {
                        homeAdapter.notifyItemRangeInserted(lastSize + 1, (hashMapProductList.size() - lastSize));
                    } else {
                        homeAdapter = new HomeAdapter(mContext, this, hashMapProductList);
                        recycler_view.setAdapter(homeAdapter);
                    }
//                    }
//                    else
//                    {
//                        homeAdapter = new HomeAdapter(mContext, this, hashMapProductList);
//                        recycler_view.setAdapter(homeAdapter);
//                    }

                    prev_cat = current_cat;
                    isLoading = false;
                } else {
                    if (prev_cat.equals(current_cat)){
                        try {
                            if (hashMapProductList.get(hashMapProductList.size() - 1).getName().equalsIgnoreCase("load")) {
                                hashMapProductList.remove(hashMapProductList.size() - 1);
                            }
                        }catch (Exception e){e.printStackTrace();}
                        Toast.makeText(mContext, "No more data available", Toast.LENGTH_SHORT).show();
                    }else{
                        hashMapProductList.clear();
                        homeAdapter.notifyDataSetChanged();
                        tv_no_items.setVisibility(View.VISIBLE);
                        recycler_view.setVisibility(View.GONE);
                        Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                    isLoading = false;
                }
                break;

            case "addToCart":
                if (jsonObject.optString("status").equals("success")) {
                    callCartListingWS();
                    Toast.makeText(mContext, "Item added to cart successfully", Toast.LENGTH_SHORT).show();
                } else {
                    callCartListingWS();
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
            case "cartListing":
                if (jsonObject.optString("status").equals("success")) {

                    JSONArray jsonArray = jsonObject.optJSONArray("items");
                    DashboardActivity.quantity = jsonArray.length();
                    ((DashboardActivity) mContext).tv_count.setText(DashboardActivity.quantity + "");
                    ((DashboardActivity) mContext).tv_count.setVisibility(View.VISIBLE);
                    ((DashboardActivity) mContext).img_cart.setVisibility(View.INVISIBLE);
                } else {
                    DashboardActivity.quantity = 0;
                    ((DashboardActivity) mContext).tv_count.setVisibility(View.INVISIBLE);
                    ((DashboardActivity) mContext).img_cart.setVisibility(View.VISIBLE);
                }

                break;


        }
    }

    public void callCartListingWS() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "cartListing").execute(AppConstants.BASE_URL + AppConstants.CART_LISTING);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
