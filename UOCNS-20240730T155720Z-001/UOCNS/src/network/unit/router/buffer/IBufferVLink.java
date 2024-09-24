/*
 * Decompiled with CFR 0_123.
 */
package network.unit.router.buffer;

import network.traffic.TFlit;

public interface IBufferVLink {
    public void pushBack(TFlit var1, int var2) throws Exception;

    public TFlit getFront();

    public TFlit popFront(int var1) throws Exception;

    public boolean canPushHead(int var1);

    public boolean canPushData(int var1);

    public boolean canPop(int var1);

    public int getCount();

    public int GetPacketFlitsRemain();

    public void doPrepareNextClock(int var1);
}

