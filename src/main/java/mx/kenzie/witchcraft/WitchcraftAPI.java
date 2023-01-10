package mx.kenzie.witchcraft;

import mx.kenzie.witchcraft.entity.Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.LockSupport;

/**
 * This class provides access to the core features from Witchcraft.
 * <p>
 * All fields are guaranteed to be filled by plugin enabling.
 * Fields are guaranteed not-null during regular runtime.
 * <p>
 * Some fields may be disabled or annulled after plugin disabling.
 */
public class WitchcraftAPI {
    
    /**
     * A multithreaded scheduler.
     */
    public static ScheduledExecutorService scheduler;
    /**
     * The off-thread executor - this is not Bukkit task safe!
     */
    public static ExecutorService executor;
    /**
     * Access to loaded spells, casting, etc.
     */
    public static SpellManager spells;
    /**
     * Access to items, materials, common GUIs, useful bits and pieces.
     */
    public static ResourceManager resources;
    /**
     * Access to anything that references Minecraft itself - entities, world, etc.
     */
    public static Minecraft minecraft;
    /**
     * Access to the server <-> client manager - access to packets, client-only blocks, entities, etc .
     */
    public static Client client;
    /**
     * Access to the texture rendering process.
     */
    public static TextureManager textures;
    /**
     * Access to the recipe book system.
     */
    public static RecipeManager recipes;
    /**
     * Access to the plugin instance and some core resources.
     */
    public static IPlugin plugin;
    /**
     * If this is running in test mode: server/minecraft features are unavailable.
     */
    public static boolean isTest;
    
    
    /**
     * This can be used on EXECUTOR threads, e.g. during animations.
     */
    public static void sleep(long millis) {
        LockSupport.parkNanos(millis * 1000000L);
    }
}
