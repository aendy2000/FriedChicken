package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.CartManagerDetailtActivity;
import com.example.project.Domain.CartManagerDomain;
import com.example.project.R;
import java.util.ArrayList;

public class CartManagerAdapter extends RecyclerView.Adapter<CartManagerAdapter.CartManagerViewHolder> {
    ArrayList<CartManagerDomain> listCart;
    Context myContex;

    public CartManagerAdapter(Context context, ArrayList<CartManagerDomain> listCart) {
        this.listCart = listCart;
        this.myContex = context;
    }

    @NonNull
    @Override
    public CartManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_cart_manager, parent, false);
        return new CartManagerAdapter.CartManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartManagerViewHolder holder, int position) {
        CartManagerDomain cartManagerDomain = listCart.get(position);
        if (cartManagerDomain == null)
            return;
        holder.somon.setText(cartManagerDomain.getSomon() + " món");
        holder.taikhoan.setText(cartManagerDomain.getTaikhoan().substring(2));
        holder.detailtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContex, CartManagerDetailtActivity.class);
                intent.putExtra("idGioHang", cartManagerDomain.getTaikhoan());
                myContex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listCart != null)
            return listCart.size();
        return 0;
    }

    public class CartManagerViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout detailtCart;
        private TextView taikhoan, somon;

        public CartManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            detailtCart = itemView.findViewById(R.id.listCartManager);
            taikhoan = itemView.findViewById(R.id.tentk_list_cart_manager);
            somon = itemView.findViewById(R.id.somon_list_cart_manager);
        }
    }
}

