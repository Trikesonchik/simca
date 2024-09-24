/*
 * Decompiled with CFR 0_123.
 */
package network.traffic;

import java.util.Vector;

public interface ICoreTraffic {
    public void setNextMessageTime(int var1, boolean var2);

    public int getClockNextPacket();

    public int getPacketCoreTo();

    public int getPacketSize();

    public Vector<TFlit> getNewPacket(long var1, int var3);

    public TFlit getNewHeadFlit(long var1, int var3, int var4, int var5);

    public TFlit getNewDataFlit(int var1, int var2);
}

