package com.example.project.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.project.Activity.AddProductActivity;
import com.example.project.Activity.CartActivity;
import com.example.project.Activity.CheckoutActivity;
import com.example.project.Activity.FoodDetailtActivity;
import com.example.project.Activity.ListOrderActivity;
import com.example.project.Activity.OrderDetailt;
import com.example.project.Activity.OrderDetailtManagerActivity;
import com.example.project.Activity.OrderDetailtOfCategoryManagerActivity;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.CheckoutListDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutlistAdapter extends RecyclerView.Adapter<CheckoutlistAdapter.CheckoutlistViewHolder> {
    ArrayList<CheckoutListDomain> listCheckout;
    Context myContex;

    NumberFormat formatter = new DecimalFormat("#,###");

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
        holder.giaOrder.setText(formatter.format(Integer.valueOf(checkoutlistdomain.getTongcong())));
        holder.trangthaiOrder.setText(checkoutlistdomain.getTrangthai());

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(urlImg, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.imgOrder);
        if (checkoutlistdomain.getIdUser().indexOf("Admin-") != -1) {
            holder.constranOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orderDetailt = new Intent(myContex, OrderDetailtOfCategoryManagerActivity.class);
                    orderDetailt.putExtra("idUser", checkoutlistdomain.getIdUser().split("-")[1]);
                    orderDetailt.putExtra("ngayMuaHang", (checkoutlistdomain.getNgaymua()));
                    myContex.startActivity(orderDetailt);
                }
            });

            if (checkoutlistdomain.getTrangthai().equals("Chờ duyệt")) {
                holder.btnHuy.setText("Duyệt đơn");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Duyệt đơn hàng?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[DUYỆT]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Đã duyệt");

                                Intent listOrder = new Intent(myContex, OrderDetailtManagerActivity.class);
                                listOrder.putExtra("TrangThai", checkoutlistdomain.getTrangthai());
                                ((OrderDetailtManagerActivity) myContex).finish();
                                myContex.startActivity(listOrder);
                                Toast.makeText(myContex, "Đã duyệt đơn hàng ", Toast.LENGTH_LONG).show();
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
            } else if (checkoutlistdomain.getTrangthai().equals("Đã duyệt")) {
                holder.btnHuy.setText("Giao hàng");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Giao đơn hàng?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[GIAO HÀNG]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Đang giao");

                                Intent listOrder = new Intent(myContex, OrderDetailtManagerActivity.class);
                                listOrder.putExtra("TrangThai", checkoutlistdomain.getTrangthai());
                                ((OrderDetailtManagerActivity) myContex).finish();
                                myContex.startActivity(listOrder);
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
            } else if (checkoutlistdomain.getTrangthai().equals("Đang giao")) {
                holder.btnHuy.setText("Không giao được");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Không giao được?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[ĐỒNG Ý]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Không thể giao");

                                Intent listOrder = new Intent(myContex, OrderDetailtManagerActivity.class);
                                listOrder.putExtra("TrangThai", checkoutlistdomain.getTrangthai());
                                ((OrderDetailtManagerActivity) myContex).finish();
                                myContex.startActivity(listOrder);
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
            } else if (checkoutlistdomain.getTrangthai().equals("Không thể giao")) {
                holder.btnHuy.setText("Giao lại đơn");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Giao lại đơn hàng?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[GIAO LẠI]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Đang giao");

                                Intent listOrder = new Intent(myContex, OrderDetailtManagerActivity.class);
                                listOrder.putExtra("TrangThai", checkoutlistdomain.getTrangthai());
                                ((OrderDetailtManagerActivity) myContex).finish();
                                myContex.startActivity(listOrder);
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
            } else {
                holder.btnHuy.setVisibility(View.INVISIBLE);
            }

        } else {
            holder.constranOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orderDetailt = new Intent(myContex, OrderDetailt.class);
                    orderDetailt.putExtra("idUser", checkoutlistdomain.getIdUser());
                    orderDetailt.putExtra("ngayMuaHang", (checkoutlistdomain.getNgaymua()));
                    myContex.startActivity(orderDetailt);
                }
            });

            if (checkoutlistdomain.getTrangthai().equals("Chờ duyệt")) {
                holder.btnHuy.setText("Hủy đơn hàng");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Bạn có muốn hủy đơn hàng?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[HỦY ĐƠN]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Đã hủy");

                                Intent listOrder = new Intent(myContex, ListOrderActivity.class);
                                listOrder.putExtra("idUser", checkoutlistdomain.getIdUser());
                                ((ListOrderActivity) myContex).finish();
                                myContex.startActivity(listOrder);
                                Toast.makeText(myContex, "Đã hủy đơn hàng ", Toast.LENGTH_LONG).show();
                            }
                        });
                        mydialog.setNegativeButton("[KHÔNG HỦY]", new DialogInterface.OnClickListener() {
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
            } else if (checkoutlistdomain.getTrangthai().equals("Đang giao")) {
                holder.btnHuy.setText("Đã nhận hàng");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Đã nhận được hàng?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[ĐỒNG Ý]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.btnHuy.setText("Đã nhận hàng");
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Đã giao");

                                Intent listOrder = new Intent(myContex, ListOrderActivity.class);
                                listOrder.putExtra("idUser", checkoutlistdomain.getIdUser());
                                ((ListOrderActivity) myContex).finish();
                                myContex.startActivity(listOrder);
                                Toast.makeText(myContex, "Cảm ơn bạn đã tin tưởng và đặt hàng!", Toast.LENGTH_LONG).show();
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
            } else if (checkoutlistdomain.getTrangthai().equals("Đã hủy")) {
                holder.btnHuy.setText("Đặt lại");
                holder.btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(myContex);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Đặt lại đơn hàng?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[ĐỒNG Ý]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.btnHuy.setText("Đã nhận hàng");
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("DonHang");
                                reference.child("DH" + checkoutlistdomain.getIdUser().split("K")[1]).child(checkoutlistdomain.getNgaymua()).child("TrangThai").child("Trangthai").setValue("Chờ duyệt");

                                Intent listOrder = new Intent(myContex, ListOrderActivity.class);
                                listOrder.putExtra("idUser", checkoutlistdomain.getIdUser());
                                ((ListOrderActivity) myContex).finish();
                                myContex.startActivity(listOrder);
                                Toast.makeText(myContex, "Cảm ơn bạn đã tin tưởng và đặt hàng!", Toast.LENGTH_LONG).show();
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
            } else {
                holder.btnHuy.setVisibility(View.INVISIBLE);
            }
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
