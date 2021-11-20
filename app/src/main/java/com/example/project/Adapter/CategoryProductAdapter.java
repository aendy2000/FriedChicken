package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.Domain.CategoryProductDomain;
import com.example.project.R;

import java.util.List;

public class CategoryProductAdapter extends ArrayAdapter<CategoryProductDomain> {

    public CategoryProductAdapter(@NonNull Context context, int resource, @NonNull List<CategoryProductDomain> objects) {

        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_category_product, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tv_selected_category_product);

        CategoryProductDomain category = this.getItem(position);
        if(category != null) {
            tvCategory.setText(category.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_product, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tv_name_category_product);

        CategoryProductDomain category = this.getItem(position);
        if(category != null) {
            tvCategory.setText(category.getName());
        }
        return convertView;
    }
}
