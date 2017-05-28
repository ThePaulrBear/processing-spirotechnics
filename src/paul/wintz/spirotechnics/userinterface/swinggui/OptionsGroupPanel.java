package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import paul.wintz.spirotechnics.modes.ModeList;
import paul.wintz.userinterface.optiontypes.EventOption;
import paul.wintz.userinterface.optiontypes.OptionGroup;
import paul.wintz.userinterface.optiontypes.OptionItem;
import paul.wintz.userinterface.optiontypes.UserInputOption;

@SuppressWarnings("serial")
class OptionsGroupPanel extends JPanel {
	public static final int WIDTH = 400;
	private final JLabel jLabel;

	OptionsGroupPanel(JPanel parent, OptionGroup optionList, JMenu eventMenu) {
		setMaximumSize(new Dimension(WIDTH, Integer.MAX_VALUE));
		setMinimumSize(new Dimension(WIDTH, 0));

		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		parent.setAlignmentY(TOP_ALIGNMENT);

		jLabel = new JLabel(optionList.getDescription());
		jLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(jLabel);
		parent.add(this);

		updateList(optionList, 1, eventMenu);
	}

	private void addList(OptionGroup optionList, JMenu eventMenu) {
		for (final OptionItem o : optionList) {
			add(o, eventMenu);
		}
	}

	public void add(OptionItem o, JMenu eventMenu) {
		if (o instanceof ModeList) {
			new ModeListPanel(this, (ModeList<?>) o);
		} else if (o instanceof OptionGroup) {
			new OptionsGroupPanel(this, (OptionGroup) o, eventMenu);
		} else if (o instanceof UserInputOption) {
			addOption((UserInputOption) o, eventMenu);
		}
	}

	/**
	 * @param eventMenu
	 * @param options
	 */
	private void addOption(UserInputOption opt, JMenu eventMenu) {
		if (opt == null)
			return;

		if (opt instanceof EventOption) {
			new EventMenuItem((EventOption) opt, eventMenu);
		} else {
			OptionPanel.makeOptionPanel(opt, this);
		}
	}

	/**
	 *
	 * @param optionList
	 * @param afterNdx
	 *            the first item to delete/replace
	 * @param eventMenu
	 */
	public void updateList(OptionGroup optionList, int afterNdx, JMenu eventMenu) {
		while (getComponentCount() > afterNdx) {
			this.remove(afterNdx);
		}

		addList(optionList, eventMenu);

	}
}