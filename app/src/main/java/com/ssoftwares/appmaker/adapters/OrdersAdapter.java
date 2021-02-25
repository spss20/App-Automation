package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.BuilderActivity;
import com.ssoftwares.appmaker.interfaces.OnClickInterface;
import com.ssoftwares.appmaker.modals.Order;

import org.w3c.dom.Text;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context mContext;
    private List<Order> orderList;
    private OnClickInterface onClickInterface;

    public OrdersAdapter(Context mContext, List<Order> orderList , OnClickInterface onClickInterface) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item , parent , false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderName.setText(order.getOrderName());
        if (order.getSubproduct() != null)
            holder.subproductName.setText(order.getSubproduct().getName());
        if (order.getOrderImage() != null)
            Picasso.get().load(order.getOrderImage().getImageUrl()).into(holder.orderImage);

        holder.itemView.setOnClickListener(v -> {
            onClickInterface.onClick(order);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderName;
        TextView subproductName;
        TextView productName;
        ImageView orderImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderName = itemView.findViewById(R.id.order_name);
            subproductName = itemView.findViewById(R.id.subproduct_name);
            productName = itemView.findViewById(R.id.product_name);
            orderImage = itemView.findViewById(R.id.order_image);
        }
    }

    public void updateData(List<Order> orderList){
        this.orderList = orderList;
        notifyDataSetChanged();
    };
}
