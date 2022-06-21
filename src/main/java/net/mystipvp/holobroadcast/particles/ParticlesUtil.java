/*
 * Copyright (c) 2020-2022.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.particles;

import net.mystipvp.holobroadcast.nms.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class ParticlesUtil {

    private static final ParticleSender PARTICLE_SENDER;

    static {
        if (ReflectionUtil.optionalClass("org.bukkit.Particle$DustOptions").isPresent()) {
            PARTICLE_SENDER = new ParticleSender.ParticleSender1_13();
        } else if (ReflectionUtil.optionalClass("org.bukkit.Particle").isPresent()) {
            PARTICLE_SENDER = new ParticleSender.ParticleSenderImpl();
        } else {
            PARTICLE_SENDER = new ParticleSenderLegacy();
        }
    }

    private ParticlesUtil() {
        throw new UnsupportedOperationException();
    }

    /*
     * Worlds methods
     */
    public static void spawnParticle(World world, ParticleType particle, Location location, int count) {
        spawnParticle(world, particle, location.getX(), location.getY(), location.getZ(), count);
    }

    public static void spawnParticle(World world, ParticleType particle, double x, double y, double z, int count) {
        spawnParticle(world, particle, x, y, z, count, null);
    }

    public static <T> void spawnParticle(World world, ParticleType particle, Location location, int count, T data) {
        spawnParticle(world, particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    public static <T> void spawnParticle(World world, ParticleType particle, double x, double y, double z, int count,
                                         T data) {
        spawnParticle(world, particle, x, y, z, count, 0.0D, 0.0D, 0.0D, data);
    }

    public static void spawnParticle(World world, ParticleType particle, Location location, int count, double offsetX,
                                     double offsetY, double offsetZ) {
        spawnParticle(world, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    public static void spawnParticle(World world, ParticleType particle, double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        spawnParticle(world, particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    public static <T> void spawnParticle(World world, ParticleType particle, Location location, int count,
                                         double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(world, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY,
                offsetZ, data);
    }

    public static <T> void spawnParticle(World world, ParticleType particle, double x, double y, double z, int count,
                                         double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(world, particle, x, y, z, count, offsetX, offsetY, offsetZ, 1.0D, data);
    }

    public static void spawnParticle(World world, ParticleType particle, Location location, int count, double offsetX,
                                     double offsetY, double offsetZ, double extra) {
        spawnParticle(world, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    public static void spawnParticle(World world, ParticleType particle, double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(world, particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    public static <T> void spawnParticle(World world, ParticleType particle, Location location, int count,
                                         double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(world, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    public static <T> void spawnParticle(World world, ParticleType particle, double x, double y, double z, int count,
                                         double offsetX, double offsetY, double offsetZ, double extra, T data) {
        sendParticle(world, particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    /*
     * Player methods
     */
    public static void spawnParticle(Player player, ParticleType particle, Location location, int count) {
        spawnParticle(player, particle, location.getX(), location.getY(), location.getZ(), count);
    }

    public static void spawnParticle(Player player, ParticleType particle, double x, double y, double z, int count) {
        spawnParticle(player, particle, x, y, z, count, null);
    }

    public static <T> void spawnParticle(Player player, ParticleType particle, Location location, int count, T data) {
        spawnParticle(player, particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    public static <T> void spawnParticle(Player player, ParticleType particle, double x, double y, double z, int count,
                                         T data) {
        spawnParticle(player, particle, x, y, z, count, 0.0D, 0.0D, 0.0D, data);
    }

    public static void spawnParticle(Player player, ParticleType particle, Location location, int count, double offsetX,
                                     double offsetY, double offsetZ) {
        spawnParticle(player, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    public static void spawnParticle(Player player, ParticleType particle, double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        spawnParticle(player, particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    public static <T> void spawnParticle(Player player, ParticleType particle, Location location, int count,
                                         double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(player, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    public static <T> void spawnParticle(Player player, ParticleType particle, double x, double y, double z, int count,
                                         double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(player, particle, x, y, z, count, offsetX, offsetY, offsetZ, 1.0D, data);
    }

    public static void spawnParticle(Player player, ParticleType particle, Location location, int count, double offsetX,
                                     double offsetY, double offsetZ, double extra) {
        spawnParticle(player, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    public static void spawnParticle(Player player, ParticleType particle, double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(player, particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    public static <T> void spawnParticle(Player player, ParticleType particle, Location location, int count,
                                         double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(player, particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    public static <T> void spawnParticle(Player player, ParticleType particle, double x, double y, double z, int count,
                                         double offsetX, double offsetY, double offsetZ, double extra, T data) {
        sendParticle(player, particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    private static void sendParticle(Object receiver, ParticleType particle, double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ, double extra, Object data) {
        if (!particle.isSupported()) {
            throw new IllegalArgumentException("The particle '" + particle + "' is not compatible with your server version");
        }

        PARTICLE_SENDER.spawnParticle(receiver, particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }
}
