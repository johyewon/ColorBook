package com.hanix.colorbook.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanix.colorbook.R;

import java.util.List;

public class PaletteAdapter extends RecyclerView.Adapter<PaletteAdapter.Holder> {

    public interface ItemClick {
        void onClick(View view, int position);
    }

    private ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    Context context;
    List<String> items;

    public PaletteAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void resetItem() {
        items.clear();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        protected View paletteView;
        protected TextView paletteCode;

        public Holder(@NonNull View itemView) {
            super(itemView);
            paletteView = itemView.findViewById(R.id.paletteView);
            paletteCode = itemView.findViewById(R.id.paletteCode);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.palette_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.paletteView.setBackgroundColor((int) Long.parseLong(items.get(i).replaceFirst("0x", ""), 16));
        holder.paletteCode.setText(items.get(i));

        final int num = i;
        holder.paletteCode.setOnClickListener((v) -> {
            if (itemClick != null)
                itemClick.onClick(v, num);
        });

        holder.paletteView.setOnClickListener((v) -> {
            if (itemClick != null)
                itemClick.onClick(v, num);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }
}
