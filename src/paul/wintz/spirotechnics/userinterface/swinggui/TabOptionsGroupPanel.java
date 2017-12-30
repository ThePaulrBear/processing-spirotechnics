package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;
import static paul.wintz.utils.logging.Lg.makeTAG;

import java.awt.FlowLayout;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import paul.wintz.uioptiontypes.*;
import paul.wintz.uioptiontypes.events.EventOption;
import paul.wintz.uioptiontypes.integers.IntegerOption;
import paul.wintz.utils.logging.Lg;

@SuppressWarnings("serial")
class TabOptionsGroupPanel extends JPanel {
	private static final String TAG = makeTAG(TabOptionsGroupPanel.class);

	public TabOptionsGroupPanel(JPanel parent, JTabbedPane tabPane, OptionGroup optionList) {

		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setAlignmentY(TOP_ALIGNMENT);
		parent.setAlignmentY(TOP_ALIGNMENT);
		parent.add(this);
		tabPane.addTab(optionList.getDescription(), null, this, null);

		addList(optionList);
	}

	public void addList(final OptionGroup optionList) {
		for (final OptionItem option : optionList) {
			add(option);
		}
	}

	public void add(OptionItem o) {
		if (o instanceof OptionGroup) {
			new OptionsGroupPanel(this, (OptionGroup) o);
		} else if (o instanceof UserInputOption) {
			addOption((UserInputOption<?>) o);
		}
	}

	private void addOption(@Nonnull UserInputOption<?> opt) {
		checkNotNull(opt);

		if (opt instanceof IntegerOption) {
			new NumberOptionPanel(this, (IntegerOption) opt);
		} else if (opt instanceof EventOption) {
			new EventButtonPanel(this, (EventOption) opt);
		} else if (opt instanceof BooleanOption) {
			new ToggleOptionPanel(this, (BooleanOption) opt);
		} else if (opt instanceof FractionOption) {
			new FractionOptionPanel(this, (FractionOption) opt);
		} else if (opt instanceof ListOption<?>) {
			new ListOptionPanel<>(this, (ListOption<?>) opt);
		} else if (opt instanceof FileOption) {
			new FileOptionPanel(this, (FileOption) opt);
		} else {
			Lg.e(TAG, "Option not supported by GUI: " + opt.getClass().getSimpleName());
		}
	}

	public void updateListExceptFirst(OptionGroup optionList) {
		while (getComponentCount() > 1) {
			this.remove(1);
		}
		addList(optionList);
	}
}