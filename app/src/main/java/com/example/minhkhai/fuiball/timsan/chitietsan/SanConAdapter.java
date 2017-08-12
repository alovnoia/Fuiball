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
import com.example.minhkhai.fuiball.Models.SanCon;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.timsan.SanBongAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhkhai on 31/07/17.
 */

public class SanConAdapter extends RecyclerView.Adapter<SanConAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<SanCon> data = new ArrayList<>();

    public SanConAdapter(Context mContext, List<SanCon> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public SanConAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_1, parent, false);
        return new SanConAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SanConAdapter.RecyclerViewHolder holder, final int position) {
        SanCon sanCon = data.get(position);
        holder.tvTen.setText(sanCon.getName());

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
        TextView tvTen;
        ConstraintLayout item;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvTen = (TextView) itemView.findViewById(R.id.tv1);
            item = (ConstraintLayout) itemView.findViewById(R.id.item_1);

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
