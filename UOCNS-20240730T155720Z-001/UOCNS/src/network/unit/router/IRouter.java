/*
 * Decompiled with CFR 0_123.
 */
package network.unit.router;

import network.traffic.TFlit;
import network.unit.core.TCore;

public interface IRouter {
    public int getId();

    public int getCountPLink();

    public void doConnectCore(int var1, TCore var2);

    public void doConnectRouter(int var1, IRouter var2);

    public void doResetCrossbarLinks();

    public void pushBackRx(int var1, TFlit var2, int var3) throws Exception;

    public boolean canPushData(int var1, int var2, int var3);

    public void setCrossbarLinks(int var1) throws Exception;

    public void moveTraficInternal(int var1) throws Exception;

    public void moveTraficExternal(int var1) throws Exception;

    public boolean canPushHead(int var1, int var2, int var3);

    public void doUpdateStatistic(int var1);

    public void doPrepareNextClock(int var1);

    public int getPLinIdConnectedRouter(IRouter var1);
}

