package com.github.testbot.interfaces;

import com.github.testbot.constans.BotCommands;

import static com.github.testbot.constans.BotCommands.*;

public interface BotTexts {
    String HELP_COMMAND_TEXT = "*Available commands*:\n\n" +
            " /" + SET_TOKEN.getCommandName() + " - add GitHub personal access token for subscribing to repositories.\n\n" +
            " /" + SUBSCRIBE.getCommandName() + " - subscribe to GitHub repository updates.\n\n" +
            " /" + UNSUBSCRIBE.getCommandName() + " - unsubscribe for GitHub repository updates.\n\n" +
            " /" + DELETE.getCommandName() + " - delete webhook from GitHub repository.\nAll users who have been " +
            "subscribed to repository will be automatically unsubscribed.\n\n" +
            " /" + LIST.getCommandName() + " - list of repositories you are subscribed to.";
    String MORE_INFO_CALLBACK_BUTTON = "More information.";
    String PROJECT_HOMEPAGE = "https://github.com/TamTamBot/TamTamTest";
    String PROJECT_WIKI = "https://github.com/TamTamBot/TamTamGitHubBot/wiki/Main";
    String HELP_MORE_INFORMATION_TEXT = "*For more information you can visit GitHub homepage of our project and read docs*" +
            ":\n`README`:  "
            + PROJECT_HOMEPAGE + "\n`WIKI`: " + PROJECT_WIKI;
}
