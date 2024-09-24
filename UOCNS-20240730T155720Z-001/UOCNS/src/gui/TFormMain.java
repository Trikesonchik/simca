/*
 * Decompiled with CFR 0_123.
 */
package gui;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMessageBox;

import network.TCommandOCNS;
import network.TControllerOCNS;
import network.TNetworkManager;
import network.common.generator.Circulant;
import network.common.generator.Mesh;
import network.common.generator.Torus;
import network.common.generator.utils.XmlWriter;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TFormMain extends QMainWindow {
	// declarations of constants and used variables
    private static /* synthetic */ int[] $SWITCH_TABLE$gui$TFormMain$TStateOCNS;
    
    public static final String MESH_TOPOLOGY = "Mesh";
    public static final String TORUS_TOPOLOGY = "Torus";
    public static final String CIRCULANT_TOPOLOGY = "Circulant";
    public static final String OPTIMAL_CIRCULANT_TOPOLOGY = "CirculantOpt";
    public static final String DIJKSTRA_ALGORITHM = "Dijkstra";
    public static final String PO_ALGORITHM = "PO";
    public static final String ROU_ALGORITHM = "ROU";
    public static String RESULTS_PATH = "";
    public static String ALGORITHM = "";
    public static String ROU_ITERS = "";
    
    public boolean isCmd = false;
    public String filepath;
    public String outputFilepath;
    public String outputTableFilepath;
    
    private TUIFormMain fUIFormMain = new TUIFormMain();
    
    private TControllerOCNS fControllerOCNS;

	/**
	 *  Main form constructor.
	 */
    public TFormMain() {
    	// configuring GUI states
        this.fUIFormMain.setupUi(this);
        this.SetStateButtons(TStateOCNS.Stoped);
        
        this.fUIFormMain.TELog.clear();
        onTopologyChanged();
        onAlgorithmChanged();
        
        this.fUIFormMain.LEResPath.setText(RESULTS_PATH);
        
        this.fControllerOCNS = new TControllerOCNS(this);
        
        this.fControllerOCNS.fSglProgressChanged.connect((Object) this, "onProgressChanged(TControllerOCNS, String)");
        this.fControllerOCNS.fSglNetworkSimulated.connect((Object) this, "onNetworkSimulated(TControllerOCNS, String)");
        this.fControllerOCNS.fSglNetworkSimulatedAll.connect((Object) this, "OnNetworkSimulatedAll(TControllerOCNS)");
        
        // connecting GUI elements and actions
        this.fUIFormMain.fBtnRun.clicked.connect(this, "onBtnRunClick()");
        this.fUIFormMain.fBtnPause.clicked.connect(this, "onBtnPauseClick()");
        this.fUIFormMain.fBtnResume.clicked.connect(this, "onBtnResumeClick()");
        this.fUIFormMain.fBtnStop.clicked.connect(this, "onBtnStopClick()");
        this.fUIFormMain.fBtnExport.clicked.connect(this, "onBtnExportClick()");
        this.fUIFormMain.CBTopology.currentIndexChanged.connect(this, "onTopologyChanged()");
        this.fUIFormMain.CBAlgorithm.currentIndexChanged.connect(this, "onAlgorithmChanged()");
        this.fUIFormMain.fBtnImport.clicked.connect(this, "onBtnImportClick()");
        this.fUIFormMain.fBtnResPath.clicked.connect(this, "onBtnChangeClick()");
        this.fUIFormMain.LEROUiters.textChanged.connect(this, "onItersChanged()");
    }

    /**
     * Main point of entry to the application.
     * @param args
     */
    public static void main(String[] args) {
    	// initializing application
        QApplication.initialize(args);
              
        // getting default working directory
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        s = s.replace('\\', '/');
        s += "/results/";
        RESULTS_PATH = s;

        // creating main form
        TFormMain fFormMain = new TFormMain();
        fFormMain.show();
            
        QApplication.execStatic();
    }

    /**
     * Function helps to switch simulation states.
     * @return
     */
    static /* synthetic */ int[] $SWITCH_TABLE$gui$TFormMain$TStateOCNS() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$gui$TFormMain$TStateOCNS;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[TStateOCNS.values().length];
        try {
            arrn[TStateOCNS.Finished.ordinal()] = 4;
        } catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            arrn[TStateOCNS.Paused.ordinal()] = 2;
        } catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            arrn[TStateOCNS.Running.ordinal()] = 1;
        } catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            arrn[TStateOCNS.Stoped.ordinal()] = 3;
        } catch (NoSuchFieldError noSuchFieldError) {
        }
        $SWITCH_TABLE$gui$TFormMain$TStateOCNS = arrn;
        return $SWITCH_TABLE$gui$TFormMain$TStateOCNS;
    }

    /**
     * Function describes actions executed
     * when run button is clicked.
     */
    public void onBtnRunClick() {
    	// checking simulation input arguments
    	boolean argsOk = checkArgs();
    	
    	// if args are correct starting simulation
    	if (argsOk == true) {
    		String [] args = prepareArgs();
    		String descr = parseArgs(args);
    		filepath = RESULTS_PATH+descr+"/config-"+descr+"__2____"+".xml";
            outputFilepath = RESULTS_PATH+descr+"/result-"+descr +"__2____"
                    + LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")) + ".html";
            outputTableFilepath = RESULTS_PATH+descr+"/table-"+descr+".txt";
            
            String aFilePath = filepath;
            TNetworkManager.doReadConfigFile(aFilePath);
            this.fControllerOCNS.AddCommand(TCommandOCNS.Start);
            this.SetStateButtons(TStateOCNS.Running);
            this.fUIFormMain.TELog.clear();
    	}
    	// otherwise showing error message
    	else {
    		QMessageBox msgBox = new QMessageBox();
    		msgBox.setWindowTitle("Warning");
    		msgBox.setText("Some input arguments not specified or incorrect arguments");
    		msgBox.exec(); 		
    	}
    }

    /**
     * Function describes actions executed
     * when pause button is clicked.
     */
    public void onBtnPauseClick() {
        this.fControllerOCNS.AddCommand(TCommandOCNS.Pause);
        this.SetStateButtons(TStateOCNS.Paused);
    }

    /**
     * Function describes actions executed
     * when resume button is clicked.
     */
    public void onBtnResumeClick() {
        this.fControllerOCNS.AddCommand(TCommandOCNS.Continue);
        this.SetStateButtons(TStateOCNS.Running);
    }

    /**
     * Function describes actions executed
     * when stop button is clicked.
     */
    public void onBtnStopClick() {
    	this.fControllerOCNS.AddCommand(TCommandOCNS.Pause);
        this.SetStateButtons(TStateOCNS.Stoped);
              
        this.fControllerOCNS = new TControllerOCNS(this);
        
        this.fControllerOCNS.fSglProgressChanged.connect((Object) this, "onProgressChanged(TControllerOCNS, String)");
        this.fControllerOCNS.fSglNetworkSimulated.connect((Object) this, "onNetworkSimulated(TControllerOCNS, String)");
        this.fControllerOCNS.fSglNetworkSimulatedAll.connect((Object) this, "OnNetworkSimulatedAll(TControllerOCNS)");
        
        this.fUIFormMain.PBProgress.setValue(0);
        this.fUIFormMain.TELog.clear();       
    }

    /**
     * Function describes actions executed
     * when stop button is clicked.
     */
    public void onBtnExportClick() {
    	// saving results to working directory
        String FilePath;

        if (outputFilepath != null && !outputFilepath.isEmpty()) {
            Path finalPath = Paths.get(outputFilepath);

            try {
                Path directory = finalPath.getParent();

                if (!Files.exists(directory)) {
                    Files.createDirectories(finalPath.getParent());
                }
            } catch (Exception e) {
            }
            try {
                Files.createFile(finalPath);
                Files.write(finalPath, Charset.forName("UTF-8").encode(fUIFormMain.TELog.toHtml()).array());
            } catch (Exception e) {
            }
        } else {
            FilePath = QFileDialog.getSaveFileName(this, this.tr("Choose file to save simulation report"), "", new QFileDialog.Filter(this.tr("NOC Performance file (*.txt)")));
            if (FilePath.isEmpty()) {
                return;
            }
            this.fUIFormMain.TELog.append("Results were saved");
        }
        this.fUIFormMain.TELog.append("Results were saved");
    }

    /**
     * Function describes actions executed
     * when simulation progress changes.
     * @param Sender
     * @param value
     */
    public void onProgressChanged(TControllerOCNS Sender, String value) {
        this.fUIFormMain.PBProgress.setValue(Integer.parseInt(value));
    }

    /**
     * Function describes actions executed
     * when simulation cycle ends.
     * @param Sender
     * @param aReport
     */
    public void onNetworkSimulated(TControllerOCNS Sender, String aReport) {
        this.fUIFormMain.TELog.append(aReport);
    }

    /**
     * Function describes actions executed
     * when the whole simulation ends.
     * @param Sender
     */
    public void OnNetworkSimulatedAll(TControllerOCNS Sender) {
        this.SetStateButtons(TStateOCNS.Finished);
    }

    /**
     * Function enables or disables
     * GUI elements
     * when simulation state changes.
     * @param aStateOCNS
     * @return
     */
    private boolean SetStateButtons(TStateOCNS aStateOCNS) {
        switch (TFormMain.$SWITCH_TABLE$gui$TFormMain$TStateOCNS()[aStateOCNS.ordinal()]) {
            case 3: {
            	// state stoped
                this.fUIFormMain.fBtnRun.setEnabled(true);
                this.fUIFormMain.fBtnPause.setEnabled(false);
                this.fUIFormMain.fBtnResume.setEnabled(false);
                this.fUIFormMain.fBtnStop.setEnabled(false);
                this.fUIFormMain.fBtnExport.setEnabled(false);
                
                this.fUIFormMain.fBtnImport.setEnabled(true);
                this.fUIFormMain.CBTopology.setEnabled(true);
                this.fUIFormMain.CBAlgorithm.setEnabled(true);
                this.fUIFormMain.LEarg1.setEnabled(true);
                this.fUIFormMain.LEarg2.setEnabled(true);
                this.fUIFormMain.LEarg3.setEnabled(true);
                this.fUIFormMain.fBtnImport.setEnabled(true);
                this.fUIFormMain.fBtnResPath.setEnabled(true);
                this.fUIFormMain.LEROUiters.setEnabled(true);
                
                break;
            }
            case 1: {
            	// state running
                this.fUIFormMain.fBtnRun.setEnabled(false);
                this.fUIFormMain.fBtnPause.setEnabled(true);
                this.fUIFormMain.fBtnResume.setEnabled(false);
                this.fUIFormMain.fBtnStop.setEnabled(true);
                this.fUIFormMain.fBtnExport.setEnabled(false);
                
                this.fUIFormMain.fBtnImport.setEnabled(false);
                this.fUIFormMain.CBTopology.setEnabled(false);
                this.fUIFormMain.CBAlgorithm.setEnabled(false);
                this.fUIFormMain.LEarg1.setEnabled(false);
                this.fUIFormMain.LEarg2.setEnabled(false);
                this.fUIFormMain.LEarg3.setEnabled(false);
                this.fUIFormMain.fBtnImport.setEnabled(false);
                this.fUIFormMain.fBtnResPath.setEnabled(false);
                this.fUIFormMain.LEROUiters.setEnabled(false);
                
                break;
            }
            case 2: {
            	// state paused
                this.fUIFormMain.fBtnRun.setEnabled(false);
                this.fUIFormMain.fBtnPause.setEnabled(false);
                this.fUIFormMain.fBtnResume.setEnabled(true);
                this.fUIFormMain.fBtnStop.setEnabled(true);
                this.fUIFormMain.fBtnExport.setEnabled(false);
                
                this.fUIFormMain.fBtnImport.setEnabled(false);
                this.fUIFormMain.CBTopology.setEnabled(false);
                this.fUIFormMain.CBAlgorithm.setEnabled(false);
                this.fUIFormMain.LEarg1.setEnabled(false);
                this.fUIFormMain.LEarg2.setEnabled(false);
                this.fUIFormMain.LEarg3.setEnabled(false);
                this.fUIFormMain.fBtnImport.setEnabled(false);
                this.fUIFormMain.fBtnResPath.setEnabled(false);
                this.fUIFormMain.LEROUiters.setEnabled(false);
                
                break;
            }
            case 4: {
            	// state finished
                this.fUIFormMain.fBtnRun.setEnabled(true);
                this.fUIFormMain.fBtnPause.setEnabled(false);
                this.fUIFormMain.fBtnResume.setEnabled(false);
                this.fUIFormMain.fBtnStop.setEnabled(false);
                this.fUIFormMain.fBtnExport.setEnabled(true);
                
                this.fUIFormMain.fBtnImport.setEnabled(true);
                this.fUIFormMain.CBTopology.setEnabled(true);
                this.fUIFormMain.CBAlgorithm.setEnabled(true);
                this.fUIFormMain.LEarg1.setEnabled(true);
                this.fUIFormMain.LEarg2.setEnabled(true);
                this.fUIFormMain.LEarg3.setEnabled(true);
                this.fUIFormMain.fBtnImport.setEnabled(true);
                this.fUIFormMain.fBtnResPath.setEnabled(true);
                this.fUIFormMain.LEROUiters.setEnabled(true);

                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }

    // states enumeration
    public enum TStateOCNS {
        Running,
        Paused,
        Stoped,
        Finished;
    }

    /**
     * Function parses input arguments,
     * creates network class instance
     * and a configuration file.
     * @param args
     * @return network description
     */
    public static String parseArgs (String[] args) {
        String descr;
        if (MESH_TOPOLOGY.equals(args[0])) {
            int n, m;
            n = Integer.parseInt(args[1]);
            m = Integer.parseInt(args[2]);
            Mesh meshNetwork;
            if (n == m) {
                meshNetwork = new Mesh(n, n);
            } else {
                meshNetwork = new Mesh(n, m);
            }
            meshNetwork.createNetlist();
            meshNetwork.createRouting();
            descr = MESH_TOPOLOGY + "-(" + n + ", " + m + ")";
            createXml(meshNetwork.getNetlist(), meshNetwork.getRouting(), descr);

        } else if (TORUS_TOPOLOGY.equals(args[0])) {
            int n, m;
            n = Integer.parseInt(args[1]);
            m = Integer.parseInt(args[2]);
            Torus torusNetwork;
            if (n == m) {
                torusNetwork = new Torus(n, n);
            } else {
                torusNetwork = new Torus(n, m);
            }
            torusNetwork.createNetlist();
            torusNetwork.createRouting();
            descr = TORUS_TOPOLOGY + "-(" + n + ", " + m + ")";
            createXml(torusNetwork.getNetlist(), torusNetwork.getRouting(), descr);
        } else if (OPTIMAL_CIRCULANT_TOPOLOGY.equals(args[0])) {
            int k = Integer.parseInt(args[1]);

            if (k < 5) {
                System.out.println("Unexpected topology type!\nPlease check input args");
                return null;
            } else {
                Circulant circulantNetwork = new Circulant(k);
                circulantNetwork.createNetlist();
                circulantNetwork.createRouting(circulantNetwork.adjacencyMatrix(circulantNetwork.getNetlist(), k),
                        circulantNetwork.getNetlist());
                descr = OPTIMAL_CIRCULANT_TOPOLOGY + "-(" + k + ", " + circulantNetwork.s1 + ", " + circulantNetwork.s2 + ")";
                createXml(circulantNetwork.getNetlist(), circulantNetwork.getRouting(), descr);
            }

        } else if (CIRCULANT_TOPOLOGY.equals(args[0])){
            int k = Integer.parseInt(args[1]);
            int s1 = Integer.parseInt(args[2]);
            int s2 = Integer.parseInt(args[3]);
            if (k < 5) {
                System.out.println("Unexpected topology type!\nPlease check input args");
                return null;
            } else {
                Circulant circulantNetwork = new Circulant(k, s1, s2);
                circulantNetwork.createNetlist();
                circulantNetwork.createRouting(circulantNetwork.adjacencyMatrix(circulantNetwork.getNetlist(), k),
                        circulantNetwork.getNetlist());
                descr = CIRCULANT_TOPOLOGY + "-(" + k + ", " + s1 + ", " + s2 + ")";
                createXml(circulantNetwork.getNetlist(), circulantNetwork.getRouting(), descr);
            }
        } else {
            System.out.println("Unexpected topology type!");
            return null;
        }
        return descr;
    }

    /**
     * Function creates xml with simulation results
     * @param netlist
     * @param routing
     * @param descr
     */
    public static void createXml(int[][] netlist, int[][] routing, String descr) {
        String netlistData = "\n";
        for (int i = 0; i < routing.length; i++) {
            for (int j = 0; j < 4; j++) {
                netlistData += netlist[i][j] + " ";
            }
            netlistData += "\n";
        }

        String routingData = "\n";
        for (int i = 0; i < routing.length; i++) {
            for (int j = 0; j < routing.length; j++) {
                routingData += routing[i][j] + " ";
            }
            routingData += "\n";
        }
        XmlWriter xmlDoc = new XmlWriter(netlistData, routingData, descr);
    }

    /**
     * Function enables or disables
     * GUI elements
     * when selected topology changes.
     */
    public void onTopologyChanged() {
    	this.fUIFormMain.LEarg1.clear();
    	this.fUIFormMain.LEarg2.clear();
    	this.fUIFormMain.LEarg3.clear();
    	
    	String selected = this.fUIFormMain.CBTopology.currentText();
    	
    	if (selected.equals(MESH_TOPOLOGY) || selected.equals(TORUS_TOPOLOGY)) {
    		this.fUIFormMain.LEarg1.setVisible(true);
    		this.fUIFormMain.LEarg2.setVisible(true);
    		this.fUIFormMain.LEarg3.setVisible(false);
    		this.fUIFormMain.CBAlgorithm.setVisible(false);
    	}
    	
    	if (selected.equals(CIRCULANT_TOPOLOGY)) {
    		this.fUIFormMain.LEarg1.setVisible(true);
    		this.fUIFormMain.LEarg2.setVisible(true);
    		this.fUIFormMain.LEarg3.setVisible(true);
    		this.fUIFormMain.CBAlgorithm.setVisible(true);
    	}
    	
    	if (selected.equals(OPTIMAL_CIRCULANT_TOPOLOGY)) {
    		this.fUIFormMain.LEarg1.setVisible(true);
    		this.fUIFormMain.LEarg2.setVisible(false);
    		this.fUIFormMain.LEarg3.setVisible(false);
    		this.fUIFormMain.CBAlgorithm.setVisible(true);
    	}
    }
    
    /**
     * Function describes actions executed
     * when selected algorithm changes.
     */
    public void onAlgorithmChanged() {
    	ALGORITHM = this.fUIFormMain.CBAlgorithm.currentText();
    	if (ALGORITHM.equals(ROU_ALGORITHM)) {
    		this.fUIFormMain.LEROUiters.setVisible(true);
    	}
    	else {
    		this.fUIFormMain.LEROUiters.setVisible(false);
    	}
    }
    
    /**
     * Function checks input arguments.
     * @return TRUE if simulation can be started, otherwise FALSE
     */
    public boolean checkArgs() {
    	boolean argsOk = true;
    	
    	String selected = this.fUIFormMain.CBTopology.currentText();
    	String arg1 = this.fUIFormMain.LEarg1.text();
    	String arg2 = this.fUIFormMain.LEarg2.text();
    	String arg3 = this.fUIFormMain.LEarg3.text();
    	
    	if ( (selected.equals(MESH_TOPOLOGY) || selected.equals(TORUS_TOPOLOGY)) && (arg1.equals("") || arg2.equals("")) ) {
    		argsOk = false;
    	}
    	
    	if (selected.equals(CIRCULANT_TOPOLOGY)) {
    		if (arg1.equals("") || arg2.equals("") || arg3.equals("")) {
    			argsOk = false;
    		}
    		if ((this.fUIFormMain.CBAlgorithm.currentText().equals(PO_ALGORITHM) || this.fUIFormMain.CBAlgorithm.currentText().equals(ROU_ALGORITHM)) && !arg2.equals("1")) {
    			argsOk = false;
    		}	
    	}
    	
    	if (selected.equals(OPTIMAL_CIRCULANT_TOPOLOGY)) {
    		if (arg1.equals("")) {
    			argsOk = false;
    		}
    		else {
    			if (this.fUIFormMain.CBAlgorithm.currentText().equals(PO_ALGORITHM) || this.fUIFormMain.CBAlgorithm.currentText().equals(ROU_ALGORITHM)) {
    				int k = Integer.parseInt(arg1);
    				int d = (int)Math.round((Math.sqrt(2*k - 1) - 1)/2);
    				if (d != 1) {
    					argsOk = false;
    				}
    			}		
    		}	
    	}
    	
    	if (this.fUIFormMain.CBAlgorithm.currentText().equals(ROU_ALGORITHM) && this.fUIFormMain.LEROUiters.text().equals("")) {
    		argsOk = false;
    	}
    	
    	return argsOk;
    }
    
    /**
     * Function prepares input arguments
     * for simulation.
     * @return formatted arguments
     */
    public String[] prepareArgs() {
    	String[] args = new String[]{};
    	
    	String selected = this.fUIFormMain.CBTopology.currentText();
    	String arg1 = this.fUIFormMain.LEarg1.text();
    	String arg2 = this.fUIFormMain.LEarg2.text();
    	String arg3 = this.fUIFormMain.LEarg3.text();
    	
    	if (selected.equals(MESH_TOPOLOGY) || selected.equals(TORUS_TOPOLOGY)) {
    		args = new String[]{selected, arg1, arg2};
    	}
    	
    	if (selected.equals(CIRCULANT_TOPOLOGY)) {
    		args = new String[]{selected, arg1, arg2, arg3};
    	}
    	
    	if (selected.equals(OPTIMAL_CIRCULANT_TOPOLOGY)) {
    		args = new String[]{selected, arg1};
    	} 
    	
    	return args;
    }
    
    /**
     * Function describes actions executed
     * when import button is clicked.
     */
    public void onBtnImportClick() {
    	// opening configuration file
    	String aFilePath;
        aFilePath = QFileDialog.getOpenFileName(this, this.tr("Open NOC parameters file"), "", new QFileDialog.Filter(this.tr("NOC Parameters file (*.xml)")));

        if (aFilePath.isEmpty()) {
            return;
        }
        
        TNetworkManager.doReadConfigFile(aFilePath);
        this.fControllerOCNS.AddCommand(TCommandOCNS.Start);
        this.SetStateButtons(TStateOCNS.Running);
        this.fUIFormMain.TELog.clear();
    }
    
    /**
     * Function describes actions executed
     * when change button is clicked
     */
    public void onBtnChangeClick() {
    	// opening new directory
    	String newDir;
    	newDir = QFileDialog.getExistingDirectory(this, tr("Open directory"), RESULTS_PATH);
    	
    	if (newDir != null ) {
    		newDir = newDir.replace('\\', '/');
    		newDir += '/';
    		
    		this.fUIFormMain.LEResPath.setText(newDir);
    		RESULTS_PATH = newDir;
    	}
    	
    }
    
    /**
     * Function describes actions executed
     * when rou iters amount changes.
     */
    public void onItersChanged() {
    	ROU_ITERS = this.fUIFormMain.LEROUiters.text();
    }

}

