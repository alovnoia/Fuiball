package com.example.minhkhai.fuiball.timsan.chitietsan;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.minhkhai.fuiball.Models.BangGia;
import com.example.minhkhai.fuiball.Models.DichVu;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.timsan.SanBongAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhkhai on 29/07/17.
 */

public class GiaSanAdapter extends RecyclerView.Adapter<GiaSanAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<BangGia> data = new ArrayList<>();

    public GiaSanAdapter(Context mContext, List<BangGia> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public GiaSanAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_dich_vu, parent, false);
        return new GiaSanAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GiaSanAdapter.RecyclerViewHolder holder, final int position) {
        BangGia bangGia = data.get(position);
        holder.tvGio.setText(bangGia.getBatDau() + " - " + bangGia.getKetThuc());
        switch (bangGia.getTrangThai()) {
            case "pending":
                holder.tvGia.setText("Đợi duyệt");
                break;
            case "ok":
                holder.tvGia.setText("Chấp nhận");
                break;
        }


        // bắt sự kiện khi kích vào LinearLayout
        /*holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, data.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ChiTietSan.class);
                //intent.putExtra("id", data.get(position).getId());
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvGio, tvGia;
        RelativeLayout item;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvGio = (TextView) itemView.findViewById(R.id.tvTenDichVu);
            tvGia = (TextView) itemView.findViewById(R.id.tvGiaDichVu);
            item = (RelativeLayout) itemView.findViewById(R.id.item_dich_vu);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SanBong sb = data.get(getAdapterPosition());
                    Toast.makeText(mContext, sb.getAddress(), Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private SanBongAdapter.OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(SanBongAdapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

}
