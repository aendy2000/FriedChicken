package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.ListFoodActivity;
import com.example.project.Domain.CategoryDomain;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<CategoryDomain> categoryDomains;
    Context myContex;

    public CategoryAdapter(Context context, ArrayList<CategoryDomain> categoryDomains) {
        this.categoryDomains = categoryDomains;
        this.myContex = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cat, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryDomain categoryDomain = categoryDomains.get(position);

        holder.categoryName.setText(categoryDomains.get(position).getTitle());
        Picasso.with(myContex).load(categoryDomain.getPic()).into(holder.categoryPic);
        if (position % 2 == 0)
            holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.category_background3));
        else
            holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.category_background2));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContex, ListFoodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Food", categoryDomain.getIdDm());
                bundle.putSerializable("DanhMuc", categoryDomain.getTitle());
                bundle.putSerializable("idUser", categoryDomain.getUserID());
                bundle.putSerializable("keySearch", "KeyNull");
                intent.putExtras(bundle);
                myContex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryDomains != null) {
            return categoryDomains.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic = itemView.findViewById(R.id.categoryPic);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
