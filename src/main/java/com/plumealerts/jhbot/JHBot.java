package com.plumealerts.jhbot;

import com.plumealerts.jhbot.command.Command;
import com.plumealerts.jhbot.command.PingCommand;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JHBot {

    private GatewayDiscordClient gateway;

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("ping", new PingCommand());
    }


    public static void main(String[] args) {
        new JHBot().start();
    }

    private void start() {
        DiscordClient client = DiscordClient.create(Constants.TOKEN);
        this.gateway = client.gateway().setEnabledIntents(IntentSet.of(Intent.GUILD_MEMBERS)).login().block();

        this.gateway.getEventDispatcher().on(ReadyEvent.class).subscribe(this::ready);
        this.gateway.getEventDispatcher().on(PresenceUpdateEvent.class).subscribe(this::presenceUpdate);
        this.gateway.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
            final String content = event.getMessage().getContent();
            for (final Map.Entry<String, Command> entry : commands.entrySet()) {
                if (content.startsWith('!' + entry.getKey())) {
                    entry.getValue().execute(event);
                    break;
                }
            }
        });
        this.gateway.onDisconnect().block();
    }

    private void ready(ReadyEvent event) {
        System.out.println("Logged in");
        this.gateway.getGuildMembers(Constants.SERVER_BULLTG)
                .filter(member -> !member.getId().equals(Constants.USER_BULLTG))
                .subscribe(member -> member.getPresence().subscribe(presence -> checkAndUpdatePresence(member, presence)));
    }

    private void presenceUpdate(PresenceUpdateEvent event) {
        if (event.getUserId().equals(Constants.USER_BULLTG)) return;
        event.getMember().subscribe(member -> checkAndUpdatePresence(member, event.getCurrent()));
    }

    public void checkAndUpdatePresence(Member member, Presence presence) {
        if (presence != null) {
            Optional<Activity> optionalActivity = presence.getActivity();
            if (optionalActivity.isPresent()) {
                Activity activity = optionalActivity.get();
                if (activity.getStreamingUrl().isPresent()) {
                    member.addRole(Constants.ROLE_LIVE_STREAMERS).subscribe();
                    return;
                }
            }
        }
        if (member.getRoleIds().contains(Constants.ROLE_LIVE_STREAMERS))
            member.removeRole(Constants.ROLE_LIVE_STREAMERS).subscribe();
    }
}
