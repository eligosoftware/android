package com.eligosoftware.notifon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.eligosoftware.notifon.database.TranslatesBase;

import java.util.List;

/**
 * Created by mragl on 09.11.2016.
 */

public class MainFragment extends Fragment {
    private TextView mWord,mDescr,mPartsp;
    private CheckBox mKnowIt;
    private ImageView mDiff_image;
    private Button nextTrunit,edit_translations;
    private TranslatesBase trbase;
    private boolean db_know_it;
    private Trunit mTrunit;

   /* private RecyclerView mRecyclerView;
    private TrunitAdapter mAdapter;*/
    public static MainFragment newInstance(){
        return new MainFragment();
    }


   /* private void updateUI(){
        TranslatesBase crimeLab=TranslatesBase.get(getActivity());
        List<Trunit> trunits=crimeLab.getTrunits();
        if (mAdapter==null){
            mAdapter=new TrunitAdapter(trunits);
            mRecyclerView.setAdapter(mAdapter);
        }

    }*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_main,container,false);
        trbase=TranslatesBase.get(getActivity());

        Trunit random_trunit=(Trunit)getActivity().getIntent().getSerializableExtra(MainActivity.NOTIFICATION_TRUNIT);

        if (random_trunit==null){
        random_trunit=trbase.getRandomTrunit();
        }

        mWord=(TextView)v.findViewById(R.id.word);
        mDescr=(TextView)v.findViewById(R.id.definition);
        mDiff_image=(ImageView)v.findViewById(R.id.diff_image);
        nextTrunit=(Button)v.findViewById(R.id.next_trunit);

        nextTrunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db_know_it!=mKnowIt.isChecked()){
                    trbase.updateTrunit(mTrunit.getWordid(),mKnowIt.isChecked());
                }
                setRandomQuestion(trbase.getRandomTrunit());

            }
        });
        mKnowIt=(CheckBox)v.findViewById(R.id.knowit);
        mPartsp=(TextView)v.findViewById(R.id.partsp);


        edit_translations=(Button)v.findViewById(R.id.edit_trunits);
        edit_translations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        //start activity for to show and edit list of trunits
                if (db_know_it!=mKnowIt.isChecked()){
                    trbase.updateTrunit(mTrunit.getWordid(),mKnowIt.isChecked());
                }
                startActivity(new Intent(getActivity(),EditTrunitsActivity.class));
            }
        });

        setRandomQuestion(random_trunit);
        /*mRecyclerView=(RecyclerView)v.findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();*/
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setRandomQuestion(Trunit random_trunit){
        mWord.setText(random_trunit.getWord());
        mDescr.setText(random_trunit.getDescr());
        mPartsp.setText(random_trunit.getPartsp());
        mKnowIt.setChecked(random_trunit.getKnow()==1);
        db_know_it=mKnowIt.isChecked();
        mTrunit=random_trunit;

        int diff=R.mipmap.medium_icon;
        switch (random_trunit.getLevel()){
            case "easy":
                diff=R.mipmap.easy_icon;
                break;
            case "hard":
                diff=R.mipmap.hard_icon;
                break;
            default:
                break;
        }
        mDiff_image.setImageDrawable(getResources().getDrawable(diff));
    }

    @Override
    public void onResume() {
        super.onResume();
       // setRandomQuestion(trbase.getRandomTrunit());
    }
/*private class TrunitHolder extends RecyclerView.ViewHolder{
        private TextView mWord,mDescr,mLang,mDescrLang;
        private Trunit mTrunit;
        public TrunitHolder(View itemView) {
            super(itemView);
            mWord=(TextView)itemView.findViewById(R.id.list_item_word_text_view);
            mDescr=(TextView)itemView.findViewById(R.id.list_item_descr_text_view);
            mLang=(TextView)itemView.findViewById(R.id.list_item_lang_text_view);
            mDescrLang=(TextView)itemView.findViewById(R.id.list_item_descr_lang_text_view);
        }
        public void bindTrunit(Trunit trunit){
            mTrunit=trunit;
            mWord.setText(mTrunit.getWord());
            mDescr.setText(mTrunit.getDescr());
            mLang.setText(mTrunit.getLang());
            mDescrLang.setText(mTrunit.getDescr_lang());
        }
    }

    private class TrunitAdapter extends RecyclerView.Adapter<TrunitHolder>{

        private List<Trunit> mTrunits;

        public TrunitAdapter(List<Trunit> trunits){
            mTrunits=trunits;
        }

        @Override
        public TrunitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            View view=layoutInflater.inflate(R.layout.list_item_trunit,parent,false);
            return new TrunitHolder(view);
        }

        @Override
        public void onBindViewHolder(TrunitHolder holder, int position) {
            Trunit trunit=mTrunits.get(position);
            holder.bindTrunit(trunit);
        }

        @Override
        public int getItemCount() {
            return mTrunits.size();
        }
    }*/
}
