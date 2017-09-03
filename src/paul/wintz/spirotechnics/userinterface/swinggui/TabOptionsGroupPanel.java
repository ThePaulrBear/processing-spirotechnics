package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import paul.wintz.userinterface.optiontypes.*;

@SuppressWarnings("serial")
class TabOptionsGroupPanel extends JPanel {

	TabOptionsGroupPanel(JPanel parent, JTabbedPane tabPane, OptionGroup optionList, JMenu eventMenu) {

		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setAlignmentY(TOP_ALIGNMENT);
		parent.setAlignmentY(TOP_ALIGNMENT);
		parent.add(this);
		tabPane.addTab(optionList.getDescription(), null, this, null);

		addList(optionList, eventMenu);
	}

	public void addList(OptionGroup optionList, JMenu eventMenu) {
		for (final OptionItem o : optionList) {
			add(o, eventMenu);
		}
	}

	public void add(OptionItem o, JMenu eventMenu) {
		if (o instanceof OptionGroup) {
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

		if (opt instanceof IntegerRangeOption) {
			new SliderOptionPanel(this, (IntegerRangeOption) opt);
		} else if (opt instanceof NumberOption) {
			new NumberOptionPanel(this, (NumberOption) opt);
		} else if (opt instanceof EventOption) {
			new EventMenuItem((EventOption) opt, eventMenu);
		} else if (opt instanceof BooleanOption) {
			new ToggleOptionPanel(this, (BooleanOption) opt);
		} else if (opt instanceof FractionOption) {
			new FractionOptionPanel(this, (FractionOption) opt);
		} else if (opt instanceof ListOption<?>) {
			new ListOptionPanel<>(this, (ListOption<?>) opt);
		} else {
			System.err.println("Option not supported by GUI: " + opt.getClass().getSimpleName());
		}
	}

	public void updateListExceptFirst(OptionGroup optionList) {
		while (getComponentCount() > 1) {
			this.remove(1);
		}
		addList(optionList, null);
	}
}