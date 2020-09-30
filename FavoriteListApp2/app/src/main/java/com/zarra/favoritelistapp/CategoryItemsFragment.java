package com.zarra.favoritelistapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryItemsFragment extends Fragment {

    private static final String CATEGORY_ARGS = "categoryargs";
    private RecyclerView itemsRecyclerView;

    Category category;


    public CategoryItemsFragment() {
        // Required empty public constructor
    }


    public static CategoryItemsFragment newInstance(Category category) {
        CategoryItemsFragment fragment=new CategoryItemsFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(CATEGORY_ARGS,category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            category = (Category) getArguments().getSerializable(CATEGORY_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category_items, container, false);
        itemsRecyclerView=view.findViewById(R.id.recycler_view_items);
        itemsRecyclerView.setAdapter(new ItemsRecyclerAdapter(category));
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void addItemToCategory(String item) {
        category.getItems().add(item);

        ItemsRecyclerAdapter adapter= (ItemsRecyclerAdapter) itemsRecyclerView.getAdapter();
        adapter.setCategory(category);
        adapter.notifyDataSetChanged();

    }
}