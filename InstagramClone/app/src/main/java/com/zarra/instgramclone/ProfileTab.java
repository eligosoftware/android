package com.zarra.instgramclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private EditText edtProfile_name,edtProfile_bio,edtProfile_profession,edtProfile_hobbies,
            edtProfile_fav_sport;
    private Button btn_update,btn_logout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileTab.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileTab newInstance(String param1, String param2) {
        ProfileTab fragment = new ProfileTab();
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
        View view= inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfile_bio=view.findViewById(R.id.profile_bio);
        edtProfile_name=view.findViewById(R.id.profile_name);
        edtProfile_hobbies=view.findViewById(R.id.profile_hobbies);
        edtProfile_profession=view.findViewById(R.id.profile_profession);
        edtProfile_fav_sport=view.findViewById(R.id.profile_fav_sport);

        btn_update=view.findViewById(R.id.btn_update);

        btn_logout=view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                getActivity().finish();
            }
        });

        final ParseUser parseUser= ParseUser.getCurrentUser();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeProfileDate(parseUser);
            }
        });
        readProfileDate(ParseUser.getCurrentUser());
        return view;
    }


    private void readProfileDate(ParseUser user){
        edtProfile_name.setText(user.get("profileName")!=null?user.get("profileName").toString():"");
        edtProfile_bio.setText(user.get("profileBio")!=null?user.get("profileBio").toString():"");
        edtProfile_profession.setText(user.get("profileProfession")!=null?user.get("profileProfession").toString():"");
        edtProfile_hobbies.setText(user.get("profileHobbies")!=null?user.get("profileHobbies").toString():"");
        edtProfile_fav_sport.setText(user.get("profileFavSport")!=null?user.get("profileFavSport").toString():"");
    }
    private void writeProfileDate(ParseUser user){
        user.put("profileName",edtProfile_name.getText().toString());
        user.put("profileBio",edtProfile_bio.getText().toString());
        user.put("profileProfession",edtProfile_profession.getText().toString());
        user.put("profileHobbies",edtProfile_hobbies.getText().toString());
        user.put("profileFavSport",edtProfile_fav_sport.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    FancyToast.makeText(getContext(),"Updated info",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();

                }
                else {
                    FancyToast.makeText(getContext(),e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                }

                progressDialog.dismiss();
            }
        });

    }

}