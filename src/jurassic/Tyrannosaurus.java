/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Tyrannosaurus.java
 *
 * Created on 18-ott-2010, 17.11.45
 */
package jurassic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import jurassic.GUI.EvolutionStep;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;
import org.pushingpixels.trident.swing.SwingRepaintTimeline;

/**
 *
 * @author mmarcon
 */
public class Tyrannosaurus extends javax.swing.JFrame {

    ArrayList<EvolutionStep> list;
    //ArrayList<Arc> arcs;
    String projectName;
    GUI gui;
    String[] BRANCH = new String[]{"fossil", "branch", "new", null, null, "-bgcolor", null, "--nosign"};
    String[] UPDATE = new String[]{"fossil", "update", null};
    String[] MERGE = new String[]{"fossil", "merge", null};
    HashSet<String> tags = new HashSet<String>();

    /** Creates new form Tyrannosaurus */
    public Tyrannosaurus(String projectName, GUI gui) {
        initComponents();
        this.projectName = projectName;
        this.gui = gui;
        setTitle("Jurassic "+GUI.version+" - " + projectName);
        setIconImage(Toolkit.getDefaultToolkit().getImage(jurassic.GUI.class.getResource("JurassicIcon.png")));
        //setIconImage(Toolkit.getDefaultToolkit().getImage(jurassic.GUI.class.getResource("tyrannosaurus-rex-icon.png")));
        jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(120);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(250);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(250);
        jTable1.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel((String) value);
                label.setOpaque(true);
                label.setBackground(table.getBackground());
                label.setForeground(table.getForeground());
                if (row == a) {
                    label.setBackground(Color.GREEN);
                    //label.setForeground(table.getSelectionForeground());
                }
                if (row == b) {
                    label.setBackground(Color.YELLOW);
                    //label.setForeground(table.getSelectionForeground());
                }
                if (row == c) {
                    label.setBackground(Color.RED);
                }
                /*if (hasFocus) {
                label.setBorder(BorderFactory.createLineBorder(Color.RED));
                }*/

                return label;
            }
        });

        ev.setBounds(0, 0, 1024, 768);
        jPanel4.add(ev);
    }
    ArrayList<String> s;
    EvolutionView ev = new EvolutionView();

    public void setModel(ArrayList<EvolutionStep> list) {
        Collections.sort(list);
        this.list = list;
        steps = new EvolvingDNA[list.size()];
            for (int i = 0; i < steps.length; i++) {
                steps[i] = new EvolvingDNA(i);
            }
        for (EvolutionStep s : list) {
            tags.add(s.tags);
        }
        s = new ArrayList<String>(tags);
        c = list.indexOf(gui.new EvolutionStep(gui.currentCheckout, null, null, null));
        steps[c].isC = true;
    }

    EvolvingDNA[] steps;
    class EvolutionView extends JComponent {


        EvolutionView() {

            Timeline repaint = new SwingRepaintTimeline(this);
            repaint.playLoop(RepeatBehavior.LOOP);
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    for (int i = 0; i < steps.length; i++) {
                        if ((y < 30 * (i + scrollPos + 1)) && (y > 30 * (scrollPos + i))) {
                            steps[i].setClicked(true);
                            b = a;
                            a = i;
                        } else {
                            steps[i].setClicked(false);
                        }
                    }
                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    for (int i = 0; i < steps.length; i++) {
                        if ((y < 30 * (i + scrollPos + 1)) && (y > 30 * (scrollPos + i))) {
                            steps[i].setRollover(true);
                        } else {
                            steps[i].setRollover(false);
                        }
                    }
                }
            });
            this.addMouseWheelListener(new MouseWheelListener() {

                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                        scrollPos -= e.getUnitsToScroll();
                        if (scrollPos<0)
                            scrollPos=0;
                    }
                }
            });
        }
        int scrollPos = 0;

        public void paint(Graphics gx) {
            Graphics2D g = (Graphics2D) gx;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            /*for (Arc a:arcs)
            {
            g.drawLine(150,list.indexOf(a.from)*30, 200,list.indexOf(a.to)*30);
            }*/
            for (int i = 0; i < list.size(); i++) {
                {
                    steps[i].drawEvolutionStep(g, scrollPos);
                    EvolutionStep es = list.get(i);
                    g.setColor(Color.BLACK);
                    for (EvolutionStep parent : es.parents) {
                        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                        BevelArrows.drawDN(g, 155 + s.indexOf(parent.tags) * 20, (scrollPos + list.indexOf(parent)) * 30 + 15, 155 + s.indexOf(es.tags) * 20, (scrollPos + i) * 30 + 10);
                        //g.drawLine(154 + s.indexOf(es.tags) * 20, i * 30 + 15, 154 + s.indexOf(parent.tags) * 20, list.indexOf(parent) * 30 + 15);
                    }
                    g.setColor(Color.BLACK);
                    g.drawOval(150 + s.indexOf(es.tags) * 20, (scrollPos + i) * 30 + 10, 10, 10);
                    g.drawString(es.date, 300, (scrollPos + i) * 30 + 20);
                    g.drawString(es.sha.substring(0, 10), 450, (scrollPos + i) * 30 + 20);
                    g.drawString(es.tags, 550, (scrollPos + i) * 30 + 20);
                    g.drawString(es.comment, 650, (scrollPos + i) * 30 + 20);
                }
            }
            g.setColor(Color.white);
            for (int i = 0; i < list.size(); i++) {
                EvolutionStep es = list.get(i);
                g.fillOval(151 + s.indexOf(es.tags) * 20, 1 + (scrollPos + i) * 30 + 10, 9, 9);
            }
        }
    }

    class TTableModel extends javax.swing.table.AbstractTableModel {

        Class[] types = new Class[]{
            java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return "";
                case 1:
                    return "Time";
                case 2:
                    return "UUID";
                case 3:
                    return "Tags";
                case 4:
                    return "Comment";
            }
            return "";
        }

        public int getRowCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        public int getColumnCount() {
            return 5;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return (rowIndex == a) || (rowIndex == b);
                case 1:
                    return list.get(rowIndex).date;
                case 2:
                    return list.get(rowIndex).sha;
                case 3:
                    return list.get(rowIndex).tags;
                case 4:
                    return list.get(rowIndex).comment;
            }
            return null;
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

        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Green"));

        jButton4.setText("New branch");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Update");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Green & Yellow"));

        jButton3.setText("View diff");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Red & Green)"));

        jButton2.setText("Merge");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1084, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Evolution view", jPanel4);

        jTable1.setModel(new TTableModel());
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane1.addTab("Table view", jScrollPane1);

        jButton1.setText("HTML");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1089, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    int a = -1;
    int b = -1;
    int c = -1;
    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        if (jTable1.getSelectedRow() == a) {
            return;
        }
        if (jTable1.getSelectedRow() == b) {
            return;
        }
        b = a;
        a = jTable1.getSelectedRow();
        jTable1.repaint();
    }//GEN-LAST:event_jTable1MousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080/" + projectName + "/vdiff?from=" + list.get(a).sha + "&to=" + list.get(b).sha + "&detail=1"));
        } catch (Exception ex) {
            Logger.getLogger(Tyrannosaurus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (a == -1) {
            return;
        }
        String branchName = JOptionPane.showInputDialog(this, "Please input new branch name for commit " + list.get(a).sha, "Jurassic", JOptionPane.QUESTION_MESSAGE);
        if (branchName == null) {
            return;
        }
        /*String basis = JOptionPane.showInputDialog(this, "Please input the check-in basis for the new branch", "Jurassic", JOptionPane.QUESTION_MESSAGE);
        if (basis == null) {
        return;
        }*/
        Color c = JColorChooser.showDialog(this, "Please choose a color for the new branch", Color.yellow);
        if (c == null) {
            return;
        }
        BRANCH[3] = branchName;
        BRANCH[4] = list.get(a).sha;
        BRANCH[6] = "#" + Integer.toHexString(c.getRGB()).substring(2);
        gui.exec(BRANCH);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (a == -1) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to update to version " + list.get(a).sha + "?", "Jurassic", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            UPDATE[2] = list.get(a).sha;
            gui.exec(UPDATE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (a == -1) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to merge current version with " + list.get(a).sha + "?", "Jurassic", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            MERGE[2] = list.get(a).sha;
            gui.exec(MERGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080/" + projectName + "/timeline?n=10&y=ci"));
        } catch (Exception ex) {
            Logger.getLogger(Tyrannosaurus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
