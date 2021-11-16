package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.CartActivity;
import com.example.project.Activity.CheckoutActivity;
import com.example.project.Activity.FoodDetailtActivity;
import com.example.project.Activity.ListOrderActivity;
import com.example.project.Activity.OrderDetailt;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.CheckoutListDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutlistAdapter extends RecyclerView.Adapter<CheckoutlistAdapter.CheckoutlistViewHolder> {
    ArrayList<CheckoutListDomain> listCheckout;
    Context myContex;
    int sum = 0;
    String luuDanhmuc = "", Result = "";

    public CheckoutlistAdapter(Context context, ArrayList<CheckoutListDomain> listCheckout) {
        this.listCheckout = listCheckout;
        this.myContex = context;
    }

    @NonNull
    @Override
    public CheckoutlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_order, parent, false);
        return new CheckoutlistAdapter.CheckoutlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutlistViewHolder holder, int position) {
        CheckoutListDomain checkoutlistdomain = listCheckout.get(position);
        if (checkoutlistdomain == null)
            return;
        String urlImg = "order";
        holder.ngayOrder.setText(checkoutlistdomain.getNgaymua().split("")[5] + checkoutlistdomain.getNgaymua().split("")[6] + "/"
                + checkoutlistdomain.getNgaymua().split("")[3] + checkoutlistdomain.getNgaymua().split("")[4] + "/"
                + checkoutlistdomain.getNgaymua().split("")[1] + checkoutlistdomain.getNgaymua().split("")[2] + " "
                + checkoutlistdomain.getNgaymua().split("-")[1].substring(0, 5));
        holder.soluongOrder.setText(checkoutlistdomain.getSomon());
        holder.giaOrder.setText(checkoutlistdomain.getTongcong());
        holder.trangthaiOrder.setText(checkoutlistdomain.getTrangthai());
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(urlImg, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.imgOrder);
        holder.constranOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase databaseorderdetailt = FirebaseDatabase.getInstance();
                DatabaseReference referenceorderdetait = databaseorderdetailt.getReference("DonHang");
                referenceorderdetait.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child("|" + checkoutlistdomain.getNgaymua()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {

                        } else {
                            String key = snapshot.getValue().toString();
                            String[] tach = key.split("[}}],");
                            String maMonAn = "";
                            for (int i = 0; i < tach.length; i++) {
                                if (tach[i].indexOf("MG") != -1) {
                                    maMonAn += "MonGa;" + tach[i].substring(tach[i].indexOf("MG"), tach[i].indexOf("MG") + 12) + "=";
                                } else if (tach[i].indexOf("CB") != -1) {
                                    maMonAn += "ComBo;" + tach[i].substring(tach[i].indexOf("CB"), tach[i].indexOf("CB") + 12) + "=";
                                } else if (tach[i].indexOf("AV") != -1) {
                                    maMonAn += "AnVat;" + tach[i].substring(tach[i].indexOf("AV"), tach[i].indexOf("AV") + 12) + "=";
                                }
                            }
                            Intent orderDetailt = new Intent(myContex, OrderDetailt.class);
                            orderDetailt.putExtra("idUser", checkoutlistdomain.getIdUser());
                            orderDetailt.putExtra("ResultMaMonOrder", maMonAn);
                            orderDetailt.putExtra("ngayMuaHang", ("|" + checkoutlistdomain.getNgaymua()));
                            myContex.startActivity(orderDetailt);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
        if (checkoutlistdomain.getTrangthai().equals("Chờ duyệt")) {
            holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("DonHang");
                    reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child("|" + checkoutlistdomain.getNgaymua()).child("TrangThai").setValue("Đã hủy");

                    Intent listOrder = new Intent(myContex, ListOrderActivity.class);
                    listOrder.putExtra("idUser", checkoutlistdomain.getIdUser());
                    ((ListOrderActivity) myContex).finish();
                    myContex.startActivity(listOrder);
                    Toast.makeText(myContex, "Đã hủy đơn hàng ", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            holder.btnHuy.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (listCheckout != null)
            return listCheckout.size();
        return 0;
    }

    public class CheckoutlistViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgOrder;
        private TextView ngayOrder, giaOrder, soluongOrder, trangthaiOrder, resulOrder;
        private ConstraintLayout constranOrder;
        private Button btnHuy;

        public CheckoutlistViewHolder(@NonNull View itemView) {
            super(itemView);

            imgOrder = itemView.findViewById(R.id.img_Food_listorder);
            ngayOrder = itemView.findViewById(R.id.Food_date_listorder);
            soluongOrder = itemView.findViewById(R.id.food_somon_listorder);
            giaOrder = itemView.findViewById(R.id.food_SumPrice_listorder);
            trangthaiOrder = itemView.findViewById(R.id.food_trangthai_listorder);
            constranOrder = itemView.findViewById(R.id.listFoodOrder);
            btnHuy = itemView.findViewById(R.id.btn_huydon_order);
            resulOrder = itemView.findViewById(R.id.tv_resultOrder);
        }
    }
}
