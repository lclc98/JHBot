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

    private static Snowflake ROLE_LIVE_STREAMERS = Snowflake.of(529218769067966465L);
    private static Snowflake BULLTG = Snowflake.of(109142456913674240L);

    public static void main(String[] args) {
        new JHBot().start();
    }

    public void start() {
        final DiscordClient client = new DiscordClientBuilder(System.getenv("TOKEN")).build();
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(ready -> {
                    System.out.println("Logged in as " + ready.getSelf().getUsername());
                    Guild guild = client.getGuildById(Snowflake.of(431695013555208193L)).block();
                    if (guild != null) {
                        guild.getMembers().toStream().forEach(member -> {
                            if (member.getId().equals(BULLTG))
                                return;
                            try {
                                Presence presence = member.getPresence().block();
                                if (presence != null && addOrRemoveStreamingRole(presence)) {
                                    member.addRole(ROLE_LIVE_STREAMERS).block();
                                    return;
                                }
                                if (member.getRoleIds().contains(ROLE_LIVE_STREAMERS))
                                    member.removeRole(ROLE_LIVE_STREAMERS).block();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });

        client.getEventDispatcher().on(PresenceUpdateEvent.class)
                .subscribe(presenceUpdateEvent -> {
                    Member member = presenceUpdateEvent.getMember().block();
                    if (member != null && !member.getId().equals(BULLTG)) {
                        if (addOrRemoveStreamingRole(presenceUpdateEvent.getCurrent())) {
                            member.addRole(ROLE_LIVE_STREAMERS).block();
                        } else if (member.getRoleIds().contains(ROLE_LIVE_STREAMERS)) {
                            member.removeRole(ROLE_LIVE_STREAMERS).block();
                        }
                    }
                });

        client.login().block();
    }

    public boolean addOrRemoveStreamingRole(Presence presence) {
        Optional<Activity> a = presence.getActivity();
        if (a.isPresent()) {
            Activity activity = a.get();
            Optional<String> streamingUrl = activity.getStreamingUrl();
            return streamingUrl.isPresent();
        }
        return false;
    }
}
