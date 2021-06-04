package com.plumealerts.jhbot.minecraft;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class MinecraftUtil {
    public static MinecraftUUID getUser(String username) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new URL("https://api.mojang.com/users/profiles/minecraft/" + username), MinecraftUUID.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
