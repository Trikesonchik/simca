/*
 * Decompiled with CFR 0_123.
 */
package network.unit.router.buffer;

public interface IBufferPLink {
    public int getBufferVLinkCount();

    public IBufferVLink getBufferVLink(int var1);

    public int getCountSlotUsed();

    public void doPrepareNextClock(int var1);
}

