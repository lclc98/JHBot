package com.plumealerts.jhbot.minecraft;

public class MinecraftUUID {
    private String id;
    private String name;

    public String getId() {
        StringBuffer idBuff = new StringBuffer(this.id);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return idBuff.toString();
    }

    public String getName() {
        return this.name;
    }
}
