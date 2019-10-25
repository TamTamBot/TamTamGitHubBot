package com.github.testbot.interfaces;

import com.github.testbot.constans.BotCommands;

import static com.github.testbot.constans.BotCommands.*;

public interface BotTexts {
    String HELP_COMMAND_TEXT = "List of all the commands:\n\r" +
            " /" + SET_TOKEN.getCommandName() + "- add GitHub OAuth token for access and subscribing to repositories.\n\r" +
            " /" + SUBSCRIBE.getCommandName() + " - subscribe to GitHub repository update and news.\n\r" +
            " /" + LIST.getCommandName()+ " - list of repositories you are subscribed to.";
    String MORE_INFO_CALLBACK_BUTTON = "More information.";
    String PROJECT_HOMEPAGE = "https://github.com/TamTamBot/TamTamTest";
    String HELP_MORE_INFORMATION_TEXT = "For more information you can visit GitHub homepage of our project: "
            + PROJECT_HOMEPAGE + " and read README.md.";
}
