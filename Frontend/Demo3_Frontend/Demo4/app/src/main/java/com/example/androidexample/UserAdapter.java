package com.example.androidexample;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private JSONArray users;

    public UserAdapter(JSONArray users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        try {
            JSONObject user = users.getJSONObject(position);
            holder.userNameTextView.setText(user.getString("username"));
            // Add more bindings if necessary
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return users.length();
    }

    public void updateData(JSONArray newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            // Initialize other views here if necessary
        }
    }
}
