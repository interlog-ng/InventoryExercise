package com.interlog.ilstockinventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private Context mCtx;
    private List<Product> productList;

    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    //LayoutInflater inflater = LayoutInflater.from(mCtx);
    //View view = inflater.inflate(R.layout.product_list, null);

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textViewCN.setText(product.getCustomerName());
        holder.textViewLA.setText(product.getLocationAddr());
        holder.textViewPN.setText(product.getProductName());
        holder.textViewRD.setText(product.getReportingDate());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCN, textViewLA, textViewPN, textViewRD;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewCN = itemView.findViewById(R.id.textViewCN);
            textViewLA = itemView.findViewById(R.id.textViewLA);
            textViewPN = itemView.findViewById(R.id.textViewPN);
            textViewRD = itemView.findViewById(R.id.textViewRD);
        }
    }
}
