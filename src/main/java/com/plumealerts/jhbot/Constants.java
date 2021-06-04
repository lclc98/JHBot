package com.plumealerts.jhbot;

import discord4j.common.util.Snowflake;
import nl.vv32.rcon.Rcon;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

    public static final Snowflake ROLE_COZY_CREW = Snowflake.of(443318246125338635L);

    public static final Connection DB_CONNECTION = openDB();

    public static String sendRcon(String command) throws IOException {
        try (Rcon rcon = Rcon.open(Constants.RCON_IP, Integer.parseInt(Constants.RCON_PORT))) {
            if (rcon.authenticate(Constants.RCON_PASSWORD)) {
                return rcon.sendCommand(command);
            } else {
                throw new RuntimeException("Failed to authenticate");
            }
        }
    }

    public static Connection openDB() {
        try {
            FluentConfiguration configuration = Flyway.configure().dataSource("jdbc:sqlite:jhbot.sqlite", "", "");
            Flyway flyway = configuration.load();
            flyway.migrate();
            return configuration.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }
}
