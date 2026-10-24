package com.renyigesai.sip_moment.common.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.joml.Vector3f;

public class WineLiquidParticle extends TextureSheetParticle {

//    public WineLiquidParticle(ClientLevel level, double x, double y, double z, WineLiquidParticleOptions options,TextureAtlasSprite sprite,SpriteSet spriteSet) {
//        super(level, x, y, z, sprite);
//        this.gravity = 0.2F;           // 重力
//        this.friction = 0.9F;          // 阻力
//        this.lifetime = 40;            // 存活 2 秒
//        this.quadSize = 1;
//        this.setSpriteFromAge(spriteSet);
//        Vector3f color = options.getColor();
//        this.rCol = color.x();
//        this.gCol = color.y();
//        this.bCol = color.z();
//    }

    private final SpriteSet sprites;

    public WineLiquidParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, WineLiquidParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.sprites = sprites;
        this.gravity = 0.06F;
        this.setSize(0.01f,0.01f);
        this.friction = 0.9F;
        this.lifetime = 40;
        this.quadSize = 0.1f;
        this.setSpriteFromAge(sprites);
        Vector3f color = options.getColor();
        this.rCol = color.x();
        this.gCol = color.y();
        this.bCol = color.z();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            // 完全锁定水平方向，让粒子只做垂直下落
            this.xd = 0;
            this.zd = 0;

            this.yd = this.yd - this.gravity;
            this.move(this.xd, this.yd, this.zd);

            // 注意：如果需要垂直方向也有空气阻力，保留 yd 的摩擦
            // 但通常垂直下落可以只靠重力，不需要摩擦
            this.yd *= 0.98F;  // 可选：让下落逐渐变慢（模拟空气阻力）
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    @Override
    public net.minecraft.client.particle.ParticleRenderType getRenderType() {
        return net.minecraft.client.particle.ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}