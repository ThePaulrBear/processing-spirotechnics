package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.*;

import paul.wintz.userinterface.optiontypes.EventOption;

@SuppressWarnings("serial")
class EventButtonPanel extends OptionPanel<EventOption> {

	EventButtonPanel(JPanel parentPanel, final EventOption option) {
		super(parentPanel, option);

		JMenuItem menuItem = new JMenuItem(option.getDescription());
		menuItem.addActionListener(arg0 -> option.triggerEvent());

	}

	private class EventButton extends JButton {
		public EventButton(final EventOption option) {
			setText(option.getDescription());
			setAlignmentX(CENTER_ALIGNMENT);

			addChangeListener(event -> {
				if(getModel().isPressed()) {
					option.triggerEvent();
				};
			});
		}
	}

	@Override
	protected void createControl() {
		final JButton button = new EventButton(option);
		this.add(button);

	}

	@Override
	protected void createLabel() {
		super.createLabel();
		label.setVisible(false);
	}

}