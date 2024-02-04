package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * To identy fy the message that is send or received if is send it will use the different type of box
     * @param position position to query
     * @return
     */

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.isCurrentUser()) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    /**
     * The different view of the box that send and received
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else { // VIEW_TYPE_MESSAGE_RECEIVED
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    /**
     *      hold the message that send and received
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    /**
     * Return the size of the message
     * @return
     */
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     *   ViewHolder for sent messages
     */
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message_body_sent); // ID for the TextView in item_message_sent.xml
        }

        void bind(Message message) {
            messageTextView.setText(message.getContent());
            // Any additional binding logic here
        }
    }

    /**
     * ViewHolder for received messages
     */
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message_body_received); // ID for the TextView in item_message_received.xml
        }

        void bind(Message message) {
            messageTextView.setText(message.getContent());
            // Any additional binding logic here
        }
    }
}
