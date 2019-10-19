package com.github.testbot.constans;

public enum BotCommands {

    HELP ("help", "Help command"),
    SAY ("say", "Say hello"),
    REG ("reg", "Reg command"),
    LIST ("list", "list of your connected GitHub repositories"),
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