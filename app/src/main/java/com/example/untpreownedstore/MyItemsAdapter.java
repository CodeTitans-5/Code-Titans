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

public class MyItemsAdapter extends RecyclerView.Adapter<MyItemsAdapter.ViewHolder>{
    private static final String TAG = "ITEMS ADAPTER";
    private ArrayList<Product> productArrayList;
    private OnRecyclerItemClickListener mListener;
    Context context;
    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        mListener = listener;
    }
    public MyItemsAdapter(ArrayList<Product> list,Context context) {
        productArrayList = list;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.myitemscard, parent, false);
        MyItemsAdapter.ViewHolder myViewHolder = new MyItemsAdapter.ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyItemsAdapter.ViewHolder holder, int position) {
        final Product product = productArrayList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$ " +product.getProductPrice()+".00");
        holder.productDescription.setText(product.getProductDescription());
        holder.productCategory.setText(product.getProductCategory());

        Picasso.with(context).load(product.getProductImage()).into(holder.productImage);
        Log.i(TAG, "onBindViewHolder: "+ product.getProductImage());
//        holder.productImage.setImageURI(Uri.parse(product.getProductImage()));
//        Glide.with(context)
//                .load(product.getProductImage())
//                .into(holder.productImage);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " +" Clicked");
                editProduct(product.getProductId(),product.getUserId(), mListener);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product.getProductId(),product.getUserId(),product.getProductCategory(),mListener);
            }
        });
    }

    private void deleteProduct(String productId, String userId,String productCategory, OnRecyclerItemClickListener mListener) {
        if (mListener != null) {

            if (productId != null) {
                Log.i(TAG, "test  " + productId);
                mListener.onRecyclerDeleteClick(productId,userId,productCategory);
            }
        }
    }


    private void editProduct(String productId, String userId, OnRecyclerItemClickListener mListener) {
        if (mListener != null) {

            if (productId != null) {
                Log.i(TAG, "test  " + productId);
                mListener.onRecyclerEditClick(productId,userId);
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
        public TextView productName, productPrice,productDescription,productCategory;
        public ImageView productImage;
        public ImageButton edit,delete;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            productName =itemView.findViewById(R.id.prodName1);
            productPrice = itemView.findViewById(R.id.prodPrice1);
            productImage = itemView.findViewById(R.id.productImg1);
            productDescription = itemView.findViewById(R.id.proddesc);
            productCategory = itemView.findViewById(R.id.prodCat);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
