/*
 * Decompiled with CFR 0_123.
 */
package gui;

import java.util.Arrays;
import java.util.List;

import com.trolltech.qt.QUiForm;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

public class TUIFormMain
        implements QUiForm<QMainWindow> {
	// declaration of main widget
	public QWidget centralwidget;
	
	// declarations of GUI layouts
	public QVBoxLayout verticalLayout;
	public QHBoxLayout horizontalLayout;
	public QHBoxLayout argsLayout;
	public QHBoxLayout imExLayout; 
	public QHBoxLayout resPathLayout;
	
	// declarations of GUI elements
    public QPushButton fBtnRun;
    public QPushButton fBtnPause;
    public QPushButton fBtnResume;
    public QPushButton fBtnStop;
    public QPushButton fBtnExport;   
    public QTextEdit TELog;
    public QProgressBar PBProgress;
    public QMenuBar menubar;
    public QStatusBar statusbar; 
    public QComboBox CBTopology;
    public QComboBox CBAlgorithm;
    public QLineEdit LEarg1;
    public QLineEdit LEarg2;
    public QLineEdit LEarg3;
    public QPushButton fBtnImport;
    public QLabel LResPath;
    public QLineEdit LEResPath;
    public QPushButton fBtnResPath;
    public QLineEdit LEROUiters;
    
    // string Lists for filling combo boxes
    public static final List<String> TOPOLOGIES = Arrays.asList("Mesh", "Torus", "Circulant", "CirculantOpt");
    public static final List<String> ALGORITHMS = Arrays.asList("Dijkstra", "PO", "ROU");
    
    // for integers validator
    public QRegExpValidator intValidator;
    public QRegExp rx;
    
    /**
     * Function initializes GUI elements,
     * defines their settings
     * and arranges them.
     */
    @Override
    public void setupUi(QMainWindow FormMain) {
    	// main form settings
        FormMain.setObjectName("FormMain");
        FormMain.resize(new QSize(520, 600).expandedTo(FormMain.minimumSizeHint()));
        
        // initializing central widget
        this.centralwidget = new QWidget(FormMain);
        this.centralwidget.setObjectName("centralwidget");
        
        // initializing main vertical layout
        this.verticalLayout = new QVBoxLayout(this.centralwidget);
        this.verticalLayout.setObjectName("verticalLayout");
        
        // initializing horizontal layout which will contain 
        // topology and algorithm combo boxes
        // and arguments line edits
        this.argsLayout = new QHBoxLayout();
        this.argsLayout.setObjectName("argsLayout");
        
        // configuring topology combo box
        this.CBTopology = new QComboBox();
        this.CBTopology.setObjectName("CBTopology");       
        this.CBTopology.addItems(TOPOLOGIES);
        this.argsLayout.addWidget(this.CBTopology);
        
        // configuring algorithm combo box
        this.CBAlgorithm = new QComboBox();
        this.CBAlgorithm.setObjectName("CBAlgorithm");       
        this.CBAlgorithm.addItems(ALGORITHMS);
        this.argsLayout.addWidget(this.CBAlgorithm);
        
        // configuring validator
        this.rx = new QRegExp("[0-9]*");
        this.intValidator = new QRegExpValidator(rx);
        
        // configuring rou iterations line edit
        this.LEROUiters = new QLineEdit();
        this.LEROUiters.setObjectName("LEROUiters");
        this.LEROUiters.setValidator(this.intValidator);
        this.argsLayout.addWidget(this.LEROUiters);       
        
        // configuring first argument line edit
        this.LEarg1 = new QLineEdit();
        this.LEarg1.setObjectName("LEarg1");
        this.LEarg1.setValidator(this.intValidator);
        this.argsLayout.addWidget(this.LEarg1);
        
        // configuring second argument line edit
        this.LEarg2 = new QLineEdit();
        this.LEarg2.setObjectName("LEarg2");
        this.LEarg2.setValidator(this.intValidator);
        this.argsLayout.addWidget(this.LEarg2);
        
        // configuring third argument line edit
        this.LEarg3 = new QLineEdit();
        this.LEarg3.setObjectName("LEarg3");
        this.LEarg3.setValidator(this.intValidator);
        this.argsLayout.addWidget(this.LEarg3);
        
        // adding layout to main vertical layout
        this.verticalLayout.addLayout(this.argsLayout);
        
        // configuring text edit for simulation reports
        this.TELog = new QTextEdit(this.centralwidget);
        this.TELog.setObjectName("TELog");     
        QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Preferred);
        sizePolicy.setHorizontalStretch((byte) 0);
        sizePolicy.setVerticalStretch((byte) 0);
        sizePolicy.setHeightForWidth(this.TELog.sizePolicy().hasHeightForWidth());
        this.TELog.setSizePolicy(sizePolicy);
        this.TELog.setFrameShape(QFrame.Shape.Box);
        this.TELog.setFrameShadow(QFrame.Shadow.Plain);
        this.TELog.setReadOnly(true);
        this.TELog.setTabStopWidth(24);
        this.TELog.setAcceptRichText(false);
        this.TELog.setTextInteractionFlags(Qt.TextInteractionFlag.createQFlags(Qt.TextInteractionFlag.TextSelectableByMouse));
        this.verticalLayout.addWidget(this.TELog);
        
        // initializing horizontal layout which will contain
        // run, pause, resume and stop buttons
        this.horizontalLayout = new QHBoxLayout();
        this.horizontalLayout.setObjectName("horizontalLayout");
        
        // configuring run button
        this.fBtnRun = new QPushButton(this.centralwidget);
        this.fBtnRun.setObjectName("fBtnRun");
        this.fBtnRun.setEnabled(true);
        this.fBtnRun.setCheckable(false);
        this.horizontalLayout.addWidget(this.fBtnRun);
        
        // configuring pause button
        this.fBtnPause = new QPushButton(this.centralwidget);
        this.fBtnPause.setObjectName("fBtnPause");
        this.fBtnPause.setEnabled(false);
        this.horizontalLayout.addWidget(this.fBtnPause);
        
        // configuring resume button
        this.fBtnResume = new QPushButton(this.centralwidget);
        this.fBtnResume.setObjectName("fBtnResume");
        this.fBtnResume.setEnabled(false);
        this.horizontalLayout.addWidget(this.fBtnResume);
        
        // configuring stop button
        this.fBtnStop = new QPushButton(this.centralwidget);
        this.fBtnStop.setObjectName("fBtnStop");
        this.fBtnStop.setEnabled(false);
        this.horizontalLayout.addWidget(this.fBtnStop);

        // adding horizontal layout to main vertical layout
        this.verticalLayout.addLayout(this.horizontalLayout);
        
        // configuring progress bar
        this.PBProgress = new QProgressBar(this.centralwidget);
        this.PBProgress.setObjectName("PBProgress");
        this.PBProgress.setValue(0);
        this.PBProgress.setAlignment(Qt.AlignmentFlag.createQFlags(Qt.AlignmentFlag.AlignCenter));
        this.verticalLayout.addWidget(this.PBProgress);
        
        // initializing horizontal layout which will contain
        // import and export buttons
        this.imExLayout = new QHBoxLayout();
        this.imExLayout.setObjectName("imExLayout");
        
        // configuring import button
        this.fBtnImport = new QPushButton(this.centralwidget);
        this.fBtnImport.setObjectName("fBtnImport");
        this.imExLayout.addWidget(this.fBtnImport);
        
        // configuring export button
        this.fBtnExport = new QPushButton(this.centralwidget);
        this.fBtnExport.setObjectName("fBtnExport");
        this.fBtnExport.setEnabled(false);
        this.imExLayout.addWidget(this.fBtnExport);
        
        // adding horizontal layout to main vertical layout
        this.verticalLayout.addLayout(this.imExLayout);
        
        // initializing horizontal layout which will contain
        // result path label, line edit and change button
        this.resPathLayout = new QHBoxLayout();
        this.resPathLayout.setObjectName("resPathLayout");
        
        // configuring result path label
        this.LResPath = new QLabel();
        this.LResPath.setObjectName("LResPath");
        this.LResPath.setText("Directory for configuration, simulation and results files:");
        this.resPathLayout.addWidget(this.LResPath);
        
        // configuring result path line edit
        this.LEResPath = new QLineEdit();
        this.LEResPath.setObjectName("LEResPath");
        this.LEResPath.setReadOnly(true);
        this.resPathLayout.addWidget(this.LEResPath);
        
        // configuring change result path button
        this.fBtnResPath = new QPushButton();
        this.fBtnResPath.setObjectName("fBtnResPath");
        this.resPathLayout.addWidget(this.fBtnResPath);
        
        // adding horizontal layout to main vertical layout
        this.verticalLayout.addLayout(this.resPathLayout);
        
        // setting central widget
        FormMain.setCentralWidget(this.centralwidget);
        
        // configuring menu bar
        this.menubar = new QMenuBar(FormMain);
        this.menubar.setObjectName("menubar");
        this.menubar.setGeometry(new QRect(0, 0, 520, 22));
        FormMain.setMenuBar(this.menubar);
        
        // configuring status bar
        this.statusbar = new QStatusBar(FormMain);
        this.statusbar.setObjectName("statusbar");
        FormMain.setStatusBar(this.statusbar);
        
        // retranslating some elements properties
        this.retranslateUi(FormMain);
        
        // connecting elements and corresponding actions
        FormMain.connectSlotsByName();
    }

    /**
     * Function retranslates 
     * some GUI elements properties
     * @param FormMain
     */
    void retranslateUi(QMainWindow FormMain) {
        FormMain.setWindowTitle(QCoreApplication.translate("FormMain", "OCNS", null));
        this.TELog.setDocumentTitle("");
        this.TELog.setHtml(QCoreApplication.translate("FormMain", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\np, li { white-space: pre-wrap; }\n</style></head><body style=\" font-family:'MS Shell Dlg 2'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\">\t</p>\n<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"></p></body></html>", null));
        this.fBtnRun.setText(QCoreApplication.translate("FormMain", "Run", null));
        this.fBtnPause.setText(QCoreApplication.translate("FormMain", "Pause", null));
        this.fBtnResume.setText(QCoreApplication.translate("FormMain", "Resume", null));
        this.fBtnStop.setText(QCoreApplication.translate("FormMain", "Stop", null));
        this.fBtnExport.setText(QCoreApplication.translate("FormMain", "Save results", null));
        this.fBtnImport.setText(QCoreApplication.translate("FormMain", "Open config file", null));
        this.fBtnResPath.setText(QCoreApplication.translate("FormMain", "Change", null));
    }
}

