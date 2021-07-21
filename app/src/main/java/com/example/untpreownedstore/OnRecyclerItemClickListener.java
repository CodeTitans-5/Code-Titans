package com.example.untpreownedstore;

public interface OnRecyclerItemClickListener {
    void onRecyclerClick(String documentId,String productCategory);
    void onRecyclerEditClick(String productId, String userId);
    void onRecyclerDeleteClick(String productId, String userId,String Category);
}
