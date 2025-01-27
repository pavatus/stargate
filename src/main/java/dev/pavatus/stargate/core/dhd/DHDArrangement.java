package dev.pavatus.stargate.core.dhd;

import dev.pavatus.stargate.core.dhd.control.impl.Symbol;
import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

public class DHDArrangement {
    private static final SymbolArrangement[] SYMBOL_ARRANGEMENT = new SymbolArrangement[]{
            new SymbolArrangement(new Symbol('A'), EntityDimensions.changing(0.275f, 0.275f),
                    new Vector3f(0.625f, 0.5250015258789062f, 0.34999999962747097f)), // 0
            new SymbolArrangement(new Symbol('B'), EntityDimensions.changing(0.175f, 0.175f),
                    new Vector3f(-0.5750000085681677f, 0.4749999772757292f, -0.5750000234693289f)), // 1
            new SymbolArrangement(new Symbol('C'), EntityDimensions.changing(0.175f, 0.175f),
                    new Vector3f(-0.8000000156462193f, 0.4749999772757292f, -0.20000000298023224f)), // 2
            new SymbolArrangement(new Symbol('D'), EntityDimensions.changing(0.175f, 0.175f),
                    new Vector3f(0.0f, 0.4749999772757292f, 0.7750000078231096f)), // 3
            new SymbolArrangement(new Symbol('E'), EntityDimensions.changing(0.087499976f, 0.087499976f),
                    new Vector3f(0.0f, 0.6249998994171619f, 0.6003906223922968f)),
            new SymbolArrangement(new Symbol('F'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.12500001676380634f, 0.6499999798834324f, -0.5250000078231096f)), // 4
            new SymbolArrangement(new Symbol('G'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.0f, 0.6499999798834324f, -0.5249999929219484f)), // 5
            new SymbolArrangement(new Symbol('H'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.12499999813735485f, 0.6499999798834324f, -0.5250000040978193f)), // 6
            new SymbolArrangement(new Symbol('I'), EntityDimensions.changing(0.15f, 0.15f),
                    new Vector3f(-1.862645149230957E-9f, 0.5249999780207872f, -0.6984375026077032f)), // 7
            new SymbolArrangement(new Symbol('J'), EntityDimensions.changing(0.125f, 0.125f),
                    new Vector3f(0.23500000406056643f, 0.48499997798353434f, -0.8034375039860606f)), // 8
            new SymbolArrangement(new Symbol('K'), EntityDimensions.changing(0.125f, 0.125f),
                    new Vector3f(-0.23500000312924385f, 0.4899999788030982f, -0.8050000118091702f)), // 9
            new SymbolArrangement(new Symbol('L'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.8000000212341547f, 0.5249999780207872f, 0.14999999664723873f)), // 10
            new SymbolArrangement(new Symbol('M'), EntityDimensions.changing(0.125f, 0.125f),
                    new Vector3f(0.4850000077858567f, 0.6449999799951911f, -0.28500001039355993f)), // 11
            new SymbolArrangement(new Symbol('N'), EntityDimensions.changing(0.325f, 0.125f),
                    new Vector3f(-0.5507812462747097f, 0.5499999932944775f, 0.3234375026077032f)), // 12
            new SymbolArrangement(new Symbol('O'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(-0.421875f, 0.6750001907348633f, 0.25f)), // 13
            new SymbolArrangement(new Symbol('P'), EntityDimensions.changing(0.074999996f, 0.074999996f),
                    new Vector3f(-0.5468749962747097f, 0.6749999076128006f, -0.17421874962747097f)), // 2
            new SymbolArrangement(new Symbol('Q'), EntityDimensions.changing(0.075f, 0.075f),
                    new Vector3f(-0.4000000134110451f, 0.674999987706542f, -0.37500001303851604f)), // idk
            new SymbolArrangement(new Symbol('R'), EntityDimensions.changing(0.074999996f, 0.099999994f),
                    new Vector3f(0.6406250018626451f, 0.625000286847353f, -0.14765625819563866f)),
            new SymbolArrangement(new Symbol('S'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.8750000037252903f, 0.5249999780207872f, 0.15156249701976776f)), // 14
            new SymbolArrangement(new Symbol('T'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.5499999783933163f, -0.3812500014901161f)), // 15
            new SymbolArrangement(new Symbol('U'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.7499999783933163f, 0.3812500014901161f)), // 15
            new SymbolArrangement(new Symbol('V'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.5499999783933163f, 0.3812500014901161f)), // 15
            new SymbolArrangement(new Symbol('W'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.6499999783933163f, 0.3812500014901161f)), // 15
            new SymbolArrangement(new Symbol('X'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.4499999783933163f, 0.3812500014901161f)), // 15
            new SymbolArrangement(new Symbol('Y'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.9499999783933163f, 0.3812500014901161f)), // 15
            new SymbolArrangement(new Symbol('Z'), EntityDimensions.changing(0.1f, 0.1f),
                    new Vector3f(0.6593750026077032f, 0.8499999783933163f, 0.3812500014901161f)), // 15
    };

    public static SymbolArrangement[] getSymbolArrangement() {
        return SYMBOL_ARRANGEMENT;
    }
}
