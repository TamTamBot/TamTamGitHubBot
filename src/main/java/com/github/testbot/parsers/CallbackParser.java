package com.github.testbot.parsers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.CallbackAnswer;
import chat.tamtam.botapi.model.MessageCallbackUpdate;
import chat.tamtam.botapi.model.NewMessageBody;
import chat.tamtam.botapi.model.Update;
import com.github.testbot.constans.Callbacks;
import com.github.testbot.interfaces.Parser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CallbackParser implements Parser {


    private TamTamBotAPI bot;

    public CallbackParser(TamTamBotAPI bot) {
        this.bot = bot;
    }

    private void help(String callbackId) throws ClientException, APIException {
        CallbackAnswer answer = new CallbackAnswer().message(new NewMessageBody("hello calback", null, null));
        bot.answerOnCallback(answer, callbackId).execute();
    }

    @Override
    public  void parse(@NotNull Update update) throws APIException, ClientException {
        MessageCallbackUpdate callbackUpdate = (MessageCallbackUpdate) update;
        callbackUpdate.getCallback().getPayload();

        String command = callbackUpdate.getCallback().getPayload();
        String callbackId = callbackUpdate.getCallback().getCallbackId();
        assert command != null;

        switch (Callbacks.valueOf(command.toUpperCase())) {
            case HELP:
                help(callbackId);
            default:

        }

    }
}
