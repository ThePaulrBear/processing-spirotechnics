package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import paul.wintz.userinterface.optiontypes.*;
import paul.wintz.userinterface.optiontypes.OptionGroup.OnListChangeListener;

@SuppressWarnings("serial")
class OptionsGroupPanel extends JPanel {
	public static final int WIDTH = 400;
	private final JLabel jLabel;

	private final OptionGroup optionGroup;
	private final JMenu eventMenu;

	OptionsGroupPanel(JPanel parent, OptionGroup optionGroup, JMenu eventMenu) {
		this.optionGroup = optionGroup;
		this.eventMenu = eventMenu;

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

		updateList(0);

		optionGroup.addListChangeListener(new OnListChangeListener() {

			@Override
			public void onListChange(int firstIndexChanged) {
				updateList(firstIndexChanged);
			}

		});
	}

	public void add(OptionItem o) {

		if (o instanceof OptionGroup) {
			new OptionsGroupPanel(this, (OptionGroup) o, eventMenu);
		} else if (o instanceof EventOption) {
			new EventMenuItem((EventOption) o, eventMenu);
		} else if(o instanceof UserInputOption){
			OptionPanel.makeOptionPanel((UserInputOption) o, this);
		} else if(o == null)
			throw new NullPointerException();
		else
			throw new ClassCastException("Wrong type: " + this);

	}

	/**
	 *
	 * @param firstIndexChanged TODO
	 * @param afterNdx
	 *            the first item to delete/replace
	 */
	private void updateList(int firstIndexChanged) {

		int lastIndex = getComponentCount() - 1;
		while (lastIndex >= firstIndexChanged) {
			remove(lastIndex);
			lastIndex = getComponentCount() - 1;
		}
		for (int i = firstIndexChanged; i < optionGroup.size(); i++) {
			add(optionGroup.get(i));
		}


	}

}