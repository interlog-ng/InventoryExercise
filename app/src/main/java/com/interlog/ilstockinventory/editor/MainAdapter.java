package com.interlog.ilstockinventory.editor;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.interlog.ilstockinventory.LoginActivity;
import com.interlog.ilstockinventory.R;
import com.interlog.ilstockinventory.SharedPrefManager;
import com.interlog.ilstockinventory.User;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecyclerViewAdapter> {

    private Context context;
    private List<Note> notes;
    private ItemClickListener itemClickListener;


    public MainAdapter(Context context, List<Note> notes, ItemClickListener itemClickListener) {
        this.context = context;
        this.notes = notes;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note,
                parent, false);
        return new RecyclerViewAdapter(view, itemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Note note = notes.get(position);
        holder.tv_custNm.setText(note.getCustomerName());
        holder.tv_prodNm.setText(note.getProductName());
        holder.tv_locAd.setText(note.getLocationAddr());
        holder.tv_locAd.setText(note.getLocationAddr());
        holder.tv_rDate.setText(note.getReportingDate());
        //holder.card_item.setCardBackgroundColor(note.getColor());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_custNm, tv_prodNm, tv_locAd, tv_rDate;
        CardView card_item;
        ItemClickListener itemClickListener;

        RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener) {
            super(itemView);

           /** TextView textViewId = itemView.findViewById(R.id.textViewId);
            TextView textViewUsername = itemView.findViewById(R.id.textViewUsername);
            TextView textViewEmail = itemView.findViewById(R.id.textViewEmail);
            TextView textViewGender =  itemView.findViewById(R.id.textViewGender);
            //getting the current user
            User user = SharedPrefManager.getInstance(MainAdapter.RecyclerViewAdapter, context).getUser();

            //setting the values to the textviews
            textViewId.setText(String.valueOf(user.getId()));
            textViewUsername.setText(user.getUsername());
            textViewEmail.setText(user.getEmail());
            textViewGender.setText(user.getGender()); **/

            tv_custNm = itemView.findViewById(R.id.custNm);
            tv_prodNm = itemView.findViewById(R.id.prodNm);
            tv_locAd = itemView.findViewById(R.id.locAd);
            tv_rDate = itemView.findViewById(R.id.rDate);
            card_item = itemView.findViewById(R.id.card_item);

            this.itemClickListener = itemClickListener;
            card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}