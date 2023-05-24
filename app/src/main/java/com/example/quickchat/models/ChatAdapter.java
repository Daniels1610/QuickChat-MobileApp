package com.example.quickchat.models;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.databinding.ItemcontainerReceivedmessageBinding;
import com.example.quickchat.databinding.ItemcontainerSentmessageBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final String senderID;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderID) {
        this.chatMessages = chatMessages;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemcontainerSentmessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceiveMessageViewHolder(
                    ItemcontainerReceivedmessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData((chatMessages.get(position)));
        } else {
            ((ReceiveMessageViewHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderID.equals(senderID)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemcontainerSentmessageBinding binding;

        SentMessageViewHolder(ItemcontainerSentmessageBinding itemcontainerSentmessageBinding) {
            super(itemcontainerSentmessageBinding.getRoot());
            binding = itemcontainerSentmessageBinding;

        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemcontainerReceivedmessageBinding binding;

        ReceiveMessageViewHolder(ItemcontainerReceivedmessageBinding itemcontainerReceivedmessageBinding) {
            super(itemcontainerReceivedmessageBinding.getRoot());
            binding = itemcontainerReceivedmessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);

        }
    }
}
