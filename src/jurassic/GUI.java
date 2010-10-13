
/*
 * GUI.java
 *
 * Created on 8-ott-2010, 10.28.25
 */
package jurassic;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Michele Marcon
 * http://127.0.0.1:8080/Netmanplus/vdiff?from=b7ea110444&to=trunk&detail=1
 */
public class GUI extends javax.swing.JFrame {

    String museum;
    String repository = "prova.fossil";
    String currentFossil = "src";
    String[] STARTWEBSERVER = new String[]{"fossil", "server", null};
    String[] NEW = new String[]{"fossil", "new", repository};
    String[] OPEN = new String[]{"fossil", "open", repository};
    String[] CLOSE = new String[]{"fossil", "close", repository};
    String[] ADD = new String[]{"fossil", "add", currentFossil};
    String[] STATUS = new String[]{"fossil", "status"};
    String[] DIFF = new String[]{"fossil", "diff"};
    String[] COMMIT = new String[]{"fossil", "commit", "-m", null};
    String[] BRANCH = new String[]{"fossil", "branch", "new", null, null, "-bgcolor", null, "--nosign"};
    String[] UPDATE = new String[]{"fossil", "update", null};
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

    private void browseFossil(){
    File f=new File(System.getProperty("user.dir"));
    huntDinosaur(f.getParentFile());

    for (File source:dinos)
    {
            try {
                System.out.print(source + " - ");
                ProcessBuilder pb = new ProcessBuilder(STATUS);
                pb.directory(source);
                Process process = pb.start();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                String fossil=new BufferedReader(isr).readLine().substring(14);
                System.out.println(fossil);
                list.add(new Dinosaur(new File(fossil).getName(),source.getAbsolutePath()));
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    ArrayList<File> dinos=new ArrayList<File>();

    private void huntDinosaur(File park){
        for (File f:park.listFiles())
    {
        if (f.getName().equals("_FOSSIL_"))
            dinos.add(park);
        if (f.isDirectory())
            huntDinosaur(f);
    }
    }

    private void setMuseum(String museum) {
        this.museum = museum;
        setTitle("Jurassic 0.1.0 - " + museum);
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
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

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

        jButton3.setText("Status");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jEditorPane1);

        jButton4.setText("Backup");
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

        jButton5.setText("Update");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Diff");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("New branch");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addGap(50, 50, 50)
                        .addComponent(jButton5)
                        .addGap(104, 104, 104)
                        .addComponent(jButton4))
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton3)
                    .addComponent(jButton2)
                    .addComponent(jButton7))
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String comment=JOptionPane.showInputDialog(this, "Please input a commit comment", "Jurassic", JOptionPane.QUESTION_MESSAGE);
        if (comment==null)
            return;
        COMMIT[3]=comment;
        exec(COMMIT);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        exec(ADD);
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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String version = JOptionPane.showInputDialog("Change version to:");
        if (version != null) {
            UPDATE[2] = version.trim();
            exec(UPDATE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        exec(DIFF);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String branchName=JOptionPane.showInputDialog(this, "Please input new branch name", "Jurassic", JOptionPane.QUESTION_MESSAGE);
        if (branchName==null)
            return;
        String basis=JOptionPane.showInputDialog(this, "Please input the check-in basis for the new branch", "Jurassic", JOptionPane.QUESTION_MESSAGE);
        if (basis==null)
            return;
        Color c=JColorChooser.showDialog(this, "Please choose a color for the new branch", Color.yellow);
        if (c==null)
            return;
        BRANCH[3]=branchName;
        BRANCH[4]=basis;
        BRANCH[6]="#"+Integer.toHexString(c.getRGB()).substring(2);
        exec(BRANCH);
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    class Dinosaur {

        String name;
        String fossil;
        String source;

        Dinosaur(String fossil, String source) {
            this.name = fossil.substring(0, fossil.length() - 7);
            this.fossil = fossil;
            this.source = source;
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
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
