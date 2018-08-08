package com.example.yperinterntest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//뷰 홀더는 클래스 내에 정의하거나 외부 클래스로 정의해야 한다.
public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.ItemViewHolder>{
    private ArrayList <RecyclerItem> recyclerItems;//데이터
    private OnItemClickListener onItemClickListener;//클릭리스너

    //클릭리스너 인터페이스
    public interface OnItemClickListener{
        //public void onItemClick(View view, int position);
        void onItemClickData(View view, RecyclerItem removedItem);
    }

    //생성자
    public RecyclerAdaptor(ArrayList<RecyclerItem> recyclerItems){
        this.recyclerItems = recyclerItems;
    }

    public ArrayList<RecyclerItem> getRecyclerItems() {
        return recyclerItems;
    }

    //setter
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    @Override
    public RecyclerAdaptor.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new RecyclerAdaptor.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final RecyclerItem item = recyclerItems.get(position);
        holder.timeTv.setText(item.getTime());
        holder.keyTv.setText(""+item.getKey());
        holder.addressTv.setText(item.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    //onItemClickListener.onItemClick(view, position);
                    onItemClickListener.onItemClickData(view, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    //뷰 홀더 정의
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView keyTv, addressTv, timeTv;

        public ItemViewHolder(View itemView){
            super(itemView);

            keyTv=itemView.findViewById(R.id.key_text);
            addressTv=itemView.findViewById(R.id.address_text);
            timeTv=itemView.findViewById(R.id.time_text);
        }
    }
}

