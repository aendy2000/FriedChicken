package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.FoodDetailtActivity;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.CheckoutDomain;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckoutViewHolder> {

    ArrayList<CheckoutDomain> listCheckout;
    Context myContex;
    NumberFormat formatter = new DecimalFormat("#,###");
    public CheckOutAdapter (Context context, ArrayList<CheckoutDomain> listCheckout){
        this.listCheckout = listCheckout;
        this.myContex = context;
    }


    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_checkout, parent, false);
        return new CheckOutAdapter.CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CheckoutDomain checkoutDomain = listCheckout.get(position);
        if (checkoutDomain == null)
            return;

        holder.nameCheckout.setText(checkoutDomain.getTen());
        holder.giaCheckout.setText(formatter.format(Integer.valueOf(checkoutDomain.getGia())));
        holder.soluongCheckout.setText(checkoutDomain.getSoluong());
        holder.tongCheckout.setText(formatter.format(Integer.valueOf(checkoutDomain.getTong())));
        Picasso.with(myContex).load(checkoutDomain.getHinhanh()).into(holder.foodCheckout);

        holder.constrafoodcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContex.getApplicationContext(), FoodDetailtActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Food", checkoutDomain.getMamonan());
                bundle.putSerializable("idUser", checkoutDomain.getIdUser());
                intent.putExtras(bundle);
                myContex.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listCheckout != null)
            return listCheckout.size();
        return 0;
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodCheckout;
        private TextView nameCheckout, giaCheckout, tongCheckout, soluongCheckout;
        private ConstraintLayout constrafoodcheckout;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);

            foodCheckout = itemView.findViewById(R.id.img_Food_checkout);
            nameCheckout = itemView.findViewById(R.id.Food_Name_checkout);
            giaCheckout = itemView.findViewById(R.id.food_Price_checkout);
            soluongCheckout = itemView.findViewById(R.id.food_quality_checkout);
            tongCheckout = itemView.findViewById(R.id.food_SumPrice_checkout);
            constrafoodcheckout = itemView.findViewById(R.id.listFoodCheckout);

        }
    }
}
