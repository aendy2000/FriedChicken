package com.example.project.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.AddProductActivity;
import com.example.project.Activity.CartActivity;
import com.example.project.Activity.FoodDetailtActivity;
import com.example.project.Activity.MainActivity;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.FoodDomain;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<CartDomain> listCart;
    Context myContex;
    NumberFormat formatter = new DecimalFormat("#,###");

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
        if (cartDomain.getIduser().indexOf("Admin-") != -1) {
            holder.tangCart.setVisibility(View.INVISIBLE);
            holder.giamCart.setVisibility(View.INVISIBLE);
            holder.deleteCart.setVisibility(View.INVISIBLE);
            holder.soluongCart.setText("SL: " + cartDomain.getSoluong());
        } else {
            holder.soluongCart.setText(cartDomain.getSoluong());
        }
        holder.nameCart.setText(cartDomain.getTen());
        holder.giaCart.setText("Giá: " + formatter.format(Integer.valueOf(cartDomain.getGia())) + " VND");
        holder.tongCart.setText("Tổng: " + formatter.format(Integer.valueOf(cartDomain.getTong())) + " VND");

        Picasso.with(myContex).load(cartDomain.getHinhanh()).into(holder.foodCart);

        if (cartDomain.getIduser().indexOf("Admin-") == -1) {
            holder.foodCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(myContex.getApplicationContext(), FoodDetailtActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Food", cartDomain.getMamonan());
                    bundle.putSerializable("idUser", cartDomain.getIduser());
                    intent.putExtras(bundle);
                    myContex.startActivity(intent);
                }
            });
            holder.tangCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(holder.soluongCart.getText().toString()) < 100) {
                        int tongsl = Integer.valueOf(holder.soluongCart.getText().toString()) + 1;
                        int tonggia = Integer.valueOf(cartDomain.getGia()) * tongsl;
                        holder.tongCart.setText("Tổng: " + formatter.format(tonggia) + " VND");
                        holder.soluongCart.setText(String.valueOf(tongsl));

                        String GIOHANG = "GH" + cartDomain.getIduser().toString().split("K")[1];
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("GioHang");
                        reference.child(GIOHANG).child(cartDomain.getMamonan()).child("SoLuong").setValue(tongsl);
                        reference.child(GIOHANG).child(cartDomain.getMamonan()).child("Tong").setValue(tonggia);
                    }
                }
            });
            holder.giamCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(holder.soluongCart.getText().toString()) > 1) {
                        int tongsl = Integer.valueOf(holder.soluongCart.getText().toString()) - 1;
                        int tonggia = Integer.valueOf(cartDomain.getGia()) * tongsl;
                        holder.tongCart.setText("Tổng: " + formatter.format(tonggia) + " VND");
                        holder.soluongCart.setText(String.valueOf(tongsl));
                        String GIOHANG = "GH" + cartDomain.getIduser().toString().split("K")[1];
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("GioHang");
                        reference.child(GIOHANG).child(cartDomain.getMamonan()).child("SoLuong").setValue(tongsl);
                        reference.child(GIOHANG).child(cartDomain.getMamonan()).child("Tong").setValue(tonggia);
                    }
                }
            });
            holder.deleteCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                    mydialog.setTitle("Xác nhận");
                    mydialog.setMessage("Xóa " + cartDomain.getTen() + " khỏi giỏ hàng?");
                    mydialog.setIcon(R.drawable.cauhoi);
                    mydialog.setPositiveButton("[XÓA]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("GioHang");
                            String GIOHANG = "GH" + cartDomain.getIduser().split("K")[1];
                            myRef.child(GIOHANG).child(cartDomain.getMamonan()).removeValue();

                            Intent Cart = new Intent(myContex, CartActivity.class);
                            Cart.putExtra("idUser", cartDomain.getIduser());
                            ((CartActivity) myContex).finish();
                            myContex.startActivity(Cart);
                            Toast.makeText(myContex, "Đã xóa " + cartDomain.getTen(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    mydialog.setNegativeButton("[HỦY BỎ]", new DialogInterface.OnClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = mydialog.create();
                    alertDialog.show();
                }
            });
        }

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
