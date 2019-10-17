package com.github.senyast4745.testbot.bot.impl;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.NewMessageBody;
import chat.tamtam.botapi.model.NewMessageLink;
import com.github.senyast4745.testbot.bot.NotifyClass;
import com.github.senyast4745.testbot.models.CanSandedToSubscriber;
import com.github.senyast4745.testbot.models.GitHubCommitCommentEvent;
import com.github.senyast4745.testbot.models.GitHubPushEvent;
import com.github.senyast4745.testbot.models.GitHubRepositoryModel;
import com.github.senyast4745.testbot.repository.UsersRepository;

import java.sql.SQLException;
import java.util.List;

public class NotifyClassImpl extends NotifyClass {
    @Override
    public void onCommitComment(GitHubCommitCommentEvent comment) {
        sendMessageToUsers(comment);
    }

    @Override
    public void onPush(GitHubPushEvent push) {
        sendMessageToUsers(push);
    }

    private void sendMessageToUsers(CanSandedToSubscriber event) {
        NewMessageBody body = new NewMessageBody(event.toString(), null, null);
        try {
            List<Long> ids = UsersRepository.getTamTamUser(event.getRepository().getFullName());
            ids.forEach(id -> {
                try {
                    bot.sendMessage(body).userId(id).execute();
                } catch (APIException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}