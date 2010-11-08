
/*
 * GUI.java
 *
 * Created on 8-ott-2010, 10.28.25
 */
package jurassic;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.TridentConfig.PulseSource;

/**
 *
 * @author Michele Marcon
 *
 * Tyrannosaurus timeline view (il capo)
 * Triceratops wiki editor (faccia-pagine)
 * Brontosaurus issue tracker (bronto-lo)
 * Stegosaurus backup manager (copie-scaglie) - graphic analyzer (grafici-scaglie)
 * Pterodactyl roadmap planner (vola-futuro)
 *
 */
public class GUI extends javax.swing.JFrame {

    public static final String version = "0.2.4";
    String museum;
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
    String[] BACKUP = new String[]{"cmd", "/c", "copy", "*.fossil", "p:\\repositoryJurassic"};
    Process webserver;
    ArrayList<Dinosaur> list = new ArrayList<Dinosaur>();

    public GUI() {
        initComponents();

        setIconImage(Toolkit.getDefaultToolkit().getImage(jurassic.GUI.class.getResource("JurassicIcon.png")));
        browseFossil();

        for (Dinosaur d : list) {
            jComboBox1.addItem(d.name);
        }

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
                String fossil = new BufferedReader(isr).readLine().substring(14);
                /*try {
                process.waitFor();
                } catch (final InterruptedException e) { }*/
                System.out.println(fossil);
                list.add(new Dinosaur(new File(fossil).getName(), source.getAbsolutePath()));
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
            Tyrannosaurus t = new Tyrannosaurus(list.get(jComboBox1.getSelectedIndex()).name, this);
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
        setTitle("Jurassic " + version + " - " + museum);
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButton8 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Commit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jEditorPane1);

        jButton4.setText("Backup");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton8.setText("Timeline");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton5.setText("New");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("HTML");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("View"));

        jButton3.setText("Status");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setText("Diff");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(270, 270, 270)
                                .addComponent(jButton4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(99, 99, 99))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton7))
                            .addComponent(jButton8))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton2)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String comment = JOptionPane.showInputDialog(this, "Please input a commit comment", "Jurassic", JOptionPane.QUESTION_MESSAGE);
        if (comment == null) {
            return;
        }
        COMMIT[3] = comment;
        exec(COMMIT);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser fc = new JFileChooser(museum);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setMultiSelectionEnabled(true);
        if (fc.showDialog(this, "Set the sources to add to the repository") == JFileChooser.APPROVE_OPTION) {
            File files[] = fc.getSelectedFiles();
            String[] ADD = new String[2 + files.length];
            ADD[0] = "fossil";
            ADD[1] = "add";
            for (int i = 0; i < files.length; i++) {
                ADD[i + 2] = files[i].getAbsolutePath();
            }
            exec(ADD);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        exec(STATUS);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        exec(BACKUP);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        setMuseum(list.get(jComboBox1.getSelectedIndex()).source);
        exec(STATUS);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        exec(DIFF);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        checkEvolution();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String name = JOptionPane.showInputDialog(this, "Please input a name for the repository", "Jurassic", JOptionPane.QUESTION_MESSAGE);
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
        if (fc.showDialog(this, "Set the source for the repository") == JFileChooser.APPROVE_OPTION) {
            OPEN[2] = System.getProperty("user.dir") + System.getProperty("file.separator") + NEW[2];
        }
        Dinosaur d = new Dinosaur(NEW[2], fc.getSelectedFile().getAbsolutePath());
        setMuseum(fc.getSelectedFile().getAbsolutePath());
        list.add(d);
        jComboBox1.addItem(d.name);
        exec(OPEN);
        jComboBox1.setSelectedItem(d.name);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080/" + jComboBox1.getSelectedItem()));
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

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
                if (args.length > 0) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    System.setProperty("sun.awt.noerasebackground", "true");
                    SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
                }
                new GUI().setVisible(true);
            }
        });
    }

    class Dinosaur {

        String name;
        String fossil;
        String source;

        Dinosaur(String fossil, String source) {
            try {
                this.name = fossil.substring(0, fossil.length() - 7);
                this.fossil = fossil;
                this.source = source;
            } catch (java.lang.StringIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(GUI.this, "Fossil files must have .fossil extension!", "Jurassic", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*ArrayList<Arc> arcs=new ArrayList<Arc>();
    class Arc {
    EvolutionStep from;
    EvolutionStep to;
    Arc(String from, String to){
    this.from=new EvolutionStep(from,null,null,null);
    this.to=new EvolutionStep(to,null,null,null);
    }
    }*/
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
            return this.date.compareTo(((EvolutionStep) o).date);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
