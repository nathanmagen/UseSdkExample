package com.example.nmagen.usesdkexample.presenters;


import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.MessagingModule;
import com.MobileTornado.sdk.model.data.Conversation;
import com.MobileTornado.sdk.model.data.Message;
import com.example.nmagen.usesdkexample.data.AppGroup;

/**
 * Created by nmagen on 21/12/2017.
 */

public class MessagePresenter {
    MessagingModule messagingModule = TornadoClient.getInstance().getMessagingModule();
    private Conversation currentConversation;

    public void startConversation(AppGroup appGroup) {
        currentConversation = messagingModule.createConversation(appGroup.getGroup());
    }

    // Returns true if could send the message
    public boolean sendMessage(String message) {
        if (messagingModule.canSendMessageTo(currentConversation)) {
            messagingModule.sendMessage(currentConversation, message);
            return true;
        }
        return false;
    }

    public void addMessagesListener(MessagingModule.MessagesListener messagesListener) {
        messagingModule.addMessagesListener(currentConversation.getId(), messagesListener);
    }

    public void removeMessagesListener(MessagingModule.MessagesListener messagesListener) {
        messagingModule.removeMessagesListener(currentConversation.getId(), messagesListener);
    }

    public void addNewMessageListener(MessagingModule.NewMessageListener newMessageListener) {
        messagingModule.addNewMessageListener(newMessageListener);
    }

    public void removeNewMessagesListener(MessagingModule.NewMessageListener newMessageListener) {
        messagingModule.removeNewMessageListener(newMessageListener);
    }

    public void markMessageAsRead(Message message) {
        messagingModule.markMessageAsRead(message);
    }

    public boolean isCurrentConversation(Conversation conversation) {
        return (conversation.getId() == currentConversation.getId());
    }
}
