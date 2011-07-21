/*
 * Copyright (c) 2010-2011 Jurassic Michele Marcon. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Jurassic Michele Marcon nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jurassic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.SwingUtilities;
import jurassic.transcoded.*;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandButtonPanel;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlack2007Skin;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;
import org.pushingpixels.substance.api.skin.OfficeSilver2007Skin;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.TridentConfig.PulseSource;

/**
 *
 * @author mmarcon
 */
public class GUI2 extends JRibbonFrame {

    public static final String version = "0.3.3";
    String museum;
    Dinosaur currentDino = new Dinosaur(".fossil", ".fossil","?");
    String repository = "prova.fossil";
    public String currentCheckout = null;
    String[] STARTWEBSERVER = new String[]{"fossil", "server", null};
    String[] NEW = new String[]{"fossil", "new", repository};
    String[] OPEN = new String[]{"fossil", "open", repository};
    String[] CLOSE = new String[]{"fossil", "close", repository};
    //String[] ADD = new String[]{"fossil", "add", null};
    String[] STATUS = new String[]{"fossil", "status"};
    String[] LEAVES = new String[]{"fossil", "leaves"};
    String[] INFO = new String[]{"fossil", "info", null};
    String[] DIFF = new String[]{"fossil", "diff"};
    String[] COMMIT = new String[]{"fossil", "commit", "-m", null};
    String[] SPECIALCOMMIT = new String[]{"fossil", "commit", "-m", null, "--branch", null, "--bgcolor", null, ""};
    String[] BACKUP = new String[]{"cmd", "/c", "copy", "*.fossil", "p:\\repositoryJurassic"};
    Process webserver;
    ArrayList<Dinosaur> list = new ArrayList<Dinosaur>();
    javax.swing.JEditorPane jEditorPane1 = new JEditorPane();
    javax.swing.JComboBox jComboBox1 = new JComboBox();

