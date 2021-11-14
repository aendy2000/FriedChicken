package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.CartActivity;
import com.example.project.Activity.FoodDetailtActivity;
import com.example.project.Activity.MainActivity;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.FoodDomain;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<CartDomain> listCart;
    Context myContex;

    public CartAdapter(Context context, ArrayList<CartDomain> listCart) {
        this.listCart = listCart;
        this.myContex = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_shopping_cart, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartDomain cartDomain = listCart.get(position);
        if (cartDomain == null)
            return;

        holder.nameCart.setText(cartDomain.getTen());
        holder.giaCart.setText("Giá: " + cartDomain.getGia());
        holder.soluongCart.setText(cartDomain.getSoluong());
        holder.tongCart.setText("Tổng: " + cartDomain.getTong());

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(cartDomain.getHinhanh(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.foodCart);

        holder.foodCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetailtFood(cartDomain);
            }
        });

        holder.tangCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(holder.soluongCart.getText().toString()) < 100) {
                    int tongsl = Integer.valueOf(holder.soluongCart.getText().toString()) + 1;
                    int tonggia = Integer.valueOf(holder.giaCart.getText().toString().split("\\.")[0].substring(5)) * tongsl;
                    String sTonggia = "";
                    if (tonggia < 1000) {
                        sTonggia = tonggia + ".000 VND";
                    } else if (tonggia < 10000) {
                        String[] formatGia = String.valueOf(tonggia).split("");
                        String chuoi = formatGia[1] + ".";
                        for (int i = 2; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    } else if (tonggia < 100000) {
                        String[] formatGia = String.valueOf(tonggia).split("");
                        String chuoi = formatGia[1] + formatGia[2] + ".";
                        for (int i = 3; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    } else if (tonggia < 1000000) {
                        String[] formatGia = String.valueOf(tonggia).split("");
                        String chuoi = formatGia[1] + formatGia[2] + formatGia[3] + ".";
                        for (int i = 4; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    }
                    holder.tongCart.setText("Tổng: " + sTonggia);
                    holder.soluongCart.setText(String.valueOf(tongsl));

                    String GIOHANG = "GH" + cartDomain.getIduser().toString().split("K")[1];
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("GioHang");
                    reference.child(GIOHANG).child(cartDomain.getDanhmuc()).child(cartDomain.getMamonan()).child("SoLuong").setValue(tongsl);
                    reference.child(GIOHANG).child(cartDomain.getDanhmuc()).child(cartDomain.getMamonan()).child("Tong").setValue(sTonggia);
                }
            }
        });
        holder.giamCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(holder.soluongCart.getText().toString()) > 1) {
                    int tongsl = Integer.valueOf(holder.soluongCart.getText().toString()) - 1;
                    int tonggia = Integer.valueOf(holder.giaCart.getText().toString().split("\\.")[0].substring(5)) * tongsl;
                    String sTonggia = "";
                    if (tonggia < 1000) {
                        sTonggia = tonggia + ".000 VND";
                    } else if (tonggia < 10000) {
                        String[] formatGia = String.valueOf(tonggia).split("");
                        String chuoi = formatGia[1] + ".";
                        for (int i = 2; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    } else if (tonggia < 100000) {
                        String[] formatGia = String.valueOf(tonggia).split("");
                        String chuoi = formatGia[1] + formatGia[2] + ".";
                        for (int i = 3; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    } else if (tonggia < 1000000) {
                        String[] formatGia = String.valueOf(tonggia).split("");
                        String chuoi = formatGia[1] + formatGia[2] + formatGia[3] + ".";
                        for (int i = 4; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    }
                    holder.tongCart.setText("Tổng: " + sTonggia);
                    holder.soluongCart.setText(String.valueOf(tongsl));

                    String GIOHANG = "GH" + cartDomain.getIduser().toString().split("K")[1];
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("GioHang");
                    reference.child(GIOHANG).child(cartDomain.getDanhmuc()).child(cartDomain.getMamonan()).child("SoLuong").setValue(tongsl);
                    reference.child(GIOHANG).child(cartDomain.getDanhmuc()).child(cartDomain.getMamonan()).child("Tong").setValue(sTonggia);
                }
            }
        });
        holder.deleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("GioHang");
                String GIOHANG = "GH" + cartDomain.getIduser().toString().split("K")[1];
                myRef.child(GIOHANG).child(cartDomain.getDanhmuc()).child(cartDomain.getMamonan()).removeValue();

                Intent Cart = new Intent(myContex, CartActivity.class);
                Cart.putExtra("idUser", cartDomain.getIduser());
                ((CartActivity)myContex).finish();
                myContex.startActivity(Cart);
                Toast.makeText(myContex, "Đã xóa " + cartDomain.getTen() + " khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickGoToDetailtFood(CartDomain cartDomain) {
        Intent intent = new Intent(myContex.getApplicationContext(), FoodDetailtActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Food", cartDomain.getMamonan());
        bundle.putSerializable("idUser", cartDomain.getIduser());
        intent.putExtras(bundle);
        myContex.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (listCart != null)
            return listCart.size();
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodCart, giamCart, tangCart, deleteCart;
        private TextView nameCart, giaCart, tongCart, soluongCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodCart = itemView.findViewById(R.id.img_Food_cart);
            giamCart = itemView.findViewById(R.id.Cart_food_giam);
            tangCart = itemView.findViewById(R.id.Cart_food_tang);

            nameCart = itemView.findViewById(R.id.Food_Name_cart);
            giaCart = itemView.findViewById(R.id.food_Price_cart);
            soluongCart = itemView.findViewById(R.id.food_soluong_cart);
            tongCart = itemView.findViewById(R.id.food_Tong_cart);
            deleteCart = itemView.findViewById(R.id.img_delete_cart);

        }
    }
}
