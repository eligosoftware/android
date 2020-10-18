package com.zarra.instgramclone;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class MyPosts extends Fragment {
    private String username;

    private LinearLayout mLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username= ParseUser.getCurrentUser().getUsername();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_posts, container, false);



        getActivity().setTitle(username+"'s posts");

        mLinearLayout=view.findViewById(R.id.linearlayout);

        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username",username);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(objects.size()>0 && e==null){
                    for(ParseObject object:objects){
                        final TextView description=new TextView(getActivity());
                        description.setText(object.get("image_des")==null? "": object.get("image_des").toString());
                        ParseFile postPicture= (ParseFile) object.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data!=null && e==null){
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView imageView=new ImageView(getActivity());

                                    LinearLayout.LayoutParams imageview_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageview_params.setMargins(5,5,5,5);
                                    imageView.setLayoutParams(imageview_params);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    imageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams desc_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    desc_params.setMargins(5,5,5,5);
                                    description.setLayoutParams(desc_params);
                                    description.setGravity(Gravity.CENTER);
                                    description.setBackgroundColor(Color.RED);
                                    description.setTextColor(Color.WHITE);
                                    description.setTextSize(30f);


                                    mLinearLayout.addView(imageView);
                                    mLinearLayout.addView(description);


                                } else {
                                    FancyToast.makeText(getActivity(),"Something went wrong( "+e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                    getActivity().finish();

                                }
                            }
                        });
                    }
                }

                dialog.dismiss();
            }
        });


        return view;
    }
}