package com.zarra.favoritelistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    //String[] categories={"Hobbies","Sports","Games","Electronic Gadgets","Foods","Countries"};

    interface CategoryIsClicked{
        void categoryIsClicked(Category category);
    }


    private ArrayList<Category> categories;
    private boolean multiSelect = false;
    private ArrayList<Category> selectedItems = new ArrayList<Category>();
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
            for (Category category : selectedItems) {
                categories.remove(category);
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
    private CategoryIsClicked mCategoryIsClicked;


    public CategoryRecyclerAdapter(ArrayList<Category> categories,CategoryIsClicked categoryIsClicked) {
        this.categories = categories;

        this.mCategoryIsClicked=categoryIsClicked;
    }

    public ArrayList<Category> getCategories(){
        return categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.category_viewholder,parent,false);



        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {

        holder.update(categories.get(position),position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addCategory(Category category) {
        categories.add(category);

        notifyItemInserted(categories.size()-1);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cat_layout;
        private TextView txtCategoryNumber;
        private TextView TxtCategoryName;

        private ActionMode mMode;



        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_layout=itemView.findViewById(R.id.cat_layout);
            txtCategoryNumber= itemView.findViewById(R.id.category_number_textview);
            TxtCategoryName= itemView.findViewById(R.id.category_name_textview);

        }
        /*
        public TextView getTxtCategoryNumber() {
            return txtCategoryNumber;
        }

        public TextView getTxtCategoryName() {
            return TxtCategoryName;
        }
        */
        void selectItem(Category item){
            if(multiSelect){
                if(selectedItems.contains(item)){
                    selectedItems.remove(item);

                    cat_layout.setBackgroundColor(Color.parseColor("#FD9803"));
                    if (selectedItems.size()==0)
                        mMode.finish();
                } else {
                    selectedItems.add(item);
                    cat_layout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }

        public void update(final Category category, final Integer position) {

            txtCategoryNumber.setText(String.valueOf(position+1));
            TxtCategoryName.setText(category.getName());

            if(selectedItems.contains(category)){
                cat_layout.setBackgroundColor(Color.LTGRAY);
            } else {
                cat_layout.setBackgroundColor(Color.parseColor("#FD9803"));
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mMode=((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(category);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(multiSelect){
                    selectItem(category);}
                    else
                    {
                    mCategoryIsClicked.categoryIsClicked(categories.get(position));
                }}
            });
        }
    }

}
