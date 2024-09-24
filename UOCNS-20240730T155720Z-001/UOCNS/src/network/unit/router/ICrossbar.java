/*
 * Decompiled with CFR 0_123.
 */
package network.unit.router;

public interface ICrossbar {
    public int setConnectionHead(int var1, int var2, int var3, int var4) throws Exception;

    public void setConnectionData(int var1, int var2, int var3) throws Exception;

    public void resetConnection(int var1, int var2) throws Exception;

    public boolean canPushData(int var1, int var2) throws Exception;

    public int tryAllocVLink(int var1, int var2) throws Exception;

    public int getId();
}

