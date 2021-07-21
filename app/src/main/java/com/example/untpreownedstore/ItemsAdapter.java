package com.example.untpreownedstore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{
    private static final String TAG = "ITEMS ADAPTER";
    private ArrayList<Product> productArrayList;
    private OnRecyclerItemClickListener mListener;
    Context context;
    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        mListener = listener;
    }
    public ItemsAdapter(ArrayList<Product> list,Context context) {
        productArrayList = list;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemscard, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemsAdapter.ViewHolder holder, int position) {
        final Product product = productArrayList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$ " +product.getProductPrice()+".00");

        Picasso.with(context).load(product.getProductImage()).into(holder.productImage);
        Log.i(TAG, "onBindViewHolder: "+ product.getProductImage());
//        holder.productImage.setImageURI(Uri.parse(product.getProductImage()));
//        Glide.with(context)
//                .load(product.getProductImage())
//                .into(holder.productImage);
        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProduct(product.getProductId(),product.getProductCategory(), mListener);
            }
        });
    }

    private void viewProduct(String productId, String productCategory, OnRecyclerItemClickListener mListener) {
        if (mListener != null) {

            if (productId != null) {
                Log.i(TAG, "test  " + productId);
                mListener.onRecyclerClick(productId,productCategory);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (productArrayList == null) {
            return 0;
        } else {
            return productArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice;
        public ImageView productImage;
        public ImageButton go;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            productName =itemView.findViewById(R.id.prodName);
            productPrice = itemView.findViewById(R.id.prodPrice);
            productImage = itemView.findViewById(R.id.productImg);
            go = itemView.findViewById(R.id.go);
        }
    }
}
