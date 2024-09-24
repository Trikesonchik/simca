/*
 * Decompiled with CFR 0_123.
 */
package network.traffic;

import java.util.Vector;

public class TPacket {
    private Vector<TFlit> fVtrFlits = null;

    public int getCount() {
        return this.fVtrFlits.size();
    }

    public TFlit popFlit() {
        return this.fVtrFlits.remove(0);
    }

    public void setFlits(Vector<TFlit> aPaket) {
        this.fVtrFlits = aPaket;
    }
}

