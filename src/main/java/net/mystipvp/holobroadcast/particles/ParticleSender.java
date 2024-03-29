/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.particles;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

interface ParticleSender {

    void spawnParticle(Object receiver, ParticleType particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data);

    default double color(double color) {
        return color / 255.0;
    }

    class ParticleSenderImpl implements ParticleSender {

        @Override
        public void spawnParticle(Object receiver, ParticleType particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
            Particle bukkitParticle = Particle.valueOf(particle.toString());

            if (data instanceof Color) {
                if (particle.getDataType() == Color.class) {
                    Color color = (Color) data;
                    count = 0;
                    offsetX = color(color.getRed());
                    offsetY = color(color.getGreen());
                    offsetZ = color(color.getBlue());
                    extra = 1.0;
                }
                data = null;
            }

            if (receiver instanceof World) {
                ((World) receiver).spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
            } else if (receiver instanceof Player) {
                ((Player) receiver).spawnParticle(bukkitParticle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
            }
        }

        public boolean isValidDataBukkit(Particle particle, Object data) {
            return particle.getDataType() == Void.class || particle.getDataType().isInstance(data);
        }
    }

    @SuppressWarnings("deprecation")
    class ParticleSender1_13 extends ParticleSenderImpl {

        @Override
        public void spawnParticle(Object receiver, ParticleType particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
            Particle bukkitParticle = Particle.valueOf(particle.toString());

            if (bukkitParticle.getDataType() == Particle.DustOptions.class) {
                if (data instanceof Color) {
                    data = new Particle.DustOptions((Color) data, 1);
                } else if (data == null) {
                    data = new Particle.DustOptions(Color.RED, 1);
                }
            } else if (bukkitParticle.getDataType() == BlockData.class && data instanceof MaterialData) {
                data = Bukkit.createBlockData(((MaterialData) data).getItemType());
            }

            super.spawnParticle(receiver, particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
        }

        @Override
        public boolean isValidDataBukkit(Particle particle, Object data) {
            if (particle.getDataType() == Particle.DustOptions.class && data instanceof Color) {
                return true;
            }

            if (particle.getDataType() == BlockData.class && data instanceof MaterialData) {
                return true;
            }

            return super.isValidDataBukkit(particle, data);
        }
    }
}
