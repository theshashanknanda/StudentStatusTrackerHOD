package com.project.sts_hod.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sts_hod.FacultyProfile;
import com.project.sts_hod.Model.FacultyModel;
import com.project.sts_hod.Model.HODModel;
import com.project.sts_hod.R;

import java.util.ArrayList;
import java.util.List;

public class FacultyRecylerView extends RecyclerView.Adapter<FacultyRecylerView.ViewHolder> {
    public List<FacultyModel> modelList = new ArrayList<>();
    public Context context;

    public FacultyRecylerView(List<FacultyModel> modelList, Context context){
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public FacultyRecylerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyRecylerView.ViewHolder holder, int position) {
        FacultyModel model = new FacultyModel();
        model = modelList.get(position);

        holder.name.setText(model.getName());
        holder.branch.setText("Branch: " + model.getBranch());

        Intent intent = new Intent(context.getApplicationContext(), FacultyProfile.class);
        intent.putExtra("branch", model.getBranch());
        intent.putExtra("name", model.getName());
        intent.putExtra("email", model.getEmail());
        intent.putExtra("password", model.getPassword());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, branch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.facultyName_row_id);
            branch = itemView.findViewById(R.id.branch_row_id);
        }
    }
}
