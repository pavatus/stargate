package dev.pavatus.stargate.core.dhd;

import dev.pavatus.stargate.core.block.DHDBlock;
import dev.pavatus.stargate.core.dhd.control.SymbolControl;
import dev.pavatus.stargate.core.entities.DHDControlEntity;
import org.joml.Vector3f;

import net.minecraft.entity.EntityDimensions;

/**
 * Holds a control which will be run when interacted with, an
 * {@linkplain Vector3f offset} from the centre of the {@link DHDBlock} and
 * a {@linkplain EntityDimensions scale} for the entity <br>
 * <br>
 * A list of these is gotten by {@link DHDControlEntity#getSymbolArrangement()} and
 * used in {@link DHDControlEntity} to hold its information
 *
 * @author loqor
 * @see DHDControlEntity
 */
public class SymbolArrangement {
    private final SymbolControl symbolControl;
    private EntityDimensions scale;
    private Vector3f offset;

    public SymbolArrangement(SymbolControl symbolControl, EntityDimensions scaling, Vector3f offset) {
        this.symbolControl = symbolControl;
        this.scale = scaling;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "ControlTypes{" + "control=" + symbolControl + ", scale=" + scale + ", offset=" + offset + '}';
    }

    public SymbolControl getControl() {
        return this.symbolControl;
    }

    public EntityDimensions getScale() {
        return this.scale;
    }

    public void setScale(EntityDimensions scale) {
        this.scale = scale;
    }

    public Vector3f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }
}
