CREATE TABLE minecraft_whitelist
(
    DISCORD_ID         VARCHAR(20) PRIMARY KEY NOT NULL,
    DISCORD_USERNAME   VARCHAR(32)             NOT NULL,
    MINECRAFT_UUID     VARCHAR(36)             NOT NULL,
    MINECRAFT_USERNAME VARCHAR(16)             NOT NULL
)