/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jurassic;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.icon.DecoratedResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.EmptyResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.*;
import static org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority.*;
import org.pushingpixels.flamingo.api.ribbon.resize.*;
import org.pushingpixels.flamingo.internal.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;

/**
 *
 * @author mmarcon
 */
public class GUI2 extends JRibbonFrame {
	public static void main(String[] args) {


		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (Exception exc) {
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final GUI2 c = new GUI2();
				c.configureRibbon();
				c.applyComponentOrientation(ComponentOrientation
						.getOrientation(Locale.getDefault()));
				Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getMaximumWindowBounds();
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

				c.getRootPane().getInputMap(
						JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
						KeyStroke.getKeyStroke("alt shift E"),
						"installTracingRepaintManager");
				c.getRootPane().getActionMap().put(
						"installTracingRepaintManager", new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent e) {
								RepaintManager
										.setCurrentManager(new TracingRepaintManager());
							}
						});
			}
		});
	}
	public void configureRibbon() {
		JRibbonBand clipboardBand = this.getClipboardBand();
		JRibbonBand quickStylesBand = this.getQuickStylesBand();
		JFlowRibbonBand fontBand = this.getFontBand();
		JRibbonBand documentBand = this.getDocumentBand();
		JRibbonBand findBand = this.getFindBand();
		RibbonTask pageLayoutTask = new RibbonTask(resourceBundle
				.getString("PageLayout.textTaskTitle"), clipboardBand,
				quickStylesBand, fontBand, documentBand, findBand);
		pageLayoutTask.setKeyTip("P");

		JRibbonBand actionBand = this.getActionBand();
		JRibbonBand preferencesBand = this.getPreferencesBand();
		JRibbonBand arrangeBand = this.getApplicationsBand();
		paragraphBand = this.getParagraphBand();
		JRibbonBand showHideBand = this.getShowHideBand();
		RibbonTask writeTask = new RibbonTask(resourceBundle
				.getString("Write.textTaskTitle"), actionBand, preferencesBand,
				arrangeBand, paragraphBand, showHideBand);
		writeTask
				.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(
						writeTask));
		writeTask.setKeyTip("W");

		JRibbonBand previewBand = this.getPreviewBand();
		JRibbonBand animationBand = this.getAnimationBand();
		JRibbonBand transitionBand = this.getTransitionBand();
		JRibbonBand transitionNextBand = this.getTransitionNextBand();
		RibbonTask animationsTask = new RibbonTask(resourceBundle
				.getString("Animations.textTaskTitle"), previewBand,
				animationBand, transitionBand, transitionNextBand);
		animationsTask.setKeyTip("A");

		JRibbonBand rowSpanBand = this.getRowSpanBand();
		JRibbonBand alignmentBand = this.getAlignmentBand();
		RibbonTask wrappedTask = new RibbonTask(resourceBundle
				.getString("Wrapped.textTaskTitle"), rowSpanBand, alignmentBand);
		wrappedTask.setKeyTip("R");

		this.getRibbon().addTask(pageLayoutTask);
		this.getRibbon().addTask(writeTask);
		this.getRibbon().addTask(animationsTask);
		this.getRibbon().addTask(wrappedTask);

		this.getRibbon().configureHelp(new help_browser(),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(GUI2.this,
								"Help button clicked");
					}
				});

		group1 = new RibbonContextualTaskGroup(resourceBundle
				.getString("Group1.textTaskGroupTitle"), Color.red,
				getContextualRibbonTask(resourceBundle
						.getString("Task11.textTaskTitle"), "XA"),
				getContextualRibbonTask(resourceBundle
						.getString("Task12.textTaskTitle"), "XB"));
		group2 = new RibbonContextualTaskGroup(resourceBundle
				.getString("Group2.textTaskGroupTitle"), Color.green,
				getContextualRibbonTask(resourceBundle
						.getString("Task21.textTaskTitle"), "YA"));
		this.getRibbon().addContextualTaskGroup(group1);
		this.getRibbon().addContextualTaskGroup(group2);

		configureTaskBar();

		// application menu
		configureApplicationMenu();

		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new EmptyBorder(20, 0, 0, 5));
		FormLayout lm = new FormLayout("right:pref, 4dlu, fill:pref:grow", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(lm, controlPanel);

		this.configureControlPanel(builder);

		this.add(controlPanel, BorderLayout.EAST);
		this.add(new RulerPanel(), BorderLayout.CENTER);

		this.configureStatusBar();
	}








	protected Locale currLocale;

	protected ResourceBundle resourceBundle;

	protected class QuickStylesPanel extends JCommandButtonPanel {
		public QuickStylesPanel() {
			super(32);

			MessageFormat mfGroupTitle = new MessageFormat(resourceBundle
					.getString("PanelStyles.text"));
			mfGroupTitle.setLocale(currLocale);
			MessageFormat mfTooltipTitle = new MessageFormat(resourceBundle
					.getString("PanelStyles.tooltip.textActionTitle"));
			mfTooltipTitle.setLocale(currLocale);
			MessageFormat mfTooltipParagraph = new MessageFormat(resourceBundle
					.getString("PanelStyles.tooltip.textActionParagraph1"));
			mfTooltipParagraph.setLocale(currLocale);

			for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
				String iconGroupName = mfGroupTitle
						.format(new Object[] { groupIndex });
				this.addButtonGroup(iconGroupName, groupIndex);
				for (int i = 0; i < 15; i++) {
					final int index = i;
					ResizableIcon fontIcon = new font_x_generic();
					ResizableIcon finalIcon = new DecoratedResizableIcon(
							fontIcon,
							new DecoratedResizableIcon.IconDecorator() {
								@Override
								public void paintIconDecoration(Component c,
										Graphics g, int x, int y, int width,
										int height) {
									Graphics2D g2d = (Graphics2D) g.create();
									g2d.setColor(Color.black);
									g2d
											.setFont(UIManager
													.getFont("Label.font"));
									RenderingUtils.installDesktopHints(g2d);
									g2d.drawString("" + index, x + 2, y
											+ height - 2);
									g2d.dispose();
								}
							});
					JCommandToggleButton jrb = new JCommandToggleButton(null,
							finalIcon);
					jrb.setName("Group " + groupIndex + ", index " + i);
					jrb.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							System.out.println("Invoked action on " + index);
						}
					});
					String actionTooltipTitle = mfTooltipTitle
							.format(new Object[] { i });
					String actionTooltipParagraph1 = mfTooltipParagraph
							.format(new Object[] { i });
					jrb.setActionRichTooltip(new RichTooltip(
							actionTooltipTitle, actionTooltipParagraph1));
					this.addButtonToLastGroup(jrb);
				}
			}
			this.setSingleSelectionMode(true);
		}
	}

	private class ExpandActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(GUI2.this,
					"Expand button clicked");
		}
	}

	private class SamplePopupMenu extends JCommandPopupMenu {
		public SamplePopupMenu() {
			MessageFormat mf = new MessageFormat(resourceBundle
					.getString("TestMenuItem.text"));
			mf.setLocale(currLocale);

			JCommandMenuButton menuButton1 = new JCommandMenuButton(mf
					.format(new Object[] { "1" }), new EmptyResizableIcon(16));
			menuButton1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 1 activated");
				}
			});
			menuButton1.setActionKeyTip("1");
			this.addMenuButton(menuButton1);

			JCommandMenuButton menuButton2 = new JCommandMenuButton(mf
					.format(new Object[] { "2" }), new EmptyResizableIcon(16));
			menuButton2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 2 activated");
				}
			});
			menuButton2.setActionKeyTip("2");
			this.addMenuButton(menuButton2);

			JCommandMenuButton menuButton3 = new JCommandMenuButton(mf
					.format(new Object[] { "3" }), new EmptyResizableIcon(16));
			menuButton3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 3 activated");
				}
			});
			menuButton3.setActionKeyTip("3");
			this.addMenuButton(menuButton3);

			this.addMenuSeparator();

			JCommandMenuButton menuButton4 = new JCommandMenuButton(mf
					.format(new Object[] { "4" }), new EmptyResizableIcon(16));
			menuButton4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 4 activated");
				}
			});
			menuButton4.setActionKeyTip("4");
			this.addMenuButton(menuButton4);

			JCommandMenuButton menuButton5 = new JCommandMenuButton(mf
					.format(new Object[] { "5" }), new EmptyResizableIcon(16));
			menuButton5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Test menu item 5 activated");
				}
			});
			menuButton5.setActionKeyTip("5");
			this.addMenuButton(menuButton5);
		}
	}

	private AbstractCommandButton getIconButton(final Icon icon,
			boolean isToggle, boolean isSelected, boolean hasPopup) {
		ResizableIcon resizableIcon = new ResizableIcon() {
			int width = icon.getIconWidth();
			int height = icon.getIconHeight();

			@Override
			public int getIconHeight() {
				return this.height;
			}

			@Override
			public int getIconWidth() {
				return this.width;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				icon.paintIcon(c, g, x, y);
			}

			@Override
			public void setDimension(Dimension newDimension) {
				this.width = newDimension.width;
				this.height = newDimension.height;
			}
		};
		AbstractCommandButton button = isToggle ? new JCommandToggleButton("",
				resizableIcon) : new JCommandButton("", resizableIcon);
		button.setDisplayState(CommandButtonDisplayState.SMALL);
		button.setGapScaleFactor(0.5);
		if (isSelected)
			button.getActionModel().setSelected(true);

		// make the button narrower by stripping away some of the right-left
		// insets
		Insets currInsets = button.getInsets();
		button.setBorder(new EmptyBorder(currInsets.top, currInsets.top / 2,
				currInsets.bottom, currInsets.bottom / 2));

		if (hasPopup) {
			((JCommandButton) button)
					.setPopupCallback(new PopupPanelCallback() {
						@Override
						public JPopupPanel getPopupPanel(
								JCommandButton commandButton) {
							return new SamplePopupMenu();
						}
					});
		}
		return button;
	}

	protected JRibbonBand getActionBand() {
		JRibbonBand actionBand = new JRibbonBand(resourceBundle
				.getString("Action.textBandTitle"), new document_new(),
				new ExpandActionListener());
		actionBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(actionBand));

		actionBand.startGroup();
		JCommandButton addressBookButton = new JCommandButton(resourceBundle
				.getString("AddressBook.text"), new address_book_new());
		addressBookButton.setActionKeyTip("NA");
		actionBand.addCommandButton(addressBookButton,
				RibbonElementPriority.TOP);

		actionBand.startGroup();
		JCommandButton documentButton = new JCommandButton(resourceBundle
				.getString("Document.text"), new document_new());
		documentButton.setActionKeyTip("ND");
		actionBand.addCommandButton(documentButton, RibbonElementPriority.TOP);

		JCommandButton appointmentButton = new JCommandButton(resourceBundle
				.getString("Appointment.text"), new appointment_new());
		appointmentButton.setActionKeyTip("NP");
		actionBand.addCommandButton(appointmentButton,
				RibbonElementPriority.MEDIUM);

		JCommandButton bookmarkButton = new JCommandButton(resourceBundle
				.getString("Bookmark.text"), new bookmark_new());
		bookmarkButton.setActionKeyTip("NB");
		actionBand.addCommandButton(bookmarkButton,
				RibbonElementPriority.MEDIUM);

		JCommandButton contactButton = new JCommandButton(resourceBundle
				.getString("Contact.text"), new contact_new());
		contactButton.setActionKeyTip("NC");
		actionBand
				.addCommandButton(contactButton, RibbonElementPriority.MEDIUM);

		List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
		resizePolicies.add(new CoreRibbonResizePolicies.Mirror(actionBand
				.getControlPanel()));
		resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(actionBand
				.getControlPanel()));
		resizePolicies.add(new IconRibbonBandResizePolicy(actionBand
				.getControlPanel()));
		actionBand.setResizePolicies(resizePolicies);

		return actionBand;
	}

	protected JRibbonBand getPreferencesBand() {
		JRibbonBand preferencesBand = new JRibbonBand(resourceBundle
				.getString("Preferences.textBandTitle"),
				new preferences_desktop_font(), new ExpandActionListener());
		preferencesBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(preferencesBand));

		preferencesBand.startGroup();
		JCommandButton accessibility = new JCommandButton(resourceBundle
				.getString("Accessibility.text"),
				new preferences_desktop_accessibility());
		accessibility.setActionKeyTip("Y");
		preferencesBand.addCommandButton(accessibility,
				RibbonElementPriority.MEDIUM);

		JCommandButton assistiveTech = new JCommandButton(resourceBundle
				.getString("Assistive.text"),
				new preferences_desktop_assistive_technology());
		assistiveTech.setActionKeyTip("E");
		preferencesBand.addCommandButton(assistiveTech,
				RibbonElementPriority.MEDIUM);

		JCommandButton keyboardShortcuts = new JCommandButton(resourceBundle
				.getString("KeyboardShortcuts.text"),
				new preferences_desktop_keyboard_shortcuts());
		keyboardShortcuts.setPopupKeyTip("H");
		keyboardShortcuts
				.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
		keyboardShortcuts.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		preferencesBand.addCommandButton(keyboardShortcuts,
				RibbonElementPriority.MEDIUM);

		preferencesBand.startGroup();

		JCommandButton size = new JCommandButton(resourceBundle
				.getString("Font.text"), new preferences_desktop_font());
		size.setActionKeyTip("Z");
		preferencesBand.addCommandButton(size, RibbonElementPriority.TOP);

		JCommandButton locale = new JCommandButton(resourceBundle
				.getString("Locale.text"), new preferences_desktop_locale());
		locale.setActionKeyTip("L");
		preferencesBand.addCommandButton(locale, RibbonElementPriority.TOP);

		preferencesBand.startGroup();
		JCommandButton screensaver = new JCommandButton(resourceBundle
				.getString("Screensaver.text"),
				new preferences_desktop_screensaver());
		screensaver.setActionKeyTip("V");
		preferencesBand.addCommandButton(screensaver,
				RibbonElementPriority.MEDIUM);

		JCommandButton themes = new JCommandButton(resourceBundle
				.getString("Themes.text"), new preferences_desktop_theme());
		themes.setActionKeyTip("T");
		preferencesBand.addCommandButton(themes, RibbonElementPriority.MEDIUM);

		return preferencesBand;
	}

	protected JRibbonBand getParagraphBand() {
		JRibbonBand paragraphBand = new JRibbonBand(resourceBundle
				.getString("Paragraph.textBandTitle"),
				new format_justify_left(), null);

		paragraphBand.startGroup(resourceBundle.getString("Indent.text"));
		JRibbonComponent justifyLeftWrapper = new JRibbonComponent(
				new format_justify_left(), resourceBundle
						.getString("IndentLeft.text"), new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		justifyLeftWrapper.setKeyTip("PL");

		RichTooltip justifyLeftTooltip = new RichTooltip();
		justifyLeftTooltip.setTitle(resourceBundle
				.getString("IndentLeft.tooltip.title"));
		justifyLeftTooltip.addDescriptionSection(resourceBundle
				.getString("IndentLeft.tooltip.actionParagraph1"));
		justifyLeftTooltip.addDescriptionSection(resourceBundle
				.getString("IndentLeft.tooltip.actionParagraph2"));
		justifyLeftWrapper.setRichTooltip(justifyLeftTooltip);

		paragraphBand.addRibbonComponent(justifyLeftWrapper);

		JRibbonComponent justifyRightWrapper = new JRibbonComponent(
				new format_justify_right(), resourceBundle
						.getString("IndentRight.text"), new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		justifyRightWrapper.setKeyTip("PR");

		RichTooltip justifyRightTooltip = new RichTooltip();
		justifyRightTooltip.setTitle(resourceBundle
				.getString("IndentRight.tooltip.title"));
		justifyRightTooltip.addDescriptionSection(resourceBundle
				.getString("IndentRight.tooltip.actionParagraph1"));
		justifyRightTooltip.addDescriptionSection(resourceBundle
				.getString("IndentRight.tooltip.actionParagraph2"));
		justifyRightWrapper.setRichTooltip(justifyRightTooltip);

		paragraphBand.addRibbonComponent(justifyRightWrapper);

		paragraphBand.startGroup(resourceBundle.getString("Spacing.text"));
		JRibbonComponent beforeWrapper = new JRibbonComponent(new JSpinner(
				new SpinnerNumberModel(0, 0, 100, 5)));
		beforeWrapper.setKeyTip("PB");
		paragraphBand.addRibbonComponent(beforeWrapper);

		JRibbonComponent afterWrapper = new JRibbonComponent(new JSpinner(
				new SpinnerNumberModel(10, 0, 100, 5)));
		afterWrapper.setKeyTip("PA");
		paragraphBand.addRibbonComponent(afterWrapper);

		return paragraphBand;
	}

	protected JRibbonBand getShowHideBand() {
		JRibbonBand showHideBand = new JRibbonBand(resourceBundle
				.getString("ShowHide.textBandTitle"),
				new format_justify_left(), null);

		JCheckBox ruler = new JCheckBox(resourceBundle.getString("Ruler.text"));
		ruler.setSelected(true);
		JRibbonComponent rulerWrapper = new JRibbonComponent(ruler);
		rulerWrapper.setKeyTip("SR");
		showHideBand.addRibbonComponent(rulerWrapper);

		JCheckBox gridlines = new JCheckBox(resourceBundle
				.getString("Gridlines.text"));
		JRibbonComponent gridlinesWrapper = new JRibbonComponent(gridlines);
		gridlinesWrapper.setKeyTip("SG");
		showHideBand.addRibbonComponent(gridlinesWrapper);

		JCheckBox messageBar = new JCheckBox(resourceBundle
				.getString("MessageBar.text"));
		messageBar.setEnabled(false);
		JRibbonComponent messageBarWrapper = new JRibbonComponent(messageBar);
		messageBarWrapper.setKeyTip("SM");
		showHideBand.addRibbonComponent(messageBarWrapper);

		JCheckBox documentMap = new JCheckBox(resourceBundle
				.getString("DocumentMap.text"));
		JRibbonComponent documentMapWrapper = new JRibbonComponent(documentMap);
		documentMapWrapper.setKeyTip("SD");
		showHideBand.addRibbonComponent(documentMapWrapper);

		JCheckBox thumbnails = new JCheckBox(resourceBundle
				.getString("Thumbnails.text"));
		JRibbonComponent thumbnailsWrapper = new JRibbonComponent(thumbnails);
		thumbnailsWrapper.setKeyTip("ST");
		showHideBand.addRibbonComponent(thumbnailsWrapper);

		return showHideBand;
	}

	protected JRibbonBand getApplicationsBand() {
		JRibbonBand applicationsBand = new JRibbonBand(resourceBundle
				.getString("Applications.textBandTitle"),
				new applications_other(), new ExpandActionListener());

		JRibbonComponent games = new JRibbonComponent(new applications_games(),
				resourceBundle.getString("Games.text"), new JComboBox(
						new Object[] { "Tetris", "Minesweeper", "Doom" }));
		games.setKeyTip("AG");
		games.setResizingAware(true);
		games.setHorizontalAlignment(HorizontalAlignment.FILL);
		applicationsBand.addRibbonComponent(games);

		JRibbonComponent internet = new JRibbonComponent(
				new applications_internet(), resourceBundle
						.getString("Internet.text"), new JComboBox(
						new Object[] { "Firefox", "Opera", "Konqueror" }));
		internet.setKeyTip("AI");
		internet.setEnabled(false);
		internet.setResizingAware(true);
		internet.setHorizontalAlignment(HorizontalAlignment.FILL);
		applicationsBand.addRibbonComponent(internet);

		JRibbonComponent multimedia = new JRibbonComponent(null, resourceBundle
				.getString("Multimedia.text"), new JComboBox(new Object[] {
				resourceBundle.getString("Pictures.text"),
				resourceBundle.getString("Video.text"),
				resourceBundle.getString("Audio.text") }));
		multimedia.setKeyTip("AM");
		multimedia.setResizingAware(true);
		multimedia.setHorizontalAlignment(HorizontalAlignment.FILL);
		applicationsBand.addRibbonComponent(multimedia);

		return applicationsBand;
	}

	protected JRibbonBand getDocumentBand() {
		JRibbonBand result = new JRibbonBand(resourceBundle
				.getString("Document.textBandTitle"),
				new applications_office(), new ExpandActionListener());
		result.setExpandButtonKeyTip("FY");
		result.setCollapsedStateKeyTip("ZD");

		result.startGroup();
		JCommandButton localFolderButton = new JCommandButton(resourceBundle
				.getString("DocumentLocal.text"), new folder());
		result.addCommandButton(localFolderButton, RibbonElementPriority.TOP);

		JCommandButton remoteFolderButton = new JCommandButton(resourceBundle
				.getString("DocumentRemote.text"), new folder_remote());
		result.addCommandButton(remoteFolderButton, RibbonElementPriority.TOP);

		JCommandButton savedFolderButton = new JCommandButton(resourceBundle
				.getString("DocumentSaved.text"), new folder_saved_search());
		result.addCommandButton(savedFolderButton, RibbonElementPriority.TOP);

		result.startGroup();

		JCommandButton docNewButton = new JCommandButton(resourceBundle
				.getString("DocumentNew.text"), new document_new());
		result.addCommandButton(docNewButton, RibbonElementPriority.MEDIUM);

		JCommandButton docOpenButton = new JCommandButton(resourceBundle
				.getString("DocumentOpen.text"), new document_open());
		result.addCommandButton(docOpenButton, RibbonElementPriority.MEDIUM);

		JCommandButton docSaveButton = new JCommandButton(resourceBundle
				.getString("DocumentSave.text"), new document_save());
		result.addCommandButton(docSaveButton, RibbonElementPriority.MEDIUM);

		JCommandButton docPrintButton = new JCommandButton(resourceBundle
				.getString("DocumentPrint.text"), new document_print());
		result.addCommandButton(docPrintButton, RibbonElementPriority.MEDIUM);

		JCommandButton docPrintPreviewButton = new JCommandButton(
				resourceBundle.getString("DocumentPrintPreview.text"),
				new document_print_preview());
		result.addCommandButton(docPrintPreviewButton,
				RibbonElementPriority.MEDIUM);

		JCommandButton docPropertiesButton = new JCommandButton(resourceBundle
				.getString("DocumentProperties.text"),
				new document_properties());
		result.addCommandButton(docPropertiesButton,
				RibbonElementPriority.MEDIUM);

		result.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(result));

		return result;
	}

	protected JRibbonBand getClipboardBand() {
		JRibbonBand clipboardBand = new JRibbonBand(resourceBundle
				.getString("Clipboard.textBandTitle"), new edit_paste(),
				new ExpandActionListener());
		clipboardBand.setExpandButtonKeyTip("FO");
		RichTooltip expandRichTooltip = new RichTooltip();
		expandRichTooltip.setTitle(resourceBundle
				.getString("Clipboard.textBandTitle"));
		expandRichTooltip.addDescriptionSection(resourceBundle
				.getString("Clipboard.textBandTooltipParagraph1"));
		clipboardBand.setExpandButtonRichTooltip(expandRichTooltip);
		clipboardBand.setCollapsedStateKeyTip("ZC");

		JCommandButton mainButton = new JCommandButton(resourceBundle
				.getString("Paste.text"), new edit_paste());
		mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pasted!");
			}
		});
		mainButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		mainButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		RichTooltip mainRichTooltip = new RichTooltip();
		mainRichTooltip.setTitle(resourceBundle.getString("Paste.text"));
		mainRichTooltip.addDescriptionSection(resourceBundle
				.getString("Paste.tooltip.actionParagraph1"));
		mainButton.setActionRichTooltip(mainRichTooltip);
		mainButton.setPopupKeyTip("V");

		RichTooltip mainPopupRichTooltip = new RichTooltip();
		mainPopupRichTooltip.setTitle(resourceBundle.getString("Paste.text"));
		mainPopupRichTooltip.addDescriptionSection(resourceBundle
				.getString("Paste.tooltip.popupParagraph1"));
		mainButton.setPopupRichTooltip(mainPopupRichTooltip);

		clipboardBand.addCommandButton(mainButton, RibbonElementPriority.TOP);

		JCommandButton cutButton = new JCommandButton(resourceBundle
				.getString("Cut.text"), new edit_cut());
		cutButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		cutButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		RichTooltip cutRichTooltip = new RichTooltip();
		cutRichTooltip.setTitle(resourceBundle.getString("Cut.text"));
		cutRichTooltip.addDescriptionSection(resourceBundle
				.getString("Cut.tooltip.actionParagraph1"));
		cutButton.setActionRichTooltip(cutRichTooltip);
		cutButton.setPopupKeyTip("X");

		clipboardBand.addCommandButton(cutButton, RibbonElementPriority.MEDIUM);

		JCommandButton copyButton = new JCommandButton(resourceBundle
				.getString("Copy.text"), new edit_copy());
		copyButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		copyButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_POPUP);
		copyButton.setPopupKeyTip("C");

		clipboardBand
				.addCommandButton(copyButton, RibbonElementPriority.MEDIUM);

		JCommandButton formatButton = new JCommandButton(resourceBundle
				.getString("Format.text"), new edit_paste());
		formatButton.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				JCommandPopupMenu popupMenu = new JCommandPopupMenu(
						new QuickStylesPanel(), 5, 3);
				JCommandMenuButton saveSelectionButton = new JCommandMenuButton(
						resourceBundle
								.getString("Format.menuSaveSelection.text"),
						new EmptyResizableIcon(16));
				saveSelectionButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Save Selection activated");
					}
				});
				saveSelectionButton.setActionKeyTip("SS");
				popupMenu.addMenuButton(saveSelectionButton);

				JCommandMenuButton clearSelectionButton = new JCommandMenuButton(
						resourceBundle
								.getString("Format.menuClearSelection.text"),
						new EmptyResizableIcon(16));
				clearSelectionButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Clear Selection activated");
					}
				});
				clearSelectionButton.setActionKeyTip("SC");
				popupMenu.addMenuButton(clearSelectionButton);

				popupMenu.addMenuSeparator();
				JCommandMenuButton applyStylesButton = new JCommandMenuButton(
						resourceBundle.getString("Format.applyStyles.text"),
						new EmptyResizableIcon(16));
				applyStylesButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Apply Styles activated");
					}
				});
				applyStylesButton.setActionKeyTip("SA");
				popupMenu.addMenuButton(applyStylesButton);
				return popupMenu;
			}
		});

		formatButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
		// pasteButton.addPopupActionListener(new SamplePopupActionListener());
		formatButton
				.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
		formatButton.setPopupKeyTip("FP");

		clipboardBand.addCommandButton(formatButton,
				RibbonElementPriority.MEDIUM);

		List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
		resizePolicies.add(new CoreRibbonResizePolicies.Mirror(clipboardBand
				.getControlPanel()));
		resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(clipboardBand
				.getControlPanel()));
		clipboardBand.setResizePolicies(resizePolicies);

		return clipboardBand;
	}

	protected JRibbonBand getFindBand() {
		JRibbonBand findBand = new JRibbonBand(resourceBundle
				.getString("Find.textBandTitle"), new edit_find());
		findBand.setCollapsedStateKeyTip("ZY");

		JCommandToggleButton findButton = new JCommandToggleButton(
				resourceBundle.getString("Find.text"), new system_search());
		findButton.setActionKeyTip("FD");
		findBand.addCommandButton(findButton, RibbonElementPriority.TOP);

		JCommandToggleButton replaceButton = new JCommandToggleButton(
				resourceBundle.getString("Find.text"), new edit_find());
		findBand.addCommandButton(replaceButton, RibbonElementPriority.MEDIUM);

		JCommandToggleButton findReplaceButton = new JCommandToggleButton(
				resourceBundle.getString("FindReplace.text"),
				new edit_find_replace());
		findReplaceButton.setEnabled(false);
		findBand.addCommandButton(findReplaceButton,
				RibbonElementPriority.MEDIUM);

		JCommandToggleButton selectAllButton = new JCommandToggleButton(
				resourceBundle.getString("SelectAll.text"),
				new edit_select_all());
		findBand
				.addCommandButton(selectAllButton, RibbonElementPriority.MEDIUM);

		List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
		resizePolicies.add(new CoreRibbonResizePolicies.Mirror(findBand
				.getControlPanel()));
		resizePolicies.add(new IconRibbonBandResizePolicy(findBand
				.getControlPanel()));
		findBand.setResizePolicies(resizePolicies);

		return findBand;
	}

	protected JRibbonBand getQuickStylesBand() {
		JRibbonBand quickStylesBand = new JRibbonBand(resourceBundle
				.getString("QuickStyles.textBandTitle"),
				new preferences_desktop_theme());
		quickStylesBand.setCollapsedStateKeyTip("ZS");

		quickStylesBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesRestrictive(quickStylesBand));

		Map<RibbonElementPriority, Integer> stylesGalleryVisibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
		stylesGalleryVisibleButtonCounts.put(RibbonElementPriority.LOW, 1);
		stylesGalleryVisibleButtonCounts.put(RibbonElementPriority.MEDIUM, 2);
		stylesGalleryVisibleButtonCounts.put(RibbonElementPriority.TOP, 2);

		List<StringValuePair<List<JCommandToggleButton>>> stylesGalleryButtons = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();
		List<JCommandToggleButton> stylesGalleryButtonsList = new ArrayList<JCommandToggleButton>();
		List<JCommandToggleButton> stylesGalleryButtonsList2 = new ArrayList<JCommandToggleButton>();
		MessageFormat mfButtonText = new MessageFormat(resourceBundle
				.getString("StylesGallery.textButton"));
		mfButtonText.setLocale(currLocale);
		for (int i = 0; i < 30; i++) {
			final int index = i;
			ResizableIcon fontIcon = new font_x_generic();
			ResizableIcon finalIcon = new DecoratedResizableIcon(fontIcon,
					new DecoratedResizableIcon.IconDecorator() {
						@Override
						public void paintIconDecoration(Component c,
								Graphics g, int x, int y, int width, int height) {
							Graphics2D g2d = (Graphics2D) g.create();
							g2d.setColor(Color.black);
							RenderingUtils.installDesktopHints(g2d);
							g2d.setFont(UIManager.getFont("Label.font"));
							g2d.drawString("" + index, x + 2, y + height - 2);
							g2d.dispose();
						}
					});
			JCommandToggleButton jrb = new JCommandToggleButton(mfButtonText
					.format(new Object[] { i }), finalIcon);
			if (i == 1)
				jrb.getActionModel().setSelected(true);
			jrb.setName("Style " + i);
			jrb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Invoked action on " + index);
				}
			});
			if (i < 10)
				stylesGalleryButtonsList.add(jrb);
			else
				stylesGalleryButtonsList2.add(jrb);
		}

		stylesGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>(
						resourceBundle
								.getString("StylesGallery.textGroupTitle1"),
						stylesGalleryButtonsList));
		stylesGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>(
						resourceBundle
								.getString("StylesGallery.textGroupTitle2"),
						stylesGalleryButtonsList2));

		quickStylesBand.addRibbonGallery("Styles", stylesGalleryButtons,
				stylesGalleryVisibleButtonCounts, 3, 3,
				JRibbonBand.BIG_FIXED_LANDSCAPE, RibbonElementPriority.TOP);
		quickStylesBand.setRibbonGalleryPopupCallback("Styles",
				new JRibbonBand.RibbonGalleryPopupCallback() {
					public void popupToBeShown(JCommandPopupMenu menu) {
						JCommandMenuButton saveSelectionButton = new JCommandMenuButton(
								resourceBundle
										.getString("Format.menuSaveSelection.text"),
								new EmptyResizableIcon(16));
						saveSelectionButton
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										System.out
												.println("Save Selection activated");
									}
								});
						saveSelectionButton.setActionKeyTip("SS");
						menu.addMenuButton(saveSelectionButton);

						JCommandMenuButton clearSelectionButton = new JCommandMenuButton(
								resourceBundle
										.getString("Format.menuClearSelection.text"),
								new EmptyResizableIcon(16));
						clearSelectionButton
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										System.out
												.println("Clear Selection activated");
									}
								});
						clearSelectionButton.setActionKeyTip("SC");
						menu.addMenuButton(clearSelectionButton);

						menu.addMenuSeparator();
						JCommandMenuButton applyStylesButton = new JCommandMenuButton(
								resourceBundle
										.getString("Format.applyStyles.text"),
								new font_x_generic());
						applyStylesButton
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										System.out
												.println("Apply Styles activated");
									}
								});
						applyStylesButton.setActionKeyTip("SA");
						menu.addMenuButton(applyStylesButton);
					}
				});
		quickStylesBand.setRibbonGalleryExpandKeyTip("Styles", "L");

		JCommandButton stylesButton1 = new JCommandButton(resourceBundle
				.getString("Styles1.text"), new font_x_generic());
		stylesButton1.setActionKeyTip("SA");
		quickStylesBand.addCommandButton(stylesButton1,
				RibbonElementPriority.MEDIUM);

		JCommandButton styles2Button = new JCommandButton(resourceBundle
				.getString("Styles2.text"), new image_x_generic());
		styles2Button.setActionKeyTip("SB");
		quickStylesBand.addCommandButton(styles2Button,
				RibbonElementPriority.MEDIUM);
		styles2Button.setEnabled(false);

		JCommandButton styles3Button = new JCommandButton(resourceBundle
				.getString("Styles3.text"), new text_html());
		styles3Button.setActionKeyTip("SC");
		quickStylesBand.addCommandButton(styles3Button,
				RibbonElementPriority.MEDIUM);

		return quickStylesBand;
	}

	protected JRibbonBand getPreviewBand() {
		JRibbonBand previewBand = new JRibbonBand(resourceBundle
				.getString("Preview.textBandTitle"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));

		previewBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesNone(previewBand));

		JCommandButton findButton = new JCommandButton(resourceBundle
				.getString("Preview.text"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));
		previewBand.addCommandButton(findButton, RibbonElementPriority.TOP);

		JCommandButton slideShowButton = new JCommandButton(resourceBundle
				.getString("SlideShow.text"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));
		previewBand
				.addCommandButton(slideShowButton, RibbonElementPriority.TOP);

		return previewBand;
	}

	protected JRibbonBand getRowSpanBand() {
		JRibbonBand rowSpanBand = new JRibbonBand(resourceBundle
				.getString("RowSpans.textBandTitle"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));

		JTextPane textPane1 = new JTextPane();
		textPane1.setText(resourceBundle.getString("ThreeRows.text"));
		JScrollPane scrollPane1 = new JScrollPane(textPane1);
		scrollPane1.setPreferredSize(new Dimension(120, 100));
		JRibbonComponent text1 = new JRibbonComponent(scrollPane1);
		rowSpanBand.addRibbonComponent(text1, 3);

		JRibbonComponent text2 = new JRibbonComponent(new JTextField(
				resourceBundle.getString("OneRow.text"), 8));
		rowSpanBand.addRibbonComponent(text2, 1);

		JTextPane textPane3 = new JTextPane();
		textPane3.setText(resourceBundle.getString("TwoRows.text"));
		JScrollPane scrollPane3 = new JScrollPane(textPane3);
		scrollPane3.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text3 = new JRibbonComponent(scrollPane3);
		rowSpanBand.addRibbonComponent(text3, 2);

		JTextPane textPane4 = new JTextPane();
		textPane4.setText(resourceBundle.getString("TwoRows.text"));
		JScrollPane scrollPane4 = new JScrollPane(textPane4);
		scrollPane4.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text4 = new JRibbonComponent(scrollPane4);
		rowSpanBand.addRibbonComponent(text4, 2);

		JRibbonComponent text5 = new JRibbonComponent(new JTextField(
				resourceBundle.getString("OneRow.text"), 8));
		rowSpanBand.addRibbonComponent(text5, 1);

		JTextPane textPane6 = new JTextPane();
		textPane6.setText(resourceBundle.getString("TwoRows.text"));
		JScrollPane scrollPane6 = new JScrollPane(textPane6);
		scrollPane6.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text6 = new JRibbonComponent(scrollPane6);
		rowSpanBand.addRibbonComponent(text6, 2);

		JTextPane textPane7 = new JTextPane();
		textPane7.setText(resourceBundle.getString("TwoRows.text"));
		JScrollPane scrollPane7 = new JScrollPane(textPane7);
		scrollPane7.setPreferredSize(new Dimension(80, 100));
		JRibbonComponent text7 = new JRibbonComponent(scrollPane7);
		rowSpanBand.addRibbonComponent(text7, 2);

		rowSpanBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesNone(rowSpanBand));

		return rowSpanBand;
	}

	protected JRibbonBand getAlignmentBand() {
		JRibbonBand alignmentBand = new JRibbonBand(resourceBundle
				.getString("Alignment.textTaskTitle"),
				new format_justify_left(), null);

		alignmentBand.startGroup();

		JRibbonComponent wideWrapper1 = new JRibbonComponent(new JLabel(
				resourceBundle.getString("VeryLong.text")));
		alignmentBand.addRibbonComponent(wideWrapper1);

		JRibbonComponent alignLeadingWrapper1 = new JRibbonComponent(null,
				resourceBundle.getString("Leading.text"), new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		alignLeadingWrapper1
				.setHorizontalAlignment(HorizontalAlignment.LEADING);
		alignmentBand.addRibbonComponent(alignLeadingWrapper1);

		JRibbonComponent alignTrailingWrapper1 = new JRibbonComponent(null,
				resourceBundle.getString("Trailing.text"), new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		alignTrailingWrapper1
				.setHorizontalAlignment(HorizontalAlignment.TRAILING);
		alignmentBand.addRibbonComponent(alignTrailingWrapper1);

		JRibbonComponent wideWrapper2 = new JRibbonComponent(new JLabel(
				resourceBundle.getString("VeryLong.text")));
		alignmentBand.addRibbonComponent(wideWrapper2);

		JRibbonComponent alignCenterWrapper1 = new JRibbonComponent(null,
				resourceBundle.getString("Center.text"), new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		alignCenterWrapper1.setHorizontalAlignment(HorizontalAlignment.CENTER);
		alignmentBand.addRibbonComponent(alignCenterWrapper1);

		JRibbonComponent alignFillWrapper1 = new JRibbonComponent(null,
				resourceBundle.getString("Fill.text"), new JSpinner(
						new SpinnerNumberModel(0, 0, 100, 5)));
		alignFillWrapper1.setHorizontalAlignment(HorizontalAlignment.FILL);
		alignmentBand.addRibbonComponent(alignFillWrapper1);

		alignmentBand.startGroup();

		JRibbonComponent wideWrapper3 = new JRibbonComponent(new JLabel(
				resourceBundle.getString("Long.text")));
		alignmentBand.addRibbonComponent(wideWrapper3);

		JRibbonComponent alignLeadingWrapper2 = new JRibbonComponent(
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignLeadingWrapper2
				.setHorizontalAlignment(HorizontalAlignment.LEADING);
		alignmentBand.addRibbonComponent(alignLeadingWrapper2);

		JRibbonComponent alignTrailingWrapper2 = new JRibbonComponent(
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignTrailingWrapper2
				.setHorizontalAlignment(HorizontalAlignment.TRAILING);
		alignmentBand.addRibbonComponent(alignTrailingWrapper2);

		JRibbonComponent wideWrapper4 = new JRibbonComponent(new JLabel(
				resourceBundle.getString("Long.text")));
		alignmentBand.addRibbonComponent(wideWrapper4);

		JRibbonComponent alignCenterWrapper2 = new JRibbonComponent(
				new JSpinner(new SpinnerNumberModel(0, 0, 100, 5)));
		alignCenterWrapper2.setHorizontalAlignment(HorizontalAlignment.CENTER);
		alignmentBand.addRibbonComponent(alignCenterWrapper2);

		JRibbonComponent alignFillWrapper2 = new JRibbonComponent(new JSpinner(
				new SpinnerNumberModel(0, 0, 100, 5)));
		alignFillWrapper2.setHorizontalAlignment(HorizontalAlignment.FILL);
		alignmentBand.addRibbonComponent(alignFillWrapper2);

		return alignmentBand;
	}

	protected JRibbonBand getAnimationBand() {
		JRibbonBand animationBand = new JRibbonBand(resourceBundle
				.getString("Animation.textBandTitle"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));

		JCommandButton button1 = new JCommandButton(resourceBundle
				.getString("CustomAnimation.text"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));
		animationBand.addCommandButton(button1, RibbonElementPriority.TOP);
		JCommandButton button2 = new JCommandButton(resourceBundle
				.getString("CustomAnimation.text"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));
		animationBand.addCommandButton(button2, RibbonElementPriority.TOP);
		JCommandButton button3 = new JCommandButton(resourceBundle
				.getString("CustomAnimation.text"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 32, 32));
		animationBand.addCommandButton(button3, RibbonElementPriority.TOP);

		animationBand.setResizePolicies(CoreRibbonResizePolicies
				.getCorePoliciesNone(animationBand));

		return animationBand;
	}

	protected JRibbonBand getTransitionBand() {
		JRibbonBand transitionBand = new JRibbonBand(resourceBundle
				.getString("TransitionToThis.textBandTitle"),
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));

		Map<RibbonElementPriority, Integer> transitionGalleryVisibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
		transitionGalleryVisibleButtonCounts.put(RibbonElementPriority.LOW, 2);
		transitionGalleryVisibleButtonCounts.put(RibbonElementPriority.MEDIUM,
				4);
		transitionGalleryVisibleButtonCounts.put(RibbonElementPriority.TOP, 6);

		List<StringValuePair<List<JCommandToggleButton>>> transitionGalleryButtons = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();

		List<JCommandToggleButton> transitionGalleryButtonsList = new ArrayList<JCommandToggleButton>();
		for (int i = 1; i <= 40; i++) {
			final int index = i;
			ResizableIcon mainIcon = new appointment_new();
			ResizableIcon finalIcon = new DecoratedResizableIcon(mainIcon,
					new DecoratedResizableIcon.IconDecorator() {
						@Override
						public void paintIconDecoration(Component c,
								Graphics g, int x, int y, int width, int height) {
							Graphics2D g2d = (Graphics2D) g.create();
							RenderingUtils.installDesktopHints(g2d);
							g2d.setFont(UIManager.getFont("Label.font")
									.deriveFont(9.0f));
							g2d.setColor(Color.black);
							g2d.drawString("" + index, x + 1, y + height - 2);
							g2d.drawString("" + index, x + 3, y + height - 2);
							g2d.drawString("" + index, x + 2, y + height - 1);
							g2d.drawString("" + index, x + 2, y + height - 3);
							g2d.setColor(Color.white);
							g2d.drawString("" + index, x + 2, y + height - 2);
							g2d.dispose();
						}
					});
			JCommandToggleButton button = new JCommandToggleButton("",
					finalIcon);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Activated action " + index);
				}
			});
			button.setHorizontalAlignment(SwingConstants.CENTER);
			transitionGalleryButtonsList.add(button);
		}
		transitionGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>(
						resourceBundle
								.getString("TransitionGallery.textGroupTitle1"),
						transitionGalleryButtonsList));

		List<JCommandToggleButton> transitionGalleryButtonsList2 = new ArrayList<JCommandToggleButton>();
		for (int i = 41; i <= 70; i++) {
			final int index = i;
			ResizableIcon mainIcon = new appointment_new();
			ResizableIcon finalIcon = new DecoratedResizableIcon(mainIcon,
					new DecoratedResizableIcon.IconDecorator() {
						@Override
						public void paintIconDecoration(Component c,
								Graphics g, int x, int y, int width, int height) {
							Graphics2D g2d = (Graphics2D) g.create();
							RenderingUtils.installDesktopHints(g2d);
							g2d.setFont(UIManager.getFont("Label.font")
									.deriveFont(9.0f));
							g2d.setColor(Color.black);
							g2d.drawString("" + index, x + 1, y + height - 2);
							g2d.drawString("" + index, x + 3, y + height - 2);
							g2d.drawString("" + index, x + 2, y + height - 1);
							g2d.drawString("" + index, x + 2, y + height - 3);
							g2d.setColor(Color.white);
							g2d.drawString("" + index, x + 2, y + height - 2);
							g2d.dispose();
						}
					});
			JCommandToggleButton button = new JCommandToggleButton("",
					finalIcon);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Activated action " + index);
				}
			});
			button.setHorizontalAlignment(SwingConstants.CENTER);
			transitionGalleryButtonsList2.add(button);
		}
		transitionGalleryButtons
				.add(new StringValuePair<List<JCommandToggleButton>>(
						resourceBundle
								.getString("TransitionGallery.textGroupTitle2"),
						transitionGalleryButtonsList2));

		transitionBand.addRibbonGallery("Transitions",
				transitionGalleryButtons, transitionGalleryVisibleButtonCounts,
				6, 6, CommandButtonDisplayState.SMALL,
				RibbonElementPriority.TOP);

		transitionBand.startGroup();
		transitionBand.addRibbonComponent(new JRibbonComponent(
				new SimpleResizableIcon(RibbonElementPriority.TOP, 16, 16),
				resourceBundle.getString("Sound.text"), new JComboBox(
						new Object[] { "["
								+ resourceBundle.getString("NoSound.text")
								+ "]     " })));
		transitionBand.addRibbonComponent(new JRibbonComponent(null,
				resourceBundle.getString("Speed.text"), new JComboBox(
						new Object[] { resourceBundle.getString("Medium.text")
								+ "           " })));
		JCommandButton applyToAll = new JCommandButton(resourceBundle
				.getString("ApplyToAll.text"), new SimpleResizableIcon(
				RibbonElementPriority.TOP, 16, 16));
		applyToAll.setDisplayState(CommandButtonDisplayState.MEDIUM);
		applyToAll.setVGapScaleFactor(0.5);
		transitionBand.addRibbonComponent(new JRibbonComponent(applyToAll));

		return transitionBand;
	}

	protected JRibbonBand getTransitionNextBand() {
		JRibbonBand transitionBand = new JRibbonBand(resourceBundle
				.getString("TransitionToNext.textBandTitle"),
				new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));

		JCheckBox mouseClick = new JCheckBox(resourceBundle
				.getString("OnMouseClick.text"));
		mouseClick.setSelected(true);
		JRibbonComponent mouseClickWrapper = new JRibbonComponent(mouseClick);
		transitionBand.addRibbonComponent(mouseClickWrapper);

		JCheckBox autoAfter = new JCheckBox(resourceBundle
				.getString("AutoAfter.text"));
		JRibbonComponent autoAfterWrapper = new JRibbonComponent(autoAfter);
		transitionBand.addRibbonComponent(autoAfterWrapper);

		transitionBand.addRibbonComponent(new JRibbonComponent(
				new SimpleResizableIcon(RibbonElementPriority.TOP, 16, 16), "",
				new JSpinner(new SpinnerDateModel())));

		return transitionBand;
	}

	protected RibbonContextualTaskGroup group1;
	protected RibbonContextualTaskGroup group2;

	protected JPanel statusBar;

	private JRibbonBand paragraphBand;

	public GUI2() {
		super();
		setApplicationIcon(new applications_internet());
		currLocale = Locale.getDefault();
		resourceBundle = ResourceBundle.getBundle("test.resource.Resources",
				currLocale);
		setTitle(resourceBundle.getString("Frame.title"));
	}



	protected void configureTaskBar() {
		// taskbar components
		JCommandButton taskbarButtonPaste = new JCommandButton("",
				new edit_paste());
		taskbarButtonPaste
				.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		taskbarButtonPaste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Paste activated");
			}
		});
		taskbarButtonPaste.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				return new SamplePopupMenu();
			}
		});
		taskbarButtonPaste.setActionRichTooltip(new RichTooltip(resourceBundle
				.getString("Paste.text"), resourceBundle
				.getString("Paste.tooltip.actionParagraph1")));
		taskbarButtonPaste.setPopupRichTooltip(new RichTooltip(resourceBundle
				.getString("Paste.text"), resourceBundle
				.getString("Paste.tooltip.popupParagraph1")));
		taskbarButtonPaste.setActionKeyTip("1");
		this.getRibbon().addTaskbarComponent(taskbarButtonPaste);

		JCommandButton taskbarButtonClear = new JCommandButton("",
				new edit_clear());
		taskbarButtonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Clear activated");
			}
		});
		taskbarButtonClear.setEnabled(false);
		taskbarButtonClear.setActionKeyTip("2");
		this.getRibbon().addTaskbarComponent(taskbarButtonClear);

		JCommandButton taskbarButtonCopy = new JCommandButton("",
				new edit_copy());
		taskbarButtonCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Copy activated");
			}
		});
		taskbarButtonCopy.setActionKeyTip("3");
		this.getRibbon().addTaskbarComponent(taskbarButtonCopy);

		this.getRibbon().addTaskbarComponent(
				new JSeparator(JSeparator.VERTICAL));

		JCommandButton taskbarButtonFind = new JCommandButton("",
				new edit_find());
		taskbarButtonFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taskbar Find activated");
			}
		});
		taskbarButtonFind.setActionKeyTip("4");
		this.getRibbon().addTaskbarComponent(taskbarButtonFind);
	}

	protected void configureApplicationMenu() {
		RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(
				new document_new(),
				resourceBundle.getString("AppMenuNew.text"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked creating new document");
					}
				}, CommandButtonKind.ACTION_ONLY);
		amEntryNew.setActionKeyTip("N");

		RibbonApplicationMenuEntryPrimary amEntryOpen = new RibbonApplicationMenuEntryPrimary(
				new document_open(), resourceBundle
						.getString("AppMenuOpen.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked opening document");
					}
				}, CommandButtonKind.ACTION_ONLY);
		amEntryOpen
				.setRolloverCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
					@Override
					public void menuEntryActivated(JPanel targetPanel) {
						targetPanel.removeAll();
						JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
								CommandButtonDisplayState.MEDIUM);
						String groupName = resourceBundle
								.getString("AppMenuOpen.secondary.textGroupTitle1");
						openHistoryPanel.addButtonGroup(groupName);

						MessageFormat mf = new MessageFormat(resourceBundle
								.getString("AppMenuOpen.secondary.textButton"));
						mf.setLocale(currLocale);
						for (int i = 0; i < 5; i++) {
							JCommandButton historyButton = new JCommandButton(
									mf.format(new Object[] { i }),
									new text_html());
							historyButton
									.setHorizontalAlignment(SwingUtilities.LEFT);
							openHistoryPanel
									.addButtonToLastGroup(historyButton);
						}
						openHistoryPanel.setMaxButtonColumns(1);
						targetPanel.setLayout(new BorderLayout());
						targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
					}
				});
		amEntryOpen.setActionKeyTip("O");

		RibbonApplicationMenuEntryPrimary amEntrySave = new RibbonApplicationMenuEntryPrimary(
				new document_save(), resourceBundle
						.getString("AppMenuSave.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked saving document");
					}
				}, CommandButtonKind.ACTION_ONLY);
		amEntrySave.setEnabled(false);
		amEntrySave.setActionKeyTip("S");

		RibbonApplicationMenuEntryPrimary amEntrySaveAs = new RibbonApplicationMenuEntryPrimary(
				new document_save_as(), resourceBundle
						.getString("AppMenuSaveAs.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked saving document as");
					}
				}, CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		amEntrySaveAs.setActionKeyTip("A");
		amEntrySaveAs.setPopupKeyTip("F");

		RibbonApplicationMenuEntrySecondary amEntrySaveAsWord = new RibbonApplicationMenuEntrySecondary(
				new x_office_document(), resourceBundle
						.getString("AppMenuSaveAs.word.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySaveAsWord.setDescriptionText(resourceBundle
				.getString("AppMenuSaveAs.word.description"));
		amEntrySaveAsWord.setActionKeyTip("W");
		RibbonApplicationMenuEntrySecondary amEntrySaveAsHtml = new RibbonApplicationMenuEntrySecondary(
				new text_html(), resourceBundle
						.getString("AppMenuSaveAs.html.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySaveAsHtml.setDescriptionText(resourceBundle
				.getString("AppMenuSaveAs.html.description"));
		amEntrySaveAsHtml.setEnabled(false);
		amEntrySaveAsHtml.setActionKeyTip("H");
		RibbonApplicationMenuEntrySecondary amEntrySaveAsOtherFormats = new RibbonApplicationMenuEntrySecondary(
				new document_save_as(), resourceBundle
						.getString("AppMenuSaveAs.other.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySaveAsOtherFormats.setDescriptionText(resourceBundle
				.getString("AppMenuSaveAs.other.description"));
		amEntrySaveAsOtherFormats.setActionKeyTip("O");

		amEntrySaveAs
				.addSecondaryMenuGroup(resourceBundle
						.getString("AppMenuSaveAs.secondary.textGroupTitle1"),
						amEntrySaveAsWord, amEntrySaveAsHtml,
						amEntrySaveAsOtherFormats);

		RibbonApplicationMenuEntryPrimary amEntryPrint = new RibbonApplicationMenuEntryPrimary(
				new document_print(), resourceBundle
						.getString("AppMenuPrint.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked printing document");
					}
				}, CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		amEntryPrint.setActionKeyTip("P");
		amEntryPrint.setPopupKeyTip("W");

		RibbonApplicationMenuEntrySecondary amEntryPrintSelect = new RibbonApplicationMenuEntrySecondary(
				new printer(), resourceBundle
						.getString("AppMenuPrint.print.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintSelect.setDescriptionText(resourceBundle
				.getString("AppMenuPrint.print.description"));
		amEntryPrintSelect.setActionKeyTip("P");
		RibbonApplicationMenuEntrySecondary amEntryPrintDefault = new RibbonApplicationMenuEntrySecondary(
				new document_print(), resourceBundle
						.getString("AppMenuPrint.quick.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintDefault.setDescriptionText(resourceBundle
				.getString("AppMenuPrint.quick.description"));
		amEntryPrintDefault.setActionKeyTip("Q");
		RibbonApplicationMenuEntrySecondary amEntryPrintPreview = new RibbonApplicationMenuEntrySecondary(
				new document_print_preview(), resourceBundle
						.getString("AppMenuPrint.preview.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintPreview.setDescriptionText(resourceBundle
				.getString("AppMenuPrint.preview.description"));
		amEntryPrintPreview.setActionKeyTip("V");

		amEntryPrint.addSecondaryMenuGroup(resourceBundle
				.getString("AppMenuPrint.secondary.textGroupTitle1"),
				amEntryPrintSelect, amEntryPrintDefault, amEntryPrintPreview);

		RibbonApplicationMenuEntrySecondary amEntryPrintMemo = new RibbonApplicationMenuEntrySecondary(
				new text_x_generic(), resourceBundle
						.getString("AppMenuPrint.memo.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntryPrintMemo.setActionKeyTip("M");

		amEntryPrint.addSecondaryMenuGroup(resourceBundle
				.getString("AppMenuPrint.secondary.textGroupTitle2"),
				amEntryPrintMemo);

		RibbonApplicationMenuEntryPrimary amEntrySend = new RibbonApplicationMenuEntryPrimary(
				new mail_forward(), resourceBundle
						.getString("AppMenuSend.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked sending document");
					}
				}, CommandButtonKind.POPUP_ONLY);
		amEntrySend.setPopupKeyTip("D");

		RibbonApplicationMenuEntrySecondary amEntrySendMail = new RibbonApplicationMenuEntrySecondary(
				new mail_message_new(), resourceBundle
						.getString("AppMenuSend.email.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySendMail.setDescriptionText(resourceBundle
				.getString("AppMenuSend.email.description"));
		amEntrySendMail.setActionKeyTip("E");
		RibbonApplicationMenuEntrySecondary amEntrySendHtml = new RibbonApplicationMenuEntrySecondary(
				new text_html(), resourceBundle
						.getString("AppMenuSend.html.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySendHtml.setDescriptionText(resourceBundle
				.getString("AppMenuSend.html.description"));
		amEntrySendHtml.setActionKeyTip("H");
		RibbonApplicationMenuEntrySecondary amEntrySendDoc = new RibbonApplicationMenuEntrySecondary(
				new x_office_document(), resourceBundle
						.getString("AppMenuSend.word.text"), null,
				CommandButtonKind.ACTION_ONLY);
		amEntrySendDoc.setDescriptionText(resourceBundle
				.getString("AppMenuSend.word.description"));
		amEntrySendDoc.setActionKeyTip("W");
		RibbonApplicationMenuEntrySecondary amEntrySendWireless = new RibbonApplicationMenuEntrySecondary(
				new network_wireless(), resourceBundle
						.getString("AppMenuSend.wireless.text"), null,
				CommandButtonKind.POPUP_ONLY);
		amEntrySendWireless.setPopupKeyTip("X");

		amEntrySendWireless.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				JCommandPopupMenu wirelessChoices = new JCommandPopupMenu();

				JCommandMenuButton wiFiMenuButton = new JCommandMenuButton(
						resourceBundle
								.getString("AppMenuSend.wireless.wifi.text"),
						new EmptyResizableIcon(16));
				wiFiMenuButton.setActionKeyTip("W");
				wiFiMenuButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("WiFi activated");
					}
				});
				wirelessChoices.addMenuButton(wiFiMenuButton);

				JCommandMenuButton blueToothMenuButton = new JCommandMenuButton(
						resourceBundle
								.getString("AppMenuSend.wireless.bluetooth.text"),
						new EmptyResizableIcon(16));
				blueToothMenuButton.setActionKeyTip("B");
				blueToothMenuButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("BlueTooth activated");
					}
				});
				wirelessChoices.addMenuButton(blueToothMenuButton);
				return wirelessChoices;
			}
		});

		amEntrySendWireless.setDescriptionText(resourceBundle
				.getString("AppMenuSend.wireless.description"));

		amEntrySend.addSecondaryMenuGroup(resourceBundle
				.getString("AppMenuSend.secondary.textGroupTitle1"),
				amEntrySendMail, amEntrySendHtml, amEntrySendDoc,
				amEntrySendWireless);

		RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(
				new system_log_out(), resourceBundle
						.getString("AppMenuExit.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}, CommandButtonKind.ACTION_ONLY);
		amEntryExit.setActionKeyTip("X");

		RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
		applicationMenu.addMenuEntry(amEntryNew);
		applicationMenu.addMenuEntry(amEntryOpen);
		applicationMenu.addMenuEntry(amEntrySave);
		applicationMenu.addMenuEntry(amEntrySaveAs);
		applicationMenu.addMenuSeparator();
		applicationMenu.addMenuEntry(amEntryPrint);
		applicationMenu.addMenuEntry(amEntrySend);
		applicationMenu.addMenuSeparator();
		applicationMenu.addMenuEntry(amEntryExit);

		applicationMenu
				.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
					@Override
					public void menuEntryActivated(JPanel targetPanel) {
						targetPanel.removeAll();
						JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
								CommandButtonDisplayState.MEDIUM);
						String groupName = resourceBundle
								.getString("AppMenu.default.textGroupTitle1");
						openHistoryPanel.addButtonGroup(groupName);

						MessageFormat mf = new MessageFormat(resourceBundle
								.getString("AppMenu.default.textButton"));
						mf.setLocale(currLocale);
						for (int i = 0; i < 5; i++) {
							JCommandButton historyButton = new JCommandButton(
									mf.format(new Object[] { i }),
									new text_html());
							historyButton
									.setHorizontalAlignment(SwingUtilities.LEFT);
							openHistoryPanel
									.addButtonToLastGroup(historyButton);
						}
						openHistoryPanel.setMaxButtonColumns(1);
						targetPanel.setLayout(new BorderLayout());
						targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
					}
				});

		RibbonApplicationMenuEntryFooter amFooterProps = new RibbonApplicationMenuEntryFooter(
				new document_properties(), resourceBundle
						.getString("AppMenuOptions.text"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked Options");
					}
				});
		RibbonApplicationMenuEntryFooter amFooterExit = new RibbonApplicationMenuEntryFooter(
				new system_log_out(), resourceBundle
						.getString("AppMenuExit.text"), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
		amFooterExit.setEnabled(false);
		applicationMenu.addFooterEntry(amFooterProps);
		applicationMenu.addFooterEntry(amFooterExit);

		this.getRibbon().setApplicationMenu(applicationMenu);

		RichTooltip appMenuRichTooltip = new RichTooltip();
		appMenuRichTooltip.setTitle(resourceBundle
				.getString("AppMenu.tooltip.title"));
		appMenuRichTooltip.addDescriptionSection(resourceBundle
				.getString("AppMenu.tooltip.paragraph1"));
		try {
			appMenuRichTooltip
					.setMainImage(ImageIO
							.read(GUI2.class
									.getResource("/test/ribbon/appmenubutton-tooltip-main.png")));
			appMenuRichTooltip.setFooterImage(ImageIO
					.read(GUI2.class
							.getResource("/test/ribbon/help-browser.png")));
		} catch (IOException ioe) {
		}
		appMenuRichTooltip.addFooterSection(resourceBundle
				.getString("AppMenu.tooltip.footer1"));
		this.getRibbon().setApplicationMenuRichTooltip(appMenuRichTooltip);
		this.getRibbon().setApplicationMenuKeyTip("F");
	}

	protected RibbonTask getContextualRibbonTask(String title, String keyTip) {
		JRibbonBand actionBand = this.getActionBand();
		JRibbonBand arrangeBand = this.getApplicationsBand();
		JRibbonBand previewBand = this.getPreviewBand();
		JRibbonBand transitionBand = this.getTransitionBand();
		RibbonTask task = new RibbonTask(title, actionBand, arrangeBand,
				previewBand, transitionBand);
		task.setKeyTip(keyTip);
		return task;
	}

	protected void configureControlPanel(DefaultFormBuilder builder) {
		final JCheckBox group1Visible = new JCheckBox("visible");
		final JCheckBox group2Visible = new JCheckBox("visible");
		group1Visible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						getRibbon().setVisible(group1,
								group1Visible.isSelected());
					}
				});
			}
		});
		group2Visible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						getRibbon().setVisible(group2,
								group2Visible.isSelected());
					}
				});
			}
		});
		builder.append("Group 1", group1Visible);
		builder.append("Group 2", group2Visible);

		builder.append("Look & feel", LookAndFeelSwitcher
				.getLookAndFeelSwitcher(this));

		final JCheckBox appMenuVisible = new JCheckBox("visible");
		appMenuVisible.setSelected(true);
		appMenuVisible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!appMenuVisible.isSelected())
							getRibbon().setApplicationMenu(null);
						else
							configureApplicationMenu();
					}
				});
			}
		});
		builder.append("App menu", appMenuVisible);

		final JCheckBox taskbarFull = new JCheckBox("full");
		taskbarFull.setSelected(true);
		taskbarFull.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!taskbarFull.isSelected()) {
							List<Component> taskbarComps = new ArrayList<Component>(
									getRibbon().getTaskbarComponents());
							for (Component taskbarComp : taskbarComps) {
								getRibbon().removeTaskbarComponent(taskbarComp);
							}
						} else {
							configureTaskBar();
						}
						repaint();
					}
				});
			}
		});
		builder.append("Taskbar", taskbarFull);

		JButton changeParagraph = new JButton("change");
		changeParagraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						paragraphBand.setGroupTitle(0, getRandomString(5, 15));
						paragraphBand.getRibbonComponents(0).get(0).setCaption(
								getRandomString(4, 10));
					}

					private String getRandomString(int minLength, int maxLength) {
						Random random = new Random();
						int len = minLength
								+ random.nextInt(maxLength - minLength);
						String newTitle = new String();
						String letters = "abcdefghijklmnopqrstubvwxyz       ";
						for (int i = 0; i < len; i++) {
							newTitle += letters.charAt(random.nextInt(letters
									.length()));
						}
						return newTitle;
					}
				});
			}
		});
		builder.append("Change 'Paragraph'", changeParagraph);
	}

	protected JFlowRibbonBand getFontBand() {
		JFlowRibbonBand fontBand = new JFlowRibbonBand(resourceBundle
				.getString("Font.textBandTitle"),
				new preferences_desktop_font(), new ExpandActionListener());
		fontBand.setExpandButtonKeyTip("FN");
		fontBand.setCollapsedStateKeyTip("ZF");

		JComboBox fontCombo = new JComboBox(new Object[] {
				"+ Minor (Calibri)   ", "+ Minor (Columbus)   ",
				"+ Minor (Consolas)   ", "+ Minor (Cornelius)   ",
				"+ Minor (Cleopatra)   ", "+ Minor (Cornucopia)   ",
				"+ Minor (Candella)   ", "+ Minor (Cambria)   " });
		JRibbonComponent fontComboWrapper = new JRibbonComponent(fontCombo);
		fontComboWrapper.setKeyTip("SF");
		fontBand.addFlowComponent(fontComboWrapper);

		JComboBox sizeCombo = new JComboBox(new Object[] { "11  " });
		JRibbonComponent sizeComboWrapper = new JRibbonComponent(sizeCombo);
		sizeComboWrapper.setKeyTip("SS");
		fontBand.addFlowComponent(sizeComboWrapper);

		JCommandButtonStrip indentStrip = new JCommandButtonStrip();

		JCommandButton indentLeftButton = new JCommandButton("",
				new format_indent_less());
		indentLeftButton.setActionKeyTip("AO");
		indentStrip.add(indentLeftButton);

		JCommandButton indentRightButton = new JCommandButton("",
				new format_indent_more());
		indentRightButton.setActionKeyTip("AI");
		indentStrip.add(indentRightButton);

		fontBand.addFlowComponent(indentStrip);

		JCommandButtonStrip styleStrip = new JCommandButtonStrip();

		JCommandToggleButton styleBoldButton = new JCommandToggleButton("",
				new format_text_bold());
		styleBoldButton.getActionModel().setSelected(true);
		styleBoldButton.setActionRichTooltip(new RichTooltip(resourceBundle
				.getString("FontBold.tooltip.textActionTitle"), resourceBundle
				.getString("FontBold.tooltip.textActionParagraph1")));
		styleBoldButton.setActionKeyTip("1");
		styleStrip.add(styleBoldButton);

		JCommandToggleButton styleItalicButton = new JCommandToggleButton("",
				new format_text_italic());
		styleItalicButton.setActionRichTooltip(new RichTooltip(resourceBundle
				.getString("FontItalic.tooltip.textActionTitle"),
				resourceBundle
						.getString("FontItalic.tooltip.textActionParagraph1")));
		styleItalicButton.setActionKeyTip("2");
		styleStrip.add(styleItalicButton);

		JCommandToggleButton styleUnderlineButton = new JCommandToggleButton(
				"", new format_text_underline());
		styleUnderlineButton
				.setActionRichTooltip(new RichTooltip(
						resourceBundle
								.getString("FontUnderline.tooltip.textActionTitle"),
						resourceBundle
								.getString("FontUnderline.tooltip.textActionParagraph1")));
		styleUnderlineButton.setActionKeyTip("3");
		styleStrip.add(styleUnderlineButton);

		JCommandToggleButton styleStrikeThroughButton = new JCommandToggleButton(
				"", new format_text_strikethrough());
		styleStrikeThroughButton
				.setActionRichTooltip(new RichTooltip(
						resourceBundle
								.getString("FontStrikethrough.tooltip.textActionTitle"),
						resourceBundle
								.getString("FontStrikethrough.tooltip.textActionParagraph1")));
		styleStrikeThroughButton.setActionKeyTip("4");
		styleStrip.add(styleStrikeThroughButton);

		fontBand.addFlowComponent(styleStrip);

		JCommandButtonStrip alignStrip = new JCommandButtonStrip();
		CommandToggleButtonGroup alignGroup = new CommandToggleButtonGroup();

		JCommandToggleButton alignLeftButton = new JCommandToggleButton("",
				new format_justify_left());
		alignLeftButton.setActionKeyTip("AL");
		alignLeftButton.getActionModel().setSelected(true);
		alignGroup.add(alignLeftButton);
		alignStrip.add(alignLeftButton);

		JCommandToggleButton alignCenterButton = new JCommandToggleButton("",
				new format_justify_center());
		alignCenterButton.setActionKeyTip("AC");
		alignGroup.add(alignCenterButton);
		alignStrip.add(alignCenterButton);

		JCommandToggleButton alignRightButton = new JCommandToggleButton("",
				new format_justify_right());
		alignRightButton.setActionKeyTip("AR");
		alignGroup.add(alignRightButton);
		alignStrip.add(alignRightButton);

		JCommandToggleButton alignFillButton = new JCommandToggleButton("",
				new format_justify_fill());
		alignFillButton.setActionKeyTip("AF");
		alignGroup.add(alignFillButton);
		alignStrip.add(alignFillButton);

		fontBand.addFlowComponent(alignStrip);

		return fontBand;
	}

	/**
	 * Main method for testing.
	 *
	 * @param args
	 *            Ignored.
	 **/

	protected void configureStatusBar() {
		statusBar = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		JLabel helper = new JLabel("Right click to show menu");
		statusBar.add(helper);

		JCommandButtonStrip alignStrip = new JCommandButtonStrip();
		CommandToggleButtonGroup alignGroup = new CommandToggleButtonGroup();

		JCommandToggleButton alignLeftButton = new JCommandToggleButton("",
				new format_justify_left());
		alignLeftButton.getActionModel().setSelected(true);
		alignGroup.add(alignLeftButton);
		alignStrip.add(alignLeftButton);

		JCommandToggleButton alignCenterButton = new JCommandToggleButton("",
				new format_justify_center());
		alignGroup.add(alignCenterButton);
		alignStrip.add(alignCenterButton);

		JCommandToggleButton alignRightButton = new JCommandToggleButton("",
				new format_justify_right());
		alignGroup.add(alignRightButton);
		alignStrip.add(alignRightButton);

		JCommandToggleButton alignFillButton = new JCommandToggleButton("",
				new format_justify_fill());
		alignGroup.add(alignFillButton);
		alignStrip.add(alignFillButton);

		statusBar.add(alignStrip);

		final Map<Integer, Boolean> selection = new TreeMap<Integer, Boolean>();
		statusBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					processPopup(e);
				}
			};

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					processPopup(e);
				}
			}

			private void processPopup(MouseEvent e) {
				Point pt = new Point(e.getPoint());
				SwingUtilities.convertPointToScreen(pt, statusBar);
				final JCommandPopupMenu menu = new JCommandPopupMenu();
				for (int i = 0; i < 10; i++) {
					final int ind = i;
					final JCommandToggleMenuButton button = new JCommandToggleMenuButton(
							"option " + i, null);
					button.getActionModel().addActionListener(
							new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									selection.put(ind, button.getActionModel()
											.isSelected());
								}
							});
					if (selection.containsKey(i)) {
						button.getActionModel().setSelected(selection.get(i));
					}
					menu.addMenuButton(button);
					if (i == 3 || i == 7) {
						menu.addMenuSeparator();
					}
				}
				menu.setToDismissOnChildClick(false);

				Popup popup = PopupFactory.getSharedInstance().getPopup(
						statusBar, menu, pt.x,
						pt.y - menu.getPreferredSize().height);
				PopupPanelManager.defaultManager().addPopup(statusBar, popup,
						menu);

				PopupListener tracker = new PopupListener() {
					@Override
					public void popupShown(PopupEvent event) {
					}

					@Override
					public void popupHidden(PopupEvent event) {
						if (event.getSource() == menu) {
							System.out.print("Current selection: ");
							for (Map.Entry<Integer, Boolean> e : selection
									.entrySet()) {
								if (e.getValue()) {
									System.out.print(e.getKey() + " ");
								}
							}
							System.out.println();
						}
						PopupPanelManager.defaultManager().removePopupListener(
								this);
					}
				};
				PopupPanelManager.defaultManager().addPopupListener(tracker);
			}
		});

		this.add(statusBar, BorderLayout.SOUTH);
	}

	public static class RulerPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g.create();
			RenderingUtils.installDesktopHints(g2d);
			g2d.setColor(Color.gray);

			if (getComponentOrientation().isLeftToRight()) {
				// horizontal ruler on top
				int offset = 20;
				for (int i = offset; i < this.getWidth(); i += 10) {
					if ((i - offset) % 100 == 0)
						continue;
					g2d.drawLine(i, 9, i, 11);
				}
				for (int i = offset + 50; i < this.getWidth(); i += 100) {
					g2d.drawLine(i, 7, i, 13);
				}
				for (int i = offset; i < this.getWidth(); i += 100) {
					int c = ((i - offset) / 100) % 10;
					g2d.drawString("" + c, i - 2, 15);
				}

				// vertical ruler on left
				for (int i = offset; i < this.getHeight(); i += 10) {
					if ((i - offset) % 100 == 0)
						continue;
					g2d.drawLine(9, i, 11, i);
				}
				for (int i = offset + 50; i < this.getHeight(); i += 100) {
					g2d.drawLine(7, i, 13, i);
				}
				for (int i = offset; i < this.getHeight(); i += 100) {
					int c = ((i - offset) / 100) % 10;
					g2d.drawString("" + c, 8, i + 4);
				}
			} else {
				// horizontal ruler on top
				int offset = 20;
				for (int i = getWidth() - offset; i > 0; i -= 10) {
					if ((getWidth() - offset - i) % 100 == 0)
						continue;
					g2d.drawLine(i, 9, i, 11);
				}
				for (int i = getWidth() - offset - 50; i > 0; i -= 100) {
					g2d.drawLine(i, 7, i, 13);
				}
				for (int i = getWidth() - offset; i > 0; i -= 100) {
					int c = ((getWidth() - offset - i) / 100) % 10;
					g2d.drawString("" + c, i - 2, 15);
				}

				// vertical ruler on right
				for (int i = offset; i < this.getHeight(); i += 10) {
					if ((i - offset) % 100 == 0)
						continue;
					g2d.drawLine(getWidth() - 9, i, getWidth() - 11, i);
				}
				for (int i = offset + 50; i < this.getHeight(); i += 100) {
					g2d.drawLine(getWidth() - 7, i, getWidth() - 13, i);
				}
				for (int i = offset; i < this.getHeight(); i += 100) {
					int c = ((i - offset) / 100) % 10;
					g2d.drawString("" + c, getWidth() - 14, i + 4);
				}
			}
			g2d.dispose();
		}
	}
}
