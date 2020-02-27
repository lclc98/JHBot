package com.plumealerts.jhbot.command;

import discord4j.core.event.domain.message.MessageCreateEvent;

public class PingCommand implements Command {
    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage().getChannel().block().createMessage("Pong!").block();
    }
}
