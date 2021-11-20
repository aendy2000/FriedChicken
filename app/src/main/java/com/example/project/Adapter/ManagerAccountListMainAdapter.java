package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.AccountAdminListManagerActivity;
import com.example.project.Activity.AccountUserListManagerActivity;
import com.example.project.Activity.AddAccountAdminActivity;
import com.example.project.Domain.AccountListUserDomain;
import com.example.project.Domain.ManagerAccountListMainDomain;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManagerAccountListMainAdapter extends RecyclerView.Adapter<ManagerAccountListMainAdapter.ManagerAccountListMainViewHolder> {
    Context myContext;
    ArrayList<ManagerAccountListMainDomain> list;

    public ManagerAccountListMainAdapter(Context context, ArrayList<ManagerAccountListMainDomain> list){
        this.list = list;
        this.myContext = context;
    }

    @NonNull
    @Override
    public ManagerAccountListMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_manager_account, parent, false);
        return new ManagerAccountListMainAdapter.ManagerAccountListMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerAccountListMainViewHolder holder, int position) {
        ManagerAccountListMainDomain domain = list.get(position);
        holder.ten.setText(domain.getTen());
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(domain.getHinhanh(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.img);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(domain.getId().equals("1")) {
                    myContext.startActivity(new Intent(myContext, AccountUserListManagerActivity.class));
                } else if (domain.getId().equals("2")) {
                    myContext.startActivity(new Intent(myContext, AccountAdminListManagerActivity.class));
                } else {
                    myContext.startActivity(new Intent(myContext, AddAccountAdminActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ManagerAccountListMainViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView ten;
        private ConstraintLayout layout;

        public ManagerAccountListMainViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.categoryPic_manageraccount);
            ten = itemView.findViewById(R.id.categoryName_manageraccount);
            layout = itemView.findViewById(R.id.mainLayout_manageraccount);
        }
    }

}
