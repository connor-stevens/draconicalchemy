package com.cleanroommc.draconicalchemy.util;


import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.cleanroommc.draconicalchemy.alchemy.BlastWave;
import net.minecraft.util.EnumFacing;

//This would be in Mixin, but it apparently can't be?
//As in the game will crash if you move it there.
public interface IAimable {

    void directBlast(EnumFacing facing, float accuracy);


    void directBlast(float angle, float accuracy);

    void setBlastWave(BlastWave blastWave);
}
