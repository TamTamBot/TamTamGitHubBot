package com.github.senyast4745.testbot.enums;

public enum Commands  {

    HELP ("help", "Help command"),
    SAY ("say", "Say hello"),
    REG ("reg", "Reg command")
    ;

    private String commandName;
    private String description;

    Commands(String commandName, String description) {
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
