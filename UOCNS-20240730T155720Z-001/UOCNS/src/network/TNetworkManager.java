/*
 * Decompiled with CFR 0_123.
 */
package network;

import network.common.IConstants;
import network.common.TNocParameter;
import network.common.TStatisticalData;
import network.common.TUtilities;

import java.util.Scanner;
import java.util.Vector;

public class TNetworkManager {
    private static TNetworkManager fNetworkManager = null;
    private static TNetwork fNetwork = null;
    private static TStatisticalData fStatisticalData = null;
    private static TUtilities fUtilities = null;
    private static int fConfigActive;

    public TNetworkManager(String aConfigFilePath) {
        fUtilities = new TUtilities();
        fUtilities.doReadConfigFile(aConfigFilePath);
        fConfigActive = 0;
    }

    public static void doReadConfigFile(String aConfigFile) {
        fNetworkManager = new TNetworkManager(aConfigFile);
    }

    public static TNetworkManager getInstance() {
        return fNetworkManager;
    }

    public static TNetwork getNetworkInstance() {
        return fNetwork;
    }

    public static TUtilities getUtilities() {
        return fUtilities;
    }

    public static TStatisticalData getStatistic() {
        return fStatisticalData;
    }

    public boolean doNetworkSetupNext(boolean IsSwitchNetwork) {
        fUtilities.setRandSeedRandom();
        Vector<TNocParameter> aVtrConfigNOC = fUtilities.getConfig(fConfigActive);
        if (aVtrConfigNOC == null) {
            return false;
        }
        this.setConfigNoC(aVtrConfigNOC);
        if (IsSwitchNetwork) {
            ++fConfigActive;
        }
        return true;
    }

    public void doNetworkReset(IControllerOCNS aControllerOCNS) {
        fNetwork = new TNetwork();
        fStatisticalData = new TStatisticalData(aControllerOCNS);
    }

    private void setConfigNoC(Vector<TNocParameter> aParameterSet) {
        if (aParameterSet == null) {
            return;
        }
        int iParameter = 0;
        while (iParameter < aParameterSet.size()) {
            String iParameterKey = aParameterSet.get(iParameter).getName();
            String iParameterValue = aParameterSet.get(iParameter).getValue();
            if (iParameterKey.equalsIgnoreCase("Description")) {
                IConstants.fConfigNoC.fDescription = iParameterValue;
            } else {
                int iStrRouter;
                String[] nStrRouter;
                if (iParameterKey.equalsIgnoreCase("Netlist")) {
                    iParameterValue = iParameterValue.trim();
                    nStrRouter = iParameterValue.split("\n");
                    iStrRouter = 0;
                    while (iStrRouter < nStrRouter.length) {
                        nStrRouter[iStrRouter] = nStrRouter[iStrRouter].trim();
                        ++iStrRouter;
                    }
                    int aCountRouterPort = 4;
                    IConstants.fConfigNoC.fNetlist = new int[nStrRouter.length][4];
                    int iRouterId = 0;
                    while (iRouterId < nStrRouter.length) {
                        Scanner aScanner = new Scanner(nStrRouter[iRouterId]);
                        int iPortId = 0;
                        while (iPortId < 4) {
                            IConstants.fConfigNoC.fNetlist[iRouterId][iPortId] = aScanner.nextInt();
                            ++iPortId;
                        }
                        ++iRouterId;
                    }
                }
                if (iParameterKey.equalsIgnoreCase("Routing")) {
                    iParameterValue = iParameterValue.trim();
                    nStrRouter = iParameterValue.split("\n");
                    iStrRouter = 0;
                    while (iStrRouter < nStrRouter.length) {
                        nStrRouter[iStrRouter] = nStrRouter[iStrRouter].trim();
                        ++iStrRouter;
                    }
                    IConstants.fConfigNoC.fRoutingTable = new int[nStrRouter.length][nStrRouter.length];
                    int iRouterId = 0;
                    while (iRouterId < nStrRouter.length) {
                        Scanner aScanner = new Scanner(nStrRouter[iRouterId]);
                        int iDestCoreId = 0;
                        while (iDestCoreId < nStrRouter.length) {
                            IConstants.fConfigNoC.fRoutingTable[iRouterId][iDestCoreId] = aScanner.nextInt();
                            ++iDestCoreId;
                        }
                        ++iRouterId;
                    }
                    IConstants.fConfigNoC.fCountCores = IConstants.fConfigNoC.fRoutingTable.length;
                }
                if (iParameterKey.equalsIgnoreCase("FifoSize")) {
                    IConstants.fConfigNoC.fSizeVLinkBuffer = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("FifoCount")) {
                    IConstants.fConfigNoC.fCountVLinkPerPLink = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("FlitSize")) {
                    IConstants.fConfigNoC.fFlitSize = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("PacketSizeAvg")) {
                    IConstants.fConfigNoC.fPacketAvgLenght = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("PacketSizeIsFixed")) {
                    IConstants.fConfigNoC.fPacketIsFixedLength = Boolean.valueOf(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("PacketPeriodAvg")) {
                    IConstants.fConfigNoC.fPacketAvgGenTime = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("CountRun")) {
                    IConstants.fConfigNoC.fCountRun = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("CountClockTotal")) {
                    IConstants.fConfigNoC.fCountClocksTotal = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("CountPacketRx")) {
                    IConstants.fConfigNoC.fCountPacketRx = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("CountPacketRxWarmUp")) {
                    IConstants.fConfigNoC.fCountPacketRxWarmUp = Integer.parseInt(iParameterValue);
                } else if (iParameterKey.equalsIgnoreCase("IsModeGALS")) {
                    // empty if block
                }
            }
            ++iParameter;
        }
    }
}

