package com.cloverinfosoft.fooddelivery.Services;


public class AppConstants {

    public static int MY_APP_PERMISSIONS = 100;
    public static String BASE_URL = "http://159.65.145.27/admin/api/";

    public static String CREATE_COOKIE="login/createCookie";
    public static String LOGIN="login/userLogin";
    public static String RESET="login/sendResetPasswordLink";
    public static String TOKEN="83597d10be49de34b28c637ffcbcea48";
    public static String STORE_CODE="126158";
    public static String AUTH_KEY="a0b1a25fe5f9c46adb3da41fc5f1975d";
    public static String GET_CATEGORY="category/getCategories";
    public static String PRODUCT_LIST="products/productList";
    public static String ADD_CART="cart/addToCart";
    public static String CART_LISTING="cart/getCartProducts";
    public static String UPDATE_CART_QUANTITY="cart/updateCartQuantity";
    public static String REMOVE_CART_ITEM="cart/removeCartProduct";
    public static String READY_TO_CHECKOUT="cart/cartToDummyOrder";
    public static String MAKE_COD="cart/dummyOrdertoLiveOrder";
    public static String GET_ORDERS="orders/getOrders";
    public static String RATING="reviewRatings/writeReview";
    public static String LOGOUT="login/logout";
    public static String makeDummyOrderReadyForOnlinePayment="cart/makeDummyOrderReadyForOnlinePayment";
    public static String updateDummyOrderBytxnid="cart/updateDummyOrderBytxnid";
    public static String dummyOrdertoLiveOrder="cart/dummyOrdertoLiveOrder";

}
