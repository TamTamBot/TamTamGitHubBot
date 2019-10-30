package com.github.testbot.constans;

public enum BotCommands {

    HELP ("help", "Help command"),
    SET_TOKEN ("set_token", "Set token for creating webhook on GitHub"),
    SUBSCRIBE("subscribe", "Subscribe to GitHub repository updates"),
    UNSUBSCRIBE("unsubscribe", "Unsubscribe from GitHub repository updates"),
    LIST("list", "List of your connected GitHub repositories"),
    DELETE("delete", "Delete webhook from GitHub repository"),
    NOT_FOUND ("", "");

    private String commandName;
    private String description;

    BotCommands(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return this.commandName;
    }




}
