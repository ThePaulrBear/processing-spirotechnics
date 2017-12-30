package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import paul.wintz.uioptiontypes.*;

@SuppressWarnings("serial")
class OptionsGroupPanel extends JPanel {
	public static final int WIDTH = 400;
	private final JLabel jLabel;

	private final OptionGroup optionGroup;

	OptionsGroupPanel(JPanel parent, OptionGroup optionGroup) {
		this.optionGroup = optionGroup;

		setMaximumSize(new Dimension(WIDTH, Integer.MAX_VALUE));
		setMinimumSize(new Dimension(WIDTH, 0));

		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		parent.setAlignmentY(TOP_ALIGNMENT);

		jLabel = new JLabel(optionGroup.getDescription());
		jLabel.setAlignmentX(CENTER_ALIGNMENT);

		this.add(jLabel);
		parent.add(this);

		refreshList();

		optionGroup.addListChangeListener(this::refreshList);
	}

	public void add(OptionItem o) {
		checkNotNull(o);

		if (o instanceof OptionGroup) {
			new OptionsGroupPanel(this, (OptionGroup) o);
		} else if(o instanceof UserInputOption) {
			OptionPanel.makeOptionPanel((UserInputOption<?>) o, this);
		} else
			throw new ClassCastException("Wrong type: " + this);

	}

	/**
	 *
	 * @param afterNdx
	 *            the first item to delete/replace
	 */
	private void refreshList() {

		removeAll();
		for (final OptionItem option : optionGroup) {
			add(option);
		}

		revalidate();
	}

}