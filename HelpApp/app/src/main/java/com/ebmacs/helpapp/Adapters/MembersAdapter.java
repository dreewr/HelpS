package com.ebmacs.helpapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ebmacs.helpapp.Models.UserDetails;
import com.ebmacs.helpapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    private Context context;
    public boolean clicked = false;
    private ArrayList<UserDetails> userrList;
 public static   ArrayList<UserDetails> userrListTemp = new ArrayList<>();


    public MembersAdapter(Context context) {
    }

    public MembersAdapter(Context context, ArrayList<UserDetails> orderList) {
        this.context = context;
        this.userrList = orderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_members, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        UserDetails data = userrList.get(position);

        holder.textView.setText(userrList.get(position).getUserName());
        holder.itemCheckBox.setChecked(userrList.get(position).getSlected());


        holder.itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetails data = userrList.get(holder.getAdapterPosition());
                if (data.getSlected()) {
                    data.setSlected(false);

                } else {
                    data.setSlected(true);
                    userrListTemp.add(data);
                }
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
        if (userrList == null) {
            return 0;
        }
        return userrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        CheckBox itemCheckBox;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.memberName);
            itemCheckBox = itemView.findViewById(R.id.checkMember);
        }
    }
}
