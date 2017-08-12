package com.example.minhkhai.fuiball.timsan;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhkhai.fuiball.Models.SanBong;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.timsan.chitietsan.ChiTietSan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhkhai on 27/07/17.
 */

public class SanBongAdapter extends RecyclerView.Adapter<SanBongAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<SanBong> data = new ArrayList<>();
    private List<SanBong> dataCopy = new ArrayList<>();

    public SanBongAdapter(Context mContext, List<SanBong> data, List<SanBong> dataCopy) {
        this.mContext = mContext;
        this.data = data;
        this.dataCopy = dataCopy;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_san_bong, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        SanBong sanBong = data.get(position);
        holder.tvTenSan.setText("Sân bóng " + sanBong.getName());
        holder.tvDiaChi.setText(sanBong.getAddress() + ", " +
                                sanBong.getArea() + ", " +
                                sanBong.getCity());

        // bắt sự kiện khi kích vào LinearLayout
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, data.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ChiTietSan.class);
                intent.putExtra("id", data.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSan, tvDiaChi;
        ConstraintLayout item;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvTenSan = (TextView) itemView.findViewById(R.id.tvTenSan);
            tvDiaChi = (TextView) itemView.findViewById(R.id.tvDiaChi);
            item = (ConstraintLayout) itemView.findViewById(R.id.item);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SanBong sb = data.get(getAdapterPosition());
                    Toast.makeText(mContext, sb.getAddress(), Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }

    public void filter(String text) {
        data.clear();
        Log.i("dataCopy", dataCopy.size() + "");
        if(text.isEmpty()){
            data.addAll(dataCopy);
        } else{
            text = text.toLowerCase();
            for(SanBong item: dataCopy){
                if(item.getName().toLowerCase().contains(text)){
                    data.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

}
