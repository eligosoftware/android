package com.zarra.favoritelistapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ItemViewHolder> {

    private Category mCategory;
    private boolean multiSelect = false;
    private ArrayList<String> selectedItems = new ArrayList<String>();
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (String Stritem : selectedItems) {
                mCategory.getItems().remove(Stritem);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    public ItemsRecyclerAdapter(Category category){
        mCategory=category;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_view_holder,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.update(mCategory.getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return mCategory.getItems().size();
    }

    public void setCategory(Category category) {
        mCategory=category;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_layout;
        public TextView itemTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextView=itemView.findViewById(R.id.item_text_view);
            lin_layout=itemView.findViewById(R.id.lin_layout);
        }
        void selectItem(String item){
            if(multiSelect){
                if(selectedItems.contains(item)){
                    selectedItems.remove(item);
                    lin_layout.setBackgroundColor(Color.WHITE);
                } else {
                    selectedItems.add(item);
                    lin_layout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }

        public void update(final String value) {
            itemTextView.setText(String.valueOf(value));
            if(selectedItems.contains(value)){
                lin_layout.setBackgroundColor(Color.LTGRAY);
            } else {
                lin_layout.setBackgroundColor(Color.WHITE);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(value);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(value);
                }
            });
        }
    }
}
