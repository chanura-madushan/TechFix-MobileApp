package com.example.techfix.ui;
import android.view.*; import android.widget.TextView; import androidx.recyclerview.widget.RecyclerView; import com.example.techfix.R; import java.util.List;
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{
 public interface Listener{void onClick(AppointmentDisplay a);} private final List<AppointmentDisplay> list; private final Listener listener;
 public AppointmentAdapter(List<AppointmentDisplay> list,Listener listener){this.list=list;this.listener=listener;}
 public static class ViewHolder extends RecyclerView.ViewHolder{TextView date,branch,status; ViewHolder(View v){super(v);date=v.findViewById(R.id.tvAppointmentDate);branch=v.findViewById(R.id.tvAppointmentBranch);status=v.findViewById(R.id.tvAppointmentStatus);}}
 @Override public ViewHolder onCreateViewHolder(ViewGroup p,int t){return new ViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_appointment,p,false));}
 @Override public void onBindViewHolder(ViewHolder h,int pos){AppointmentDisplay a=list.get(pos);h.date.setText(a.getServiceName()+" — "+a.getDate());h.branch.setText(a.getBranchName());h.status.setText(a.getStatus());h.itemView.setOnClickListener(v->listener.onClick(a));}
 @Override public int getItemCount(){return list.size();}
}