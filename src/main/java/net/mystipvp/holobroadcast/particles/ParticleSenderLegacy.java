/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.particles;

import net.mystipvp.holobroadcast.nms.ReflectionUtil;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ParticleSenderLegacy implements ParticleSender {

    private static final boolean SERVER_IS_1_8;

    private static final Constructor<?> PACKET_PARTICLE;
    private static final Class<?> ENUM_PARTICLE;

    private static final Method WORLD_GET_HANDLE;
    private static final Method WORLD_SEND_PARTICLE;

    private static final Method PLAYER_GET_HANDLE;
    private static final Field PLAYER_CONNECTION;
    private static final Method SEND_PACKET;
    private static final int[] EMPTY = new int[0];

    static {
        ENUM_PARTICLE = ReflectionUtil.getNMSOptionalClass("EnumParticle").orElse(null);
        SERVER_IS_1_8 = ENUM_PARTICLE != null;

        try {
            Class<?> packetParticleClass = ReflectionUtil.getNMSClass("PacketPlayOutWorldParticles", "network.protocol.game");
            Class<?> playerClass = ReflectionUtil.getNMSClass("EntityPlayer", "server.level");
            Class<?> playerConnectionClass = ReflectionUtil.getNMSClass("PlayerConnection", "server.network");
            Class<?> worldClass = ReflectionUtil.getNMSClass("WorldServer", "server.level");
            Class<?> entityPlayerClass = ReflectionUtil.getNMSClass("EntityPlayer", "server.level");

            Class<?> craftPlayerClass = ReflectionUtil.getOBCClass("entity.CraftPlayer");
            Class<?> craftWorldClass = ReflectionUtil.getOBCClass("CraftWorld");

            if (SERVER_IS_1_8) {
                PACKET_PARTICLE = packetParticleClass.getConstructor(ENUM_PARTICLE, boolean.class, float.class,
                        float.class, float.class, float.class, float.class, float.class, float.class, int.class,
                        int[].class);
                WORLD_SEND_PARTICLE = worldClass.getDeclaredMethod("sendParticles", entityPlayerClass, ENUM_PARTICLE,
                        boolean.class, double.class, double.class, double.class, int.class, double.class, double.class,
                        double.class, double.class, int[].class);
            } else {
                PACKET_PARTICLE = packetParticleClass.getConstructor(String.class, float.class, float.class, float.class,
                        float.class, float.class, float.class, float.class, int.class);
                WORLD_SEND_PARTICLE = worldClass.getDeclaredMethod("a", String.class, double.class, double.class,
                        double.class, int.class, double.class, double.class, double.class, double.class);
            }

            WORLD_GET_HANDLE = craftWorldClass.getDeclaredMethod("getHandle");
            PLAYER_GET_HANDLE = craftPlayerClass.getDeclaredMethod("getHandle");
            PLAYER_CONNECTION = playerClass.getField("playerConnection");
            SEND_PACKET = playerConnectionClass.getMethod("sendPacket", ReflectionUtil.getNMSClass("Packet", "network.protocol"));
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public void spawnParticle(Object receiver, ParticleType particle, double x, double y, double z, int count, double offsetX, double offsetY,
                              double offsetZ, double extra, Object data) {
        try {
            int[] datas = toData(particle, data);

            if (data instanceof Color) {
                if (particle.getDataType() == Color.class) {
                    Color color = (Color) data;
                    count = 0;
                    offsetX = color(color.getRed());
                    offsetY = color(color.getGreen());
                    offsetZ = color(color.getBlue());
                    extra = 1.0;
                }
            }

            if (receiver instanceof World) {
                Object worldServer = WORLD_GET_HANDLE.invoke(receiver);

                if (SERVER_IS_1_8) {
                    WORLD_SEND_PARTICLE.invoke(worldServer, null, getEnumParticle(particle), true, x, y, z, count, offsetX, offsetY, offsetZ, extra, datas);
                } else {
                    String particleName = particle.getLegacyName() + (datas.length != 2 ? "" : "_" + datas[0] + "_" + datas[1]);
                    WORLD_SEND_PARTICLE.invoke(worldServer, particleName, x, y, z, count, offsetX, offsetY, offsetZ, extra);
                }
            } else if (receiver instanceof Player) {
                Object packet;

                if (SERVER_IS_1_8) {
                    packet = PACKET_PARTICLE.newInstance(getEnumParticle(particle), true, (float) x, (float) y,
                            (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count, datas);
                } else {
                    String particleName = particle.getLegacyName() + (datas.length != 2 ? "" : "_" + datas[0] + "_" + datas[1]);
                    packet = PACKET_PARTICLE.newInstance(particleName, (float) x, (float) y, (float) z,
                            (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count);
                }

                Object entityPlayer = PLAYER_GET_HANDLE.invoke(receiver);
                Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
                SEND_PACKET.invoke(playerConnection, packet);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidData(Object particle, Object data) {
        return true;
    }

    public Object getParticle(ParticleType particle) {
        if (!SERVER_IS_1_8) {
            return particle.getLegacyName();
        }

        try {
            return getEnumParticle(particle);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Object getEnumParticle(ParticleType particleType) {
        return ReflectionUtil.enumValueOf(ENUM_PARTICLE, particleType.toString());
    }

    @SuppressWarnings("deprecation")
    private int[] toData(ParticleType particle, Object data) {
        Class<?> dataType = particle.getDataType();
        if (dataType == ItemStack.class) {
            if (!(data instanceof ItemStack itemStack)) {
                return SERVER_IS_1_8 ? new int[2] : new int[]{1, 0};
            }

            return new int[]{itemStack.getType().getId(), itemStack.getDurability()};
        }

        if (dataType == MaterialData.class) {
            if (!(data instanceof MaterialData materialData)) {
                return SERVER_IS_1_8 ? new int[1] : new int[]{1, 0};
            }

            if (SERVER_IS_1_8) {
                return new int[]{materialData.getItemType().getId() + (materialData.getData() << 12)};
            } else {
                return new int[]{materialData.getItemType().getId(), materialData.getData()};
            }
        }

        return EMPTY;
    }
}
