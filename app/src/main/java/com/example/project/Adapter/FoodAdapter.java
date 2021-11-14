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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.FoodDetailtActivity;
import com.example.project.Domain.FoodDomain;
import com.example.project.R;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    ArrayList<FoodDomain> listFood;
    Context myContex;

    public FoodAdapter(Context context, ArrayList<FoodDomain> listFood) {
        this.listFood = listFood;
        this.myContex = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_cat, parent, false);
        return new FoodViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodDomain foodDomain = listFood.get(position);
        if (foodDomain == null)
            return;

        holder.ten.setText(foodDomain.getName());
        holder.mota.setText(foodDomain.getMota());
        holder.gia.setText(foodDomain.getGia());

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(foodDomain.getImgFood(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.imgFood);

        holder.foodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetailtFood(foodDomain);
            }
        });
    }

    private void onClickGoToDetailtFood(FoodDomain foodDomain) {
        Intent intent = new Intent(myContex.getApplicationContext(), FoodDetailtActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Food", foodDomain.getMamonan());
        bundle.putSerializable("idUser", foodDomain.getUserID());
        intent.putExtras(bundle);
        myContex.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (listFood != null)
            return listFood.size();
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFood;
        private TextView ten, mota, gia;
        private ConstraintLayout foodList;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.img_Food);
            ten = itemView.findViewById(R.id.Food_Name);
            mota = itemView.findViewById(R.id.food_Decription);
            gia = itemView.findViewById(R.id.food_Price);
            foodList = itemView.findViewById(R.id.listFood);
        }
    }
}
