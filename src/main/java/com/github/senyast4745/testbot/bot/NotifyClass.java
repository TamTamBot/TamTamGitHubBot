package com.github.senyast4745.testbot.bot;

import chat.tamtam.botapi.TamTamBotAPI;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.models.GitHubCommitCommentEvent;

public abstract class NotifyClass {
    protected TamTamBotAPI bot = TestTamTamBot.getBot();

    public void onCommitComment(GitHubCommitCommentEvent comment){}
}
