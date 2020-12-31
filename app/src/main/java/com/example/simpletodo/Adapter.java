package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    public interface OnClick{
        void EditClicked(int position);
    }
    public interface OnLongClick{
        void RemoveClicked(int position);
    }
    List<String> items;
    OnLongClick onLongClick;
    OnClick onClick;

    public Adapter(List<String> items, OnLongClick onLongClick, OnClick onClick) {
        this.items = items;
        this.onLongClick = onLongClick;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //pass view to view holder and return it
        ViewHolder viewHolder = new ViewHolder(todoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get corresponding items from position
        String item = items.get(position);
        //bind items to view holder
        holder.bind(item);
    }

    //determine number of items
    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1); //referencing view we can access here
        }
        //setting contents of item to text view
        public void bind(String item) {
            textView.setText(item);
            //get position of single click
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.EditClicked(getAdapterPosition());
                }
            });

            //get position of long click
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClick.RemoveClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
