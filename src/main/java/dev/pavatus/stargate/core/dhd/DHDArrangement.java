package dev.pavatus.stargate.core.dhd;

import dev.pavatus.stargate.core.dhd.control.impl.Symbol;
import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

public class DHDArrangement {
    private static final SymbolArrangement[] SYMBOL_ARRANGEMENT = new SymbolArrangement[]{
            new SymbolArrangement(new Symbol('A'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.42499999701976776f, 0.5500015262514353f, -0.21249390486627817f)), // TODO
            new SymbolArrangement(new Symbol('B'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.2125007575377822f, 0.7749984785914421f, 0.024987801909446716f)), // TODO
            new SymbolArrangement(new Symbol('C'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.24999922886490822f, 0.6624984769150615f, -0.20001220703125f)), // TODO
            new SymbolArrangement(new Symbol('D'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.0f, 0.8249984793365002f, 0.31249388959258795f)), // TODO
            new SymbolArrangement(new Symbol('E'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.11250000167638063f, 0.8000000026077032f, 0.08790282439440489f)), // TODO
            new SymbolArrangement(new Symbol('F'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.07499999925494194f, 0.5750015247613192f, -0.38749389443546534f)), // TODO
            new SymbolArrangement(new Symbol('G'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.07500000111758709f, 0.5750015247613192f, -0.38749389443546534f)), // TODO
            new SymbolArrangement(new Symbol('H'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.18750000093132257f, 0.6125015253201127f, -0.32499389350414276f)), // TODO
            new SymbolArrangement(new Symbol('I'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.11250000167638063f, 0.43750152457505465f, -0.4609252894297242f)), // TODO
            new SymbolArrangement(new Symbol('J'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.19750060979276896f, 0.6100006122142076f, -0.3159362720325589f)), // TODO
            new SymbolArrangement(new Symbol('K'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.1024993946775794f, 0.4399978630244732f, -0.4549926705658436f)), // TODO
            new SymbolArrangement(new Symbol('L'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.36249923054128885f, 0.7500015292316675f, 0.149993896484375f)), // TODO
            new SymbolArrangement(new Symbol('M'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.26000060699880123f, 0.6574966432526708f, -0.1975036608055234f)), // TODO
            new SymbolArrangement(new Symbol('N'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.20078124478459358f, 0.7875030552968383f, 0.27342529222369194f)), // TODO
            new SymbolArrangement(new Symbol('O'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.10937499534338713f, 0.8000030536204576f, 0.0874999975785613f)), // TODO
            new SymbolArrangement(new Symbol('P'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.27187499590218067f, 0.712503052316606f, -0.0867248522117734f)), // TODO
            new SymbolArrangement(new Symbol('Q'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-1.519918441772461E-6f, 0.8250030539929867f, 0.1250000074505806f)), // TODO
            new SymbolArrangement(new Symbol('R'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.27812499459832907f, 0.7125000013038516f, -0.08514404203742743f)), // TODO
            new SymbolArrangement(new Symbol('S'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.42499999329447746f, 0.6625015279278159f, -0.035949709825217724f)), // TODO
            new SymbolArrangement(new Symbol('T'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.20937423035502434f, 0.7750030551105738f, 0.018743902444839478f)), // TODO
            new SymbolArrangement(new Symbol('U'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.44062577933073044f, 0.5499999970197678f, -0.21874390542507172f)), // TODO
            new SymbolArrangement(new Symbol('V'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.29687423165887594f, 0.4875030508264899f, -0.3812439078465104f)), // TODO
            new SymbolArrangement(new Symbol('W'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.3593742325901985f, 0.7500015273690224f, 0.15625610016286373f)), // TODO
            new SymbolArrangement(new Symbol('X'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.3156257774680853f, 0.48749694880098104f, -0.3687439076602459f)), // TODO
            new SymbolArrangement(new Symbol('Y'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.19687423016875982f, 0.7874969458207488f, 0.26875610183924437f)), // TODO
            new SymbolArrangement(new Symbol('Z'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.4281257791444659f, 0.662498471327126f, -0.04374390281736851f)), // TODO
            new SymbolArrangement(new Symbol('?'), EntityDimensions.changing(0.25f, 0.1f),
                    new Vector3f(-3.7997961044311523E-7f, 0.7375015262514353f, -0.10000000149011612f)), // TODO
    };

    public static SymbolArrangement[] getSymbolArrangement() {
        return SYMBOL_ARRANGEMENT;
    }
}
