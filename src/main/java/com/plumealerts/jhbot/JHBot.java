package com.plumealerts.jhbot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Snowflake;

import java.util.Optional;

public class JHBot {
    private static final Snowflake USER_BULLTG = Snowflake.of(109142456913674240L);
    private static final Snowflake SERVER_BULLTG = Snowflake.of(431695013555208193L);
    private static final Snowflake ROLE_LIVE_STREAMERS = Snowflake.of(529218769067966465L);
    private DiscordClient client;

    public static void main(String[] args) {
        new JHBot().start();
    }

    private void start() {
        this.client = new DiscordClientBuilder(System.getenv("TOKEN")).build();
        this.client.getEventDispatcher().on(ReadyEvent.class).subscribe(this::ready);
        this.client.getEventDispatcher().on(PresenceUpdateEvent.class).subscribe(this::presenceUpdate);
        this.client.login().block();
    }

    private void ready(ReadyEvent event) {
        System.out.println("Logged in");
        this.client.getGuildById(SERVER_BULLTG)
                .flatMapMany(Guild::getMembers)
                .filter(member -> !member.getId().equals(USER_BULLTG))
                .subscribe(member -> member.getPresence().subscribe(presence -> checkAndUpdatePresence(member, presence)));
    }

    private void presenceUpdate(PresenceUpdateEvent event) {
        if (event.getUserId().equals(USER_BULLTG)) return;
        event.getMember()
                .subscribe(member -> checkAndUpdatePresence(member, event.getCurrent()));
    }

    public void checkAndUpdatePresence(Member member, Presence presence) {
        if (presence != null) {
            Optional<Activity> optionalActivity = presence.getActivity();
            if (optionalActivity.isPresent()) {
                Activity activity = optionalActivity.get();
                if (activity.getStreamingUrl().isPresent()) {
                    member.addRole(ROLE_LIVE_STREAMERS).subscribe();
                    return;
                }
            }
        }
        if (member.getRoleIds().contains(ROLE_LIVE_STREAMERS))
            member.removeRole(ROLE_LIVE_STREAMERS).subscribe();
    }
}
