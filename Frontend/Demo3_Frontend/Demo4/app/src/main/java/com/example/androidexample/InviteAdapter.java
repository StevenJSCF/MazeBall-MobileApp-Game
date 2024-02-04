package com.example.androidexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {

    private List<String> usernames;
    private Context context;

    private long userId;

    private List<Long> friendIds; // List to store friend IDs


    public InviteAdapter(Context context, List<String> usernames, List<Long> friendIds) {
        this.context = context;
        this.usernames = usernames;
        this.friendIds = friendIds;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_invite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String username = usernames.get(position);
        holder.textViewUsername.setText(username + " wants to be your friend");

        holder.buttonYes.setOnClickListener(v -> {
            long friendId = friendIds.get(position);
            ((NotificationActivity)context).acceptFriendRequest(friendId);
        });

        holder.buttonNo.setOnClickListener(v -> {
            long friendId = friendIds.get(position);
            ((NotificationActivity) context).declineFriendRequest(friendId);
        });
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        Button buttonYes, buttonNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textView_inviteUser);
            buttonYes = itemView.findViewById(R.id.button_accept);
            buttonNo = itemView.findViewById(R.id.button_reject);
        }
    }




}

