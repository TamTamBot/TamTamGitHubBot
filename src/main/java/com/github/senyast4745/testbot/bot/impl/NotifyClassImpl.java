package com.github.senyast4745.testbot.bot.impl;

import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.NewMessageBody;
import com.github.senyast4745.testbot.bot.NotifyClass;
import com.github.senyast4745.testbot.models.GitHubCommitCommentEvent;
import com.github.senyast4745.testbot.repos.GitHubUser;

import java.sql.SQLException;
import java.util.List;

public class NotifyClassImpl extends NotifyClass {
    @Override
    public void onCommitComment(GitHubCommitCommentEvent comment) {
        NewMessageBody body = new NewMessageBody(comment.toString(), null, null);
        try {
            List<Long> ids = GitHubUser.getTamTamUser(
                    comment.getComment().getUser().getLogin());
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
