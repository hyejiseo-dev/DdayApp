package com.hjproject.daydaypj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>{
    public Activity activity;
    private List<DayInfo> dayList;
    private Realm realm;
    private int changenum;
    String[] categories = {
            "일정", "시험", "운동", "약속", "사랑", "기타"
    };
    public DayAdapter(Activity activity, List<DayInfo> dayList){
        this.activity = activity;
        this.dayList = dayList;
    }
    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mtitle,mdate,mdday,mcategory;
        public ViewHolder(final View itemView) {
            super(itemView);
            mtitle = itemView.findViewById(R.id.mtitleview);
            mdate = itemView.findViewById(R.id.mdateview);
            mdday = itemView.findViewById(R.id.mddayview);
            mcategory = itemView.findViewById(R.id.mcategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Context context = itemView.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", getAdapterPosition());
                    Intent mainIntent = new Intent(context, DayModify.class);
                    mainIntent.putExtras(bundle);
                    mainIntent.putExtra("title",dayList.get(getAdapterPosition()).getTitle());
                    mainIntent.putExtra("date",dayList.get(getAdapterPosition()).getDate());
                    mainIntent.putExtra("dday",dayList.get(getAdapterPosition()).getDday());
                    mainIntent.putExtra("category",dayList.get(getAdapterPosition()).getDcategory());
                    mainIntent.putExtra("changenum",getAdapterPosition());
                    context.startActivity(mainIntent);
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {   //데이터를 담을 공간
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dayitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayInfo data = dayList.get(position);
        holder.mtitle.setText(data.getTitle());
        holder.mdate.setText(data.getDate());
        holder.mdday.setText(data.getDday());
        holder.mcategory.setText(categories[data.getDcategory()]);

    }

    private void removeItemView(int position) {
        dayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dayList.size());
    }
    // 데이터 삭제
    private void removeMemo(String text) {
        realm = Realm.getDefaultInstance();
        final RealmResults<DayInfo> results = realm.where(DayInfo.class).equalTo("title",text).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFromRealm(0);
            }
        });
        realm.close();
    }
}
