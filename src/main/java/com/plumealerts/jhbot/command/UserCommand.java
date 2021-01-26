package com.plumealerts.jhbot.command;

import com.github.twitch4j.kraken.domain.KrakenUser;
import com.github.twitch4j.kraken.domain.KrakenUserList;
import com.plumealerts.jhbot.JHBot;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserCommand implements Command {
    @Override
    public void execute(MessageCreateEvent event) {
        try {
            if (!event.getMember().get().getId().equals(Snowflake.of(139667487057772545L))) {
                return;
            }
            String content = event.getMessage().getContent();
            final List<String> command = Arrays.asList(content.split(" "));

            Future<KrakenUserList> queue = JHBot.getClient().getKraken().getUsersByLogin(Arrays.asList(command.get(1))).queue();

            List<KrakenUser> users = queue.get().getUsers();
            if (users.isEmpty()) {
                event.getMessage().getChannel().block().createMessage("User not found!").block();
                return;
            }

            KrakenUser user = users.get(0);
            event.getMessage().getChannel().block().createEmbed(embedCreateSpec -> {
                embedCreateSpec.setTitle(user.getDisplayName());
                embedCreateSpec.setThumbnail(user.getLogo());
                embedCreateSpec.addField("ID", user.getId(), true);
                embedCreateSpec.addField("Created At", user.getCreatedAt().toString(), false);
            }).block();
        } catch (ExecutionException e) {
            e.printStackTrace();
            event.getMessage().getChannel().block().createMessage("User not found!").block();
        } catch (InterruptedException e) {
            e.printStackTrace();
            event.getMessage().getChannel().block().createMessage("Failed to get user!").block();
        }
    }
}