    GUI2() {
        browseFossil();

        jComboBox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                currentDino.name = list.get(jComboBox1.getSelectedIndex()).name;
                currentDino.currentBranch = list.get(jComboBox1.getSelectedIndex()).currentBranch;
                setMuseum(list.get(jComboBox1.getSelectedIndex()).source);
                exec(STATUS);
            }
        });
        /*for (Dinosaur d : list) {
        jComboBox1.addItem(d.name);
        }*/

        setMuseum(System.getProperty("user.dir"));
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    webserver = new ProcessBuilder(STARTWEBSERVER).start();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                webserver.destroy();
            }
        }));
    }

    private void browseFossil() {
        File f = new File(System.getProperty("user.dir"));
        huntDinosaur(f.getParentFile());

        for (File source : dinos) {
            try {
                System.out.print(source + " - ");
                ProcessBuilder pb = new ProcessBuilder(STATUS);
                pb.directory(source);
                Process process = pb.start();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                //String fossil = new BufferedReader(isr).readLine().substring(14);
                BufferedReader reader=new BufferedReader(isr);
                String rdLine = null;
                String fossil=null,currentBranch=null;
                while ((rdLine = reader.readLine()) != null) {
                if (rdLine.startsWith("repository:")) {
                    Scanner sc = new Scanner(rdLine);
                    sc.next();
                    fossil = sc.next();
                }
                if (rdLine.startsWith("tags:")) {
                    Scanner sc = new Scanner(rdLine);
                    sc.next();
                    currentBranch = sc.next();
                }
            }
                /*try {
                process.waitFor();
                } catch (final InterruptedException e) { }*/
                System.out.println(fossil);
                list.add(new Dinosaur(new File(fossil).getName(), source.getAbsolutePath(),currentBranch));
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException eg) {
                System.err.println("Jurassic cannot find some of your repositories.");
                JOptionPane.showMessageDialog(this, "Jurassic cannot find some of your repositories.", "Jurassic", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

        }
    }
    ArrayList<File> dinos = new ArrayList<File>();

    private void huntDinosaur(File park) {
        try {
            for (File f : park.listFiles()) {
                if (f.getName().equals("_FOSSIL_")) {
                    dinos.add(park);
                }
                if (f.isDirectory()) {
                    huntDinosaur(f);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("You must run Jurassic from the same folder where your .fossil are.");
            JOptionPane.showMessageDialog(this, "You must run Jurassic from the same folder where your .fossil are.", "Jurassic", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    ArrayList<EvolutionStep> evolution = new ArrayList<EvolutionStep>();

    private void checkEvolution() {
        try {
            evolution.clear();
            System.out.println("Check evolution");
            ProcessBuilder pb = new ProcessBuilder(STATUS);
            pb.directory(new File(museum));
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String rdLine = null;
            while ((rdLine = reader.readLine()) != null) {
                if (rdLine.startsWith("checkout:")) {
                    Scanner sc = new Scanner(rdLine);
                    sc.next();
                    currentCheckout = sc.next();
                }
            }
            pb = new ProcessBuilder(LEAVES);
            pb.directory(new File(museum));
            process = pb.start();
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            rdLine = null;
            EvolutionStep es = null;
            //arcs.clear();
            while ((rdLine = reader.readLine()) != null) {
                if ((rdLine.charAt(9) == '[') && (rdLine.charAt(20) == ']')) {
                    inspectEvolution(rdLine.substring(10, 20));
                }
                /*if (rdLine.startsWith("checkout:")) {
                Scanner sc = new Scanner(rdLine);
                sc.next();
                inspectEvolution(sc.next());
                //es=new EvolutionStep(sc.next(), sc.next()+sc.next(),null,null);
                }*/
                /*if (rdLine.startsWith("parent:")){
                Scanner sc=new Scanner(rdLine);
                sc.next();
                //navigate.add(sc.next());
                inspectEvolution(sc.next());
                }*/
                //evolution.add(es);
            }
            Tyrannosaurus t = new Tyrannosaurus(currentDino.name,currentDino.currentBranch, this);
            t.setModel(evolution);
            t.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private EvolutionStep inspectEvolution(String sha) throws IOException {
        INFO[2] = sha;
        ProcessBuilder pb = new ProcessBuilder(INFO);
        pb.directory(new File(museum));
        Process process = pb.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        String rdLine = null;
        EvolutionStep es = null;
        while ((rdLine = reader.readLine()) != null) {
            if (rdLine.startsWith("uuid:")) {
                es = new EvolutionStep(rdLine.substring(14, 54), rdLine.substring(55, 74), null, null);
            }
            if (rdLine.startsWith("parent:")) {
                Scanner sc = new Scanner(rdLine);
                sc.next();
                String parent = sc.next();
                //arcs.add(new Arc(sha,parent));
                System.out.println("New arc! " + sha + " " + parent);
                es.parents.add(inspectEvolution(parent));
            }
            if (rdLine.startsWith("tags:")) {
                es.tags = rdLine.substring(14);
            }
            if (rdLine.startsWith("comment:")) {
                es.comment = rdLine.substring(14);
            }
        }
        System.out.println(es.date + " " + es.sha + " " + es.tags + " " + es.comment);
        if (!evolution.contains(es)) {
            evolution.add(es);
        }
        return es;
    }

    private void setMuseum(String museum) {
        this.museum = museum;
        setTitle("Jurassic " + version + " - " + museum+"@"+currentDino.currentBranch);
        STARTWEBSERVER[2] = museum;
    }

    public void exec(String args[]) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.directory(new File(museum));
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            BufferedReader brerr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            jEditorPane1.read(is, null);
            //jEditorPane1.append("\r\nOutput of running "+Arrays.toString(args)+" is:\r\n");
/*            while ((line = br.readLine()) != null) {
            jEditorPane1.read(is,null);
            }*/
            String err = "";
            while ((line = brerr.readLine()) != null) {
                err += (line + "\r\n");
                if (!err.equals("")) {
                    JOptionPane.showConfirmDialog(this, err, "Jurassic", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TridentConfig.getInstance().setPulseSource(new PulseSource() {

                    public void waitUntilNextPulse() {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    SubstanceLookAndFeel.setSkin(new OfficeBlack2007Skin());
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                final GUI2 c = new GUI2();
                c.add(new JScrollPane(c.jEditorPane1));
                c.configureRibbon();
                c.configureApplicationMenu();
                c.configureTaskBar();
                c.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
                Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                c.setPreferredSize(new Dimension(r.width, r.height / 2));
                c.setMinimumSize(new Dimension(r.width / 10, r.height / 2));
                c.pack();
                c.setLocation(r.x, r.y);
                c.setVisible(true);
                c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            }
        });
    }

    protected void configureTaskBar() {
        // taskbar components
        JCommandButton taskbarButtonPaste = new JCommandButton("",
                new document_save());
        taskbarButtonPaste.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
        taskbarButtonPaste.addActionListener(new CommitActionListener());
        taskbarButtonPaste.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu();
            }
        });
        taskbarButtonPaste.setActionRichTooltip(new RichTooltip("Commit", "Performs a fossil commit"));
        taskbarButtonPaste.setActionKeyTip("1");
        this.getRibbon().addTaskbarComponent(taskbarButtonPaste);
    }

    protected void configureApplicationMenu() {
        RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(
                new document_new(),
                "New",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = JOptionPane.showInputDialog(GUI2.this, "Please input a name for the repository", "Jurassic", JOptionPane.QUESTION_MESSAGE);
                        if (name == null) {
                            return;
                        }
                        if (name.endsWith(".fossil")) {
                            NEW[2] = name;
                        } else {
                            NEW[2] = name + ".fossil";
                        }
                        setMuseum(System.getProperty("user.dir"));
                        exec(NEW);
                        JFileChooser fc = new JFileChooser(museum);
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        if (fc.showDialog(GUI2.this, "Set the source for the repository") == JFileChooser.APPROVE_OPTION) {
                            OPEN[2] = System.getProperty("user.dir") + System.getProperty("file.separator") + NEW[2];
                        }
                        Dinosaur d = new Dinosaur(NEW[2], fc.getSelectedFile().getAbsolutePath(),"trunk");
                        setMuseum(fc.getSelectedFile().getAbsolutePath());
                        list.add(d);
                        jComboBox1.addItem(d.name);
                        exec(OPEN);
                        jComboBox1.setSelectedItem(d.name);
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntryNew.setActionKeyTip("N");

        RibbonApplicationMenuEntryPrimary amAdd = new RibbonApplicationMenuEntryPrimary(
                new document_new(),
                "Add",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser(museum);
                        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        fc.setMultiSelectionEnabled(true);
                        if (fc.showDialog(GUI2.this, "Set the sources to add to the repository") == JFileChooser.APPROVE_OPTION) {
                            File files[] = fc.getSelectedFiles();
                            String[] ADD = new String[2 + files.length];
                            ADD[0] = "fossil";
                            ADD[1] = "add";
                            for (int i = 0; i < files.length; i++) {
                                ADD[i + 2] = files[i].getAbsolutePath();
                            }
                            exec(ADD);
                        }
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntryNew.setActionKeyTip("A");
        RibbonApplicationMenuEntryPrimary amDel = new RibbonApplicationMenuEntryPrimary(
                new document_new(),
                "Delete",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser(museum);
                        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        fc.setMultiSelectionEnabled(true);
                        if (fc.showDialog(GUI2.this, "Select files to delete") == JFileChooser.APPROVE_OPTION) {
                            File files[] = fc.getSelectedFiles();
                            String[] DELETE = new String[2 + files.length];
                            DELETE[0] = "fossil";
                            DELETE[1] = "rm";
                            for (int i = 0; i < files.length; i++) {
                                DELETE[i + 2] = files[i].getAbsolutePath();
                            }
                            exec(DELETE);
                        }
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntryNew.setActionKeyTip("D");        
        RibbonApplicationMenuEntryPrimary amBack = new RibbonApplicationMenuEntryPrimary(
                new document_save(),
                "Backup",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //non implementato!
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntryNew.setActionKeyTip("B");
        amBack.setEnabled(false);
        RibbonApplicationMenuEntryPrimary amCommit = new RibbonApplicationMenuEntryPrimary(
                new document_save(),
                "Commit",
                new CommitActionListener(), CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        amCommit.setActionKeyTip("C");
        amCommit.setPopupKeyTip("S");
        RibbonApplicationMenuEntrySecondary amCommitDefault = new RibbonApplicationMenuEntrySecondary(
                new document_save(), "Standard commit", new CommitActionListener(),
                CommandButtonKind.ACTION_ONLY);
        amCommitDefault.setDescriptionText("Commit to current branch");
        amCommitDefault.setActionKeyTip("D");
        RibbonApplicationMenuEntrySecondary amCommitSpecial = new RibbonApplicationMenuEntrySecondary(
                new document_save_as(), "Special commit", new SpecialCommitActionListener(),
                CommandButtonKind.ACTION_ONLY);
        amCommitSpecial.setDescriptionText("Commit to different branch");
        amCommitSpecial.setActionKeyTip("S");
        amCommit.addSecondaryMenuGroup("Type of commit",
                amCommitDefault, amCommitSpecial);
        RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(
                new system_log_out(), "Exit", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }, CommandButtonKind.ACTION_ONLY);
        amEntryExit.setActionKeyTip("X");

        RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
        applicationMenu.addMenuEntry(amEntryNew);
        applicationMenu.addMenuEntry(amAdd);
        applicationMenu.addMenuEntry(amDel);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amBack);
        applicationMenu.addMenuEntry(amCommit);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryExit);

        RibbonApplicationMenuEntryFooter amFooterProps = new RibbonApplicationMenuEntryFooter(
                null, "Options",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Invoked Options");
                    }
                });
        RibbonApplicationMenuEntryFooter amFooterExit = new RibbonApplicationMenuEntryFooter(
                new system_log_out(), "Quit", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        amFooterProps.setEnabled(false);
        setApplicationIcon(new Fossil_SCM_logo());
        applicationMenu.addFooterEntry(amFooterProps);
        applicationMenu.addFooterEntry(amFooterExit);
        this.getRibbon().setApplicationMenu(applicationMenu);
        this.getRibbon().setApplicationMenuKeyTip("F");
        applicationMenu.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {

            @Override
            public void menuEntryActivated(JPanel targetPanel) {
                targetPanel.removeAll();
                JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
                        CommandButtonDisplayState.MEDIUM);
                String groupName = "Repository";
                openHistoryPanel.addButtonGroup(groupName);

                //mf.setLocale(currLocale);
                for (final Dinosaur d : list) {
                    JCommandButton historyButton = new JCommandButton(
                            d.name,
                            new Fossil_SCM_logo());
                    historyButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            currentDino = d;
                            //setMuseum(d.source);
                            //exec(STATUS);
                            jComboBox1.setSelectedItem(d.name);
                        }
                    });
                    historyButton.setHorizontalAlignment(SwingUtilities.LEFT);
                    openHistoryPanel.addButtonToLastGroup(historyButton);
                }
                openHistoryPanel.setMaxButtonColumns(2);
                targetPanel.setLayout(new BorderLayout());
                targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
            }
        });
        //if (currentDino == null) {
        try {
            currentDino = list.get(0);
        } catch (Exception e) {
            System.err.println("You must run Jurassic from the same folder where your .fossil are.");
            JOptionPane.showMessageDialog(this, "You must run Jurassic from the same folder where your .fossil are.", "Jurassic", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        //}
    }

    private void configureRibbon() {

        this.getRibbon().addTask(configureHTMLTask());
        this.getRibbon().addTask(configureTicketTask());
        this.getRibbon().addTask(configureWikiTask());
    }

    private RibbonTask configureHTMLTask() {
        JRibbonBand homeBand = new JRibbonBand("HTML", new applications_internet());
        homeBand.setExpandButtonKeyTip("FO");
        List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(homeBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(homeBand.getControlPanel()));
        homeBand.setResizePolicies(resizePolicies);
        JCommandButton mainButton = new JCommandButton("Home", new applications_internet());
        mainButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("index");
            }
        });
        homeBand.addCommandButton(mainButton, RibbonElementPriority.TOP);
        JCommandButton fileButton = new JCommandButton("Files", new applications_internet());
        fileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("dir?ci=tip");
            }
        });
        fileButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Tip", "dir?ci=tip", "Trunk", "dir?ci=trunk", "All", "dir?ci=all");
            }
        });
        fileButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        homeBand.addCommandButton(fileButton, RibbonElementPriority.MEDIUM);
        JCommandButton leavesButton = new JCommandButton("Leaves", new applications_internet());
        leavesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("leaves");
            }
        });
        leavesButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Open", "leaves", "Closed", "leaves?closed", "All", "leaves?all");
            }
        });
        leavesButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        homeBand.addCommandButton(leavesButton, RibbonElementPriority.MEDIUM);
        JCommandButton branchButton = new JCommandButton("Branches", new applications_internet());
        branchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("brlist");
            }
        });
        branchButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Open", "brlist", "Closed", "brlist?closed", "Timeline", "brtimeline");
            }
        });
        branchButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        homeBand.addCommandButton(branchButton, RibbonElementPriority.MEDIUM);

        JRibbonBand statusBand = new JRibbonBand("Basic", null);
        statusBand.setResizePolicies(resizePolicies);
        JCommandButton statusButton = new JCommandButton("Status", null);
        statusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exec(STATUS);
            }
        });
        JCommandButton diffsButton = new JCommandButton("Diffs", null);
        diffsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exec(DIFF);
            }
        });
        JCommandButton tyranButton = new JCommandButton("Timeline app", null);
        tyranButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkEvolution();
            }
        });
        statusBand.addCommandButton(statusButton, RibbonElementPriority.TOP);
        statusBand.addCommandButton(diffsButton, RibbonElementPriority.TOP);
        statusBand.addCommandButton(tyranButton, RibbonElementPriority.TOP);

        JFlowRibbonBand repoBand = new JFlowRibbonBand("Repositories", null);
        repoBand.setResizePolicies(resizePolicies);
        for (Dinosaur d : list) {
            jComboBox1.addItem(d.name);
        }
        JRibbonComponent repoComboWrapper = new JRibbonComponent(jComboBox1);
        repoComboWrapper.setKeyTip("SF");
        repoBand.addFlowComponent(repoComboWrapper);
        //statusBand.add(jComboBox1, RibbonElementPriority.TOP);
        //statusBand.addCommandButton(diffsButton, RibbonElementPriority.TOP);

        RibbonTask htmlTask = new RibbonTask("View", homeBand, statusBand, repoBand);
        //quickStylesBand, fontBand, documentBand, findBand);
        htmlTask.setKeyTip("P");
        return htmlTask;
    }

    private RibbonTask configureTicketTask() {
        JRibbonBand editBand = new JRibbonBand("Edit", null);
        JCommandButton newButton = new JCommandButton("New ticket", new text_html());
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("tktnew");
            }
        });
        editBand.addCommandButton(newButton, RibbonElementPriority.TOP);

        JRibbonBand viewBand = new JRibbonBand("View", null, new ExpandActionListener());
        JCommandButton statusButton = new JCommandButton("All tickets", new applications_internet());
        statusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("rptview?rn=1");
            }
        });
        statusButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        statusButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("All", "rptview?rn=1", "Raw", "rptview?tablist=1&rn=1");
            }
        });
        JCommandButton diffsButton = new JCommandButton("Timeline", new applications_internet());
        diffsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("timeline?y=t");
            }
        });
        diffsButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        diffsButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Latest 20", "timeline?n=20&y=t", "Latest 200", "timeline?n=200&y=t", "Latest 1000", "timeline?n=1000&y=t");
            }
        });
        List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        /*resizePolicies.add(new CoreRibbonResizePolicies.Mirror(editBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(editBand.getControlPanel()));*/
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(viewBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(viewBand.getControlPanel()));
        editBand.setResizePolicies(resizePolicies);
        viewBand.setResizePolicies(resizePolicies);
        viewBand.addCommandButton(statusButton, RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(diffsButton, RibbonElementPriority.MEDIUM);

        RibbonTask ticketTask = new RibbonTask("Ticket", editBand, viewBand);
        ticketTask.setKeyTip("T");
        return ticketTask;
    }

    private RibbonTask configureWikiTask() {
        JRibbonBand editBand = new JRibbonBand("Edit", null);
        JCommandButton newButton = new JCommandButton("New wiki", new text_html());
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("wikinew");
            }
        });
        editBand.addCommandButton(newButton, RibbonElementPriority.TOP);

        JRibbonBand viewBand = new JRibbonBand("View", null);
        JCommandButton statusButton = new JCommandButton("All wiki", new applications_internet());
        statusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("wcontent");
            }
        });
        /*statusButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        statusButton.setPopupCallback(new PopupPanelCallback() {

        @Override
        public JPopupPanel getPopupPanel(JCommandButton commandButton) {
        return new SamplePopupMenu("All","http://127.0.0.1:8080/NetmanPlus/rptview?rn=1","Raw","http://127.0.0.1:8080/NetmanPlus/rptview?tablist=1&rn=1");
        }
        });*/
        JCommandButton diffsButton = new JCommandButton("Timeline", new applications_internet());
        diffsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                webLink("timeline?y=w");
            }
        });
        diffsButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        diffsButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Latest 20", "timeline?n=20&y=w", "Latest 200", "timeline?n=200&y=w", "Latest 1000", "timeline?n=1000&y=w");
            }
        });
        List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        /*resizePolicies.add(new CoreRibbonResizePolicies.Mirror(editBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(editBand.getControlPanel()));*/
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(viewBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(viewBand.getControlPanel()));
        editBand.setResizePolicies(resizePolicies);
        viewBand.setResizePolicies(resizePolicies);
        viewBand.addCommandButton(statusButton, RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(diffsButton, RibbonElementPriority.MEDIUM);

        RibbonTask wikiTask = new RibbonTask("Wiki", editBand, viewBand);
        wikiTask.setKeyTip("W");
        return wikiTask;
    }

    private void webLink(String link) {
        try {
            Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080/" + currentDino.name + "/" + link));
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ExpandActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(GUI2.this,
                    "Expand button clicked");
        }
    }

    private class CommitActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String comment = JOptionPane.showInputDialog(GUI2.this, "Please input a commit comment", "Jurassic", JOptionPane.QUESTION_MESSAGE);
            if (comment == null) {
                return;
            }
            COMMIT[3] = comment;
            exec(COMMIT);
        }
    }

    private class SpecialCommitActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String branchName = JOptionPane.showInputDialog(GUI2.this, "Please input new branch name", "Jurassic", JOptionPane.QUESTION_MESSAGE);
            if (branchName == null) {
                return;
            }
            String comment = JOptionPane.showInputDialog(GUI2.this, "Please input a commit comment", "Jurassic", JOptionPane.QUESTION_MESSAGE);
            if (comment == null) {
                return;
            }
            Color c = JColorChooser.showDialog(GUI2.this, "Please choose a color for the new branch", Color.yellow);
            if (c == null) {
                return;
            }
            if (JOptionPane.showConfirmDialog(GUI2.this, "Do you want the branch to be private?", "Jurassic", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
                SPECIALCOMMIT[8]="--private";
            SPECIALCOMMIT[3] = comment;
            SPECIALCOMMIT[5] = branchName;
            SPECIALCOMMIT[7] = "#" + Integer.toHexString(c.getRGB()).substring(2);
            exec(SPECIALCOMMIT);
        }
    }

    private class SamplePopupMenu extends JCommandPopupMenu {

        public SamplePopupMenu(final String... commands) {

            for (int i = 0; i < commands.length; i += 2) {
                JCommandMenuButton menuButton1 = new JCommandMenuButton(commands[i], null);
                final int z = i + 1;
                menuButton1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        webLink(commands[z]);
                    }
                });
                menuButton1.setActionKeyTip((i / 2 + 1) + "");
                this.addMenuButton(menuButton1);
            }


            /*
            JCommandMenuButton menuButton2 = new JCommandMenuButton("Trunk", null);
            menuButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            System.out.println("Test menu item 2 activated");
            }
            });
            menuButton2.setActionKeyTip("2");
            this.addMenuButton(menuButton2);

            this.addMenuSeparator();
            JCommandMenuButton menuButton3 = new JCommandMenuButton("All", null);
            menuButton3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            System.out.println("Test menu item 3 activated");
            }
            });
            menuButton3.setActionKeyTip("3");
            this.addMenuButton(menuButton3);

             */
        }
    }

    class Dinosaur {

        String name;
        String fossil;
        String source;
        String currentBranch;

        Dinosaur(String fossil, String source, String currentBranch) {
            try {
                this.name = fossil.substring(0, fossil.length() - 7);
                this.fossil = fossil;
                this.source = source;
                this.currentBranch=currentBranch;
            } catch (java.lang.StringIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(GUI2.this, "Fossil files must have .fossil extension!", "Jurassic", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class EvolutionStep implements Comparable {

        String sha;
        String date;
        String tags;
        String comment;
        ArrayList<EvolutionStep> parents = new ArrayList<EvolutionStep>();

        EvolutionStep(String sha, String date, String tags, String comment) {
            this.sha = sha;
            this.date = date;
            this.tags = tags;
            this.comment = comment;
        }

        @Override
        public boolean equals(Object es) {
            if (es instanceof EvolutionStep) {
                if (((EvolutionStep) es).sha.length() == (this.sha.length())) {
                    return (((EvolutionStep) es).sha.equals(this.sha));
                }
                return (((EvolutionStep) es).sha.substring(0, 10).equals(this.sha.substring(0, 10)));
            }
            return false;
        }

        public int compareTo(Object o) {
            return ((EvolutionStep) o).date.compareTo(this.date);
        }
    }
}
