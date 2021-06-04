package com.plumealerts.jhbot.command;

import com.plumealerts.jhbot.Constants;
import com.plumealerts.jhbot.db.tables.records.MinecraftWhitelistRecord;
import com.plumealerts.jhbot.minecraft.MinecraftUUID;
import com.plumealerts.jhbot.minecraft.MinecraftUtil;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.plumealerts.jhbot.db.Tables.MINECRAFT_WHITELIST;

public class WhitelistCommand implements Command {
    @Override
    public void execute(MessageCreateEvent event) {
        MessageChannel channel = event.getMessage().getChannel().block();
        if (!channel.getId().equals(Constants.CHANNEL_MINECRAFT)) {
            return;
        }

        Member member = event.getMember().get();
        String content = event.getMessage().getContent();
        final List<String> command = Arrays.asList(content.split(" "));

        if (command.size() < 2) {
            channel.createMessage("You must use !whitelist <add|list>").block();
            return;
        }

        String subcommand = command.get(1);
        if (subcommand.equalsIgnoreCase("list")) {
            try {
                channel.createMessage(Constants.sendRcon("whitelist list")).block();
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
                MinecraftUUID user = MinecraftUtil.getUser(command.get(2));
                if (user == null) {
                    channel.createMessage("Can't find minecraft user").block();
                    return;
                }
                addUser(channel, member.getId().asString(), member.getUsername(), user);
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                channel.createMessage("Failed to add to the whitelist").block();
            }
        }
    }

    public boolean removeOldUser(MessageChannel channel, String discordId, String uuid) throws IOException {
        DSLContext create = DSL.using(Constants.DB_CONNECTION, SQLDialect.SQLITE);
        @Nullable MinecraftWhitelistRecord result = create.selectFrom(MINECRAFT_WHITELIST)
                .where(MINECRAFT_WHITELIST.DISCORD_ID.eq(discordId).or(MINECRAFT_WHITELIST.MINECRAFT_UUID.eq(uuid)))
                .fetchAny();
        if (result != null) {
            if (result.getMinecraftUuid().equalsIgnoreCase(uuid)) {
                return false;
            }

            create.delete(MINECRAFT_WHITELIST)
                    .where(MINECRAFT_WHITELIST.DISCORD_ID.eq(discordId))
                    .execute();
            try {
                Constants.sendRcon("whitelist remove " + result.value1());
            } catch (IOException e) {
                channel.createMessage("Failed to remove user " + result.value1()).block();
                throw e;
            }
        }
        return true;
    }

    public void addUser(MessageChannel channel, String discordId, String discordUsername, MinecraftUUID user) throws IOException {
        if (removeOldUser(channel, discordId, user.getId())) {
            DSLContext create = DSL.using(Constants.DB_CONNECTION, SQLDialect.SQLITE);
            create.insertInto(MINECRAFT_WHITELIST,
                    MINECRAFT_WHITELIST.DISCORD_ID, MINECRAFT_WHITELIST.DISCORD_USERNAME, MINECRAFT_WHITELIST.MINECRAFT_UUID, MINECRAFT_WHITELIST.MINECRAFT_USERNAME)
                    .values(discordId, discordUsername, user.getId(), user.getName())
                    .execute();
            channel.createMessage(Constants.sendRcon("whitelist add " + user.getName())).block();
        } else {
            channel.createMessage("You cannot add an existing minecraft username").block();
        }
    }

}
