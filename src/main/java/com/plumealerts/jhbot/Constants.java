package com.plumealerts.jhbot;

import discord4j.common.util.Snowflake;

public class Constants {

    public static final String RCON_IP = System.getenv("RCON_IP");
    public static final String RCON_PORT = System.getenv("RCON_PORT");
    public static final String RCON_PASSWORD = System.getenv("RCON_PASSWORD");
    public static final String TOKEN = System.getenv("TOKEN");

    public static final Snowflake USER_LCLC98 = Snowflake.of(139667487057772545L);
    public static final Snowflake USER_BULLTG = Snowflake.of(109142456913674240L);
    public static final Snowflake CHANNEL_MINECRAFT = Snowflake.of(829037727864717362L);
    public static final Snowflake SERVER_BULLTG = Snowflake.of(431695013555208193L);
    public static final Snowflake ROLE_LIVE_STREAMERS = Snowflake.of(529218769067966465L);

    public static final Snowflake ROLE_MODS = Snowflake.of(431930878495686657L);

}
