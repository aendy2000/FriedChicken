package com.example.project.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.Activity.AddProductActivity;
import com.example.project.Activity.ProductManagerActivity;
import com.example.project.Domain.ProductManagerDomain;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductManagerAdapter extends RecyclerView.Adapter<ProductManagerAdapter.ProductManagerAdapterViewHolder> {
    ArrayList<ProductManagerDomain> productManagerDomains;
    Context myContex;
    NumberFormat format = new DecimalFormat("#,###");

    public ProductManagerAdapter(Context context, ArrayList<ProductManagerDomain> list) {
        this.myContex = context;
        this.productManagerDomains = list;
    }

    @NonNull
    @Override
    public ProductManagerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_product, parent, false);
        return new ProductManagerAdapter.ProductManagerAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManagerAdapterViewHolder holder, int position) {
        ProductManagerDomain pro = productManagerDomains.get(position);
        holder.name.setText(pro.getTen());
        holder.mota.setText(pro.getMota());
        holder.gia.setText(format.format(Integer.valueOf(pro.getGia())));
        Picasso.with(myContex).load(pro.getHinhanh()).into(holder.image);

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContex, AddProductActivity.class);
                intent.putExtra("MaSanPham", pro.getMasp());
                intent.putExtra("MaDanhMuc", pro.getMadm());
                ((ProductManagerActivity)myContex).finish();
                myContex.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                mydialog.setTitle("Xác nhận");
                mydialog.setMessage("Xóa " + pro.getTen() + "?");
                mydialog.setIcon(R.drawable.cauhoi);
                mydialog.setPositiveButton("[XOÁ]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
                        reference.child(pro.getMasp()).removeValue();
                        Toast.makeText(myContex, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(myContex, ProductManagerActivity.class);
                        intent.putExtra("idData", "SanPham");
                        ((ProductManagerActivity)myContex).finish();
                        myContex.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return productManagerDomains.size();
    }

    public class ProductManagerAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name, gia, mota;
        ImageView image, delete;
        ConstraintLayout Layout;

        public ProductManagerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Food_NameProduct);
            gia = itemView.findViewById(R.id.food_PriceProduct);
            mota = itemView.findViewById(R.id.food_DecriptionProduct);
            image = itemView.findViewById(R.id.img_FoodProduct);
            Layout = itemView.findViewById(R.id.listFoodProduct);
            delete = itemView.findViewById(R.id.img_delete_product_listProduct);
        }
    }
}
