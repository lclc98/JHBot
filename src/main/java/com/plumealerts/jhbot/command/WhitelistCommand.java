package com.plumealerts.jhbot.command;

import com.plumealerts.jhbot.Constants;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import nl.vv32.rcon.Rcon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WhitelistCommand implements Command {
    @Override
    public void execute(MessageCreateEvent event) {
        MessageChannel channel = event.getMessage().getChannel().block();
        if (!channel.getId().equals(Constants.CHANNEL_MINECRAFT)) {
            return;
        }
        if (!event.getMember().get().hasHigherRoles(Collections.singleton(Constants.ROLE_MODS)).block() && !event.getMember().get().getId().equals(Constants.USER_LCLC98)) {
            return;
        }

        String content = event.getMessage().getContent();
        final List<String> command = Arrays.asList(content.split(" "));

        if (command.size() < 2) {
            channel.createMessage("You must use !whitelist <add|list>").block();
            return;
        }

        String subcommand = command.get(1);
        if (subcommand.equalsIgnoreCase("list")) {
            try {
                channel.createMessage(WhitelistCommand.sendRcon("whitelist list")).block();
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                channel.createMessage("Failed to list the whitelist").block();
            }
        } else if (subcommand.equalsIgnoreCase("add")) {
            if (command.size() != 3) {
                channel.createMessage("You must use !whitelist add <username>").block();
                return;
            }
            try {
                String username = command.get(2);
                channel.createMessage(WhitelistCommand.sendRcon("whitelist add " + username)).block();
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                channel.createMessage("Failed to add to the whitelist").block();
            }
        }
    }


    private static String sendRcon(String command) throws IOException {
        try (Rcon rcon = Rcon.open(Constants.RCON_IP, Integer.parseInt(Constants.RCON_PORT))) {
            if (rcon.authenticate(Constants.RCON_PASSWORD)) {
                return rcon.sendCommand(command);
            } else {
                throw new RuntimeException("Failed to authenticate");
            }
        }
    }
}
