package com.eligosoftware.notifon;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eligosoftware.notifon.database.NotifonDBHelper;
import com.eligosoftware.notifon.database.TranslatesBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by mragl on 12.11.2016.
 */

public class EditTrunitsFragment extends Fragment {


    public interface Callbacks{
        void onEditTrunit(Trunit selectedValue,int operation_code,boolean use_list_value,boolean list_value);
        void OnCopyTrunit(Trunit selectedValue,int operation_code);
        void onAddTrunit(Trunit selectedValue,int operation_code);
    }


    private RecyclerView mRecyclerView;
    private TrunitAdapter mTrunitAdapter;
    private TranslatesBase mTranslatesBase;
    private Trunit selectedTrunit;
    private Callbacks mCallbacks;
    private FloatingActionButton fab_add_trunit;
    private String query;

    private ListView mListView;
    private SearchView searchView;

    private Map<Trunit,Boolean> changedTrunits;

    private Spinner mknowitSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.edit_trunits,container,false);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.edit_trunits_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       FastScroller fastScroller = (FastScroller) v.findViewById(R.id.fast_scroller);
//
//
        fastScroller.setRecyclerView(mRecyclerView);

        //updateUI();
        changedTrunits=new HashMap();
        fab_add_trunit=(FloatingActionButton)v.findViewById(R.id.fab_add_trunit);
        fab_add_trunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onAddTrunit(new Trunit(),3);
            }
        });

        mListView=(ListView)v.findViewById(R.id.search_list);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks=(Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    private void updateUI(){
        if (query!=null && !query.contains("%"))
        {
            query="word like '%"+query+"%'";
        }
         mTranslatesBase=TranslatesBase.get(getActivity());
        List<Trunit> trunits=mTranslatesBase.getTrunits(query);
       if (mTrunitAdapter==null){
            mTrunitAdapter=new TrunitAdapter(trunits);
           mRecyclerView.setAdapter(mTrunitAdapter);}
        else{
           mTrunitAdapter.setTrunits(trunits);
            mTrunitAdapter.notifyDataSetChanged();
       }
        if (((EditTrunitsActivity)getActivity()).scrolldown){
            ((EditTrunitsActivity)getActivity()).scrolldown=false;
            mRecyclerView.getLayoutManager().scrollToPosition(trunits.size()-1);

        }
        mTranslatesBase.deleteAllSTrunits();
/*        for(Trunit t:trunits){
        mTranslatesBase.createSTrunit(t.getWord(),t.getPartsp(),t.getDescr());}*/
        mTranslatesBase.createSTrunits();
        }

    private class TrunitHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private CheckBox knowit_chk;
        private TextView word_txt,descr_txt;
        private ImageView diff_image;
        private Trunit mTrunit;

        public TrunitHolder(View itemView) {
            super(itemView);
            knowit_chk=(CheckBox)itemView.findViewById(R.id.knowit_list_row);
            knowit_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    changedTrunits.put(mTrunit,knowit_chk.isChecked());
                }
            });

            word_txt=(TextView)itemView.findViewById(R.id.word_list_row);
            descr_txt=(TextView)itemView.findViewById(R.id.descr_list_row);
            diff_image=(ImageView) itemView.findViewById(R.id.diff_list_row);
            itemView.setOnCreateContextMenuListener(this);
        }

        private void bindTrunit(Trunit trunit){
            mTrunit=trunit;
            knowit_chk.setChecked(mTrunit.getKnow()==1);
            word_txt.setText(mTrunit.getWord()+" -"+mTrunit.getPartsp());
            descr_txt.setText(mTrunit.getDescr());

            int diff=R.mipmap.medium_icon;
            switch (mTrunit.getLevel()){
                case "easy":
                    diff=R.mipmap.easy_icon;
                    break;
                case "hard":
                    diff=R.mipmap.hard_icon;
                    break;
                default:
                    break;
            }
            diff_image.setImageDrawable(getResources().getDrawable(diff));
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            selectedTrunit=mTrunit;
            contextMenu.setHeaderTitle(mTrunit.getWord());
            contextMenu.add(0,1,0,"Edit");
            contextMenu.add(0,2,0,"Copy");
            contextMenu.add(0,3,0,"Delete");


        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    private void showResults(String query){
        Cursor cursor=mTranslatesBase.searchSTrunit((query!=null?query.toString():"@@@@"));

        if (cursor==null){
            //
        }
        else {
            String[] from=new String[]{
                    NotifonDBHelper.KEY_WORD,
                    NotifonDBHelper.KEY_PARTSP,
                    NotifonDBHelper.KEY_DESCR
            };
            int[] to=new int[]{
                R.id.trunit_search_word,
                    R.id.trunit_search_partsp,
                    R.id.trunit_search_description
            };

            SimpleCursorAdapter result=new SimpleCursorAdapter(getActivity(),R.layout.trunit_result,cursor,from,to);
            mListView.setAdapter(result);

            mListView.setOnItemClickListener(new ListView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    searchView.setQuery(((TextView)view.findViewById(R.id.trunit_search_word)).getText(),true);
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_trunits_menu,menu);

        MenuItem item=menu.findItem(R.id.menu_item_search);

        searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query1) {
                showResults("");
                query=query1;
                mRecyclerView.bringToFront();
                updateUI();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    showResults("");
                    query=null;
                    mRecyclerView.bringToFront();
                    updateUI();
                }
                else {
                showResults(newText+"*");
                mListView.bringToFront();}
                return false;
            }


        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }

        );
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                showResults("");
                query=null;
                mRecyclerView.bringToFront();
                updateUI();
                return false;
            }
        });
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                boolean use_l_value=false;
                boolean l_value=false;
                if (changedTrunits.containsKey(selectedTrunit))
                {
                    use_l_value=true;
                    l_value=changedTrunits.get(selectedTrunit);
                }
               mCallbacks.onEditTrunit(selectedTrunit,1,use_l_value,l_value);
                break;
            case 2:
                mCallbacks.OnCopyTrunit(selectedTrunit,2);

                break;
            case 3:
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.warning_delete_trunit)+" \""+selectedTrunit.getWord()+"\"")
                        .setPositiveButton(R.string.warning_delete_trunit_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TranslatesBase.get(getActivity()).deleteTrunit(selectedTrunit);
                                changedTrunits.remove(selectedTrunit);
                                updateUI();
                            }
                        }

                        )
                        .setNegativeButton(R.string.warning_delete_trunit_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               //do nothing
                            }
                        })
                        .show();
                break;
            default:
                selectedTrunit=null;
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class TrunitAdapter extends RecyclerView.Adapter<TrunitHolder>{

        public void setTrunits(List<Trunit> trunits) {
            mTrunits = trunits;
        }

        private List<Trunit> mTrunits;

        private TrunitAdapter(List<Trunit> trunits){mTrunits=trunits;}

        @Override
        public TrunitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View v=inflater.inflate(R.layout.list_row,parent,false);

            return new TrunitHolder(v);
        }

        @Override
        public void onBindViewHolder(TrunitHolder holder, int position) {
            Trunit mTrunit=mTrunits.get(position);
            holder.bindTrunit(mTrunit);
        }

        @Override
        public int getItemCount() {
            return mTrunits.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (changedTrunits.size()>0){
            for(Trunit t:changedTrunits.keySet()){
                mTranslatesBase.updateTrunit(t.getWordid(),changedTrunits.get(t));
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onPause() {
        if (changedTrunits.size()>0){
            for(Trunit t:changedTrunits.keySet()){
                mTranslatesBase.updateTrunit(t.getWordid(),changedTrunits.get(t));
            }
        }
        changedTrunits=new HashMap();
        super.onPause();
    }
}
