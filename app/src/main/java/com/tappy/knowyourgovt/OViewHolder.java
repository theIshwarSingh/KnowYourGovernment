package com.tappy.knowyourgovt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OViewHolder extends RecyclerView.ViewHolder {

    public TextView offDesign;
    public TextView oName;


    public OViewHolder(View itemView) {
        super(itemView);

        offDesign = (TextView) itemView.findViewById(R.id.offDesign);
         oName = (TextView) itemView.findViewById(R.id.oName);


    }
}
