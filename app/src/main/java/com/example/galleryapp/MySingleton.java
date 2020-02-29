package com.example.galleryapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    public static MySingleton myInstance;
    public RequestQueue requestQueue;
    public static Context myContext;

    public MySingleton(Context context){
        myContext = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(myContext);
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){
        if(myInstance==null){
            myInstance = new MySingleton(context);
        }
        return myInstance;
    }
    public<T> void addRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
