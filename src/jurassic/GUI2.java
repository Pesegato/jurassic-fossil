/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jurassic;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlack2007Skin;

/**
 *
 * @author mmarcon
 */
public class GUI2 extends JRibbonFrame {

    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    SubstanceLookAndFeel.setSkin(new OfficeBlack2007Skin());
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                final GUI2 c = new GUI2();
                c.configureRibbon();
                c.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
                Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                c.setPreferredSize(new Dimension(r.width, r.height / 2));
                c.setMinimumSize(new Dimension(r.width / 10, r.height / 2));
                c.pack();
                c.setLocation(r.x, r.y);
                c.setVisible(true);
                c.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // c.addComponentListener(new ComponentAdapter() {
                // @Override
                // public void componentResized(ComponentEvent e) {
                // System.out.println("Size " + c.getSize());
                // }
                // });

//				c.getRootPane().getInputMap(
//						JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
//						KeyStroke.getKeyStroke("alt shift E"),
//						"installTracingRepaintManager");
//				c.getRootPane().getActionMap().put(
//						"installTracingRepaintManager", new AbstractAction() {
//							@Override
//							public void actionPerformed(ActionEvent e) {
//								RepaintManager
//										.setCurrentManager(new TracingRepaintManager());
//							}
//						});
            }
        });
    }

    private void configureRibbon() {

        this.getRibbon().addTask(configureHTMLTask());
        this.getRibbon().addTask(configureTicketTask());
        this.getRibbon().addTask(configureWikiTask());
    }

    private RibbonTask configureHTMLTask() {
        JRibbonBand homeBand = new JRibbonBand("HTML", null, new ExpandActionListener());
        homeBand.setExpandButtonKeyTip("FO");
        List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(homeBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(homeBand.getControlPanel()));
        homeBand.setResizePolicies(resizePolicies);
        JCommandButton mainButton = new JCommandButton("Go home", null);
        mainButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/NetmanV3/index");
            }
        });
        homeBand.addCommandButton(mainButton, RibbonElementPriority.TOP);
        JCommandButton fileButton = new JCommandButton("Go files", null);
        fileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/NetmanV3/dir?ci=tip");
            }
        });
        fileButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Tip","http://127.0.0.1:8080/NetmanV3/dir?ci=tip","Trunk","http://127.0.0.1:8080/NetmanV3/dir?ci=trunk","All","http://127.0.0.1:8080/NetmanV3/dir?ci=all");
            }
        });
        fileButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        homeBand.addCommandButton(fileButton, RibbonElementPriority.MEDIUM);
        JCommandButton leavesButton = new JCommandButton("Go leaves", null);
        leavesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/Netmanplus/leaves");
            }
        });
        leavesButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Open","http://127.0.0.1:8080/Netmanplus/leaves","Closed","http://127.0.0.1:8080/Netmanplus/leaves?closed","All","http://127.0.0.1:8080/Netmanplus/leaves?all");
            }
        });
        leavesButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        homeBand.addCommandButton(leavesButton, RibbonElementPriority.MEDIUM);
        JCommandButton branchButton = new JCommandButton("Go branches", null);
        branchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/Netmanplus/brlist");
            }
        });
        branchButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Open","http://127.0.0.1:8080/Netmanplus/brlist","Closed","http://127.0.0.1:8080/Netmanplus/brlist?closed","Timeline","http://127.0.0.1:8080/Netmanplus/brtimeline");
            }
        });
        branchButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        homeBand.addCommandButton(branchButton, RibbonElementPriority.MEDIUM);

        JRibbonBand statusBand = new JRibbonBand("Basic", null, new ExpandActionListener());
        statusBand.setResizePolicies(resizePolicies);
        JCommandButton statusButton = new JCommandButton("Status",null);
        statusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Status");
            }
        });
        JCommandButton diffsButton = new JCommandButton("Diffs",null);
        diffsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Diffs");
            }
        });
        statusBand.addCommandButton(statusButton, RibbonElementPriority.TOP);
        statusBand.addCommandButton(diffsButton, RibbonElementPriority.TOP);


        RibbonTask htmlTask = new RibbonTask("View", homeBand, statusBand);
        //quickStylesBand, fontBand, documentBand, findBand);
        htmlTask.setKeyTip("P");
        return htmlTask;
    }

    private RibbonTask configureTicketTask() {
        JRibbonBand editBand = new JRibbonBand("Edit", null, new ExpandActionListener());
        JCommandButton newButton = new JCommandButton("New ticket", null);
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/NetmanPlus/tktnew");
            }
        });
        editBand.addCommandButton(newButton, RibbonElementPriority.TOP);

        JRibbonBand viewBand = new JRibbonBand("View", null, new ExpandActionListener());
        JCommandButton statusButton = new JCommandButton("All tickets",null);
        statusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("All t");
            }
        });
        statusButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        statusButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("All","http://127.0.0.1:8080/NetmanPlus/rptview?rn=1","Raw","http://127.0.0.1:8080/NetmanPlus/rptview?tablist=1&rn=1");
            }
        });
        JCommandButton diffsButton = new JCommandButton("Timeline",null);
        diffsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Timeline t");
            }
        });
        diffsButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        diffsButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Latest 20","http://127.0.0.1:8080/NetmanPlus/timeline?n=20&y=t","Latest 200","http://127.0.0.1:8080/NetmanPlus/timeline?n=200&y=t","Latest 1000","http://127.0.0.1:8080/NetmanPlus/timeline?n=1000&y=t");
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

        RibbonTask ticketTask = new RibbonTask("Ticket",editBand,viewBand);
        ticketTask.setKeyTip("T");
        return ticketTask;
    }

    private RibbonTask configureWikiTask() {
        JRibbonBand editBand = new JRibbonBand("Edit", null, new ExpandActionListener());
        JCommandButton newButton = new JCommandButton("New wiki", null);
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/NetmanPlus/wikinew");
            }
        });
        editBand.addCommandButton(newButton, RibbonElementPriority.TOP);

        JRibbonBand viewBand = new JRibbonBand("View", null, new ExpandActionListener());
        JCommandButton statusButton = new JCommandButton("All wiki",null);
        statusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/NetmanPlus/wcontent");
            }
        });
        /*statusButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        statusButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("All","http://127.0.0.1:8080/NetmanPlus/rptview?rn=1","Raw","http://127.0.0.1:8080/NetmanPlus/rptview?tablist=1&rn=1");
            }
        });*/
        JCommandButton diffsButton = new JCommandButton("Timeline",null);
        diffsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("http://127.0.0.1:8080/NetmanPlus/timeline?y=w");
            }
        });
        diffsButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        diffsButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return new SamplePopupMenu("Latest 20","http://127.0.0.1:8080/NetmanPlus/timeline?n=20&y=w","Latest 200","http://127.0.0.1:8080/NetmanPlus/timeline?n=200&y=w","Latest 1000","http://127.0.0.1:8080/NetmanPlus/timeline?n=1000&y=w");
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

        RibbonTask wikiTask = new RibbonTask("Wiki",editBand,viewBand);
        wikiTask.setKeyTip("W");
        return wikiTask;
    }

    private class ExpandActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(GUI2.this,
                    "Expand button clicked");
        }
    }

    private class SamplePopupMenu extends JCommandPopupMenu {

        public SamplePopupMenu(final String... commands) {

            for (int i=0;i<commands.length;i+=2)
            {
            JCommandMenuButton menuButton1 = new JCommandMenuButton(commands[i], null);
            final int z=i+1;
            menuButton1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(commands[z]);
                }
            });
            menuButton1.setActionKeyTip((i/2+1)+"");
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
}
