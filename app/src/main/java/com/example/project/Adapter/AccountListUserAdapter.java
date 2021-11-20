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

import com.example.project.Activity.AccountAdminListManagerActivity;
import com.example.project.Activity.AccountDetailtUserActivity;
import com.example.project.Activity.AccountUserListManagerActivity;
import com.example.project.Domain.AccountListUserDomain;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AccountListUserAdapter extends RecyclerView.Adapter<AccountListUserAdapter.AccountListUserMainViewHolder> {
    ArrayList<AccountListUserDomain> list;
    Context myContext;

    public AccountListUserAdapter(Context context, ArrayList<AccountListUserDomain> list) {
        this.list = list;
        this.myContext = context;
    }

    @NonNull
    @Override
    public AccountListUserMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_account_user_manager, parent, false);
        return new AccountListUserAdapter.AccountListUserMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListUserMainViewHolder holder, int position) {
        AccountListUserDomain domain = list.get(position);
        holder.ten.setText(domain.getTen());
        holder.tk.setText(domain.getMatk().substring(2));

        if(!domain.getAvatar().equals("Null"))
            Picasso.with(myContext).load(domain.getAvatar()).into(holder.img);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, AccountDetailtUserActivity.class);
                intent.putExtra("idUser", domain.getMatk());
                intent.putExtra("Role", domain.getRole());
                if(domain.getRole().equals("User"))
                    ((AccountUserListManagerActivity) myContext).finish();
                else
                    ((AccountAdminListManagerActivity) myContext).finish();
                myContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AccountListUserMainViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView ten, tk;
        private ConstraintLayout layout;

        public AccountListUserMainViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_list_account_user_manager);
            tk = itemView.findViewById(R.id.tv_tk_list_account_user_manager);
            ten = itemView.findViewById(R.id.tv_ten_list_account_user_manager);
            layout = itemView.findViewById(R.id.listAccountUserManager);
        }
    }
}
