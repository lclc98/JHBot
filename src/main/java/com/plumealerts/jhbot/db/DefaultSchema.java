/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.jhbot.db;


import com.plumealerts.jhbot.db.tables.MinecraftWhitelist;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>minecraft_whitelist</code>.
     */
    public final MinecraftWhitelist MINECRAFT_WHITELIST = MinecraftWhitelist.MINECRAFT_WHITELIST;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            MinecraftWhitelist.MINECRAFT_WHITELIST);
    }
}
