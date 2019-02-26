package com.tappy.knowyourgovt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class OAdapter extends RecyclerView.Adapter<OViewHolder> {


    private static final String TAG = "OAdapter";
    private ArrayList<Official> oList;
    private MainActivity mainAct;

    public OAdapter(ArrayList<Official> arList, MainActivity mainActivity) {
        this.oList = arList;
        mainAct = mainActivity;
    }

    @Override
    public OViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview, parent, false);


        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);


        return new OViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(OViewHolder holder, int position) {

        Official person = oList.get(position);


        StringBuffer partyname = new StringBuffer();
        partyname.append(person.getoName());

        if(person.getParty() != null)
        {
            partyname.append("( ");
            partyname.append(person.getParty());
            partyname.append(" )");
        }
        holder.oName.setText(partyname);
        holder.offDesign.setText(person.getoDesig());

    }

    @Override
    public int getItemCount() {

        return oList.size();

    }
}
