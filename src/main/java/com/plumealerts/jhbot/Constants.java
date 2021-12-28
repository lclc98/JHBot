package com.plumealerts.jhbot;

import discord4j.common.util.Snowflake;

public class Constants {

    public static final String TOKEN = System.getenv("TOKEN");

    public static final Snowflake USER_BULLTG = Snowflake.of(109142456913674240L);
    public static final Snowflake SERVER_BULLTG = Snowflake.of(431695013555208193L);
    public static final Snowflake ROLE_LIVE_STREAMERS = Snowflake.of(529218769067966465L);
}
