package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.*;

import javax.swing.*;

import paul.wintz.uioptiontypes.*;
import paul.wintz.uioptiontypes.events.EventOption;
import paul.wintz.uioptiontypes.integers.NumberOption;
import paul.wintz.utils.exceptions.UnhandledCaseException;

@SuppressWarnings("serial")
abstract class OptionPanel<T extends UserInputOption<?>> extends JPanel {
	protected final T option;

	protected JLabel label;

	OptionPanel(JPanel parentPanel, T option) {
		this.option = checkNotNull(option);

		parentPanel.add(this);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		createLabel();
		updateLabel();
		createControl();

		final Component spacer = Box.createVerticalStrut(10);
		this.add(spacer);
	}

	protected void createLabel() {
		label = new JLabel(option.getDescription());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setMinimumSize(new Dimension(3000, 0));
		this.add(label);
	}

	protected abstract void createControl();

	protected void updateLabel() {
		label.setText(option.getDescription());
	}

	protected void updateLabelWithValue() {
		final StringBuilder sb = new StringBuilder(option.getDescription());
		if (option.getValue() != null) {
			sb.append(": ").append(option.getValue());
		}
		label.setText(sb.toString());
	}

	public static OptionPanel<?> makeOptionPanel(UserInputOption<?> option, OptionsGroupPanel parent) {
		checkNotNull(option, "Option was null");
		checkNotNull(parent, "Parent panel was null");

		if (option instanceof NumberOption)
			return new NumberOptionPanel(parent, (NumberOption<?>) option);
		if (option instanceof BooleanOption)
			return new ToggleOptionPanel(parent, (BooleanOption) option);
		if (option instanceof FractionOption)
			return new FractionOptionPanel(parent, (FractionOption) option);
		if (option instanceof EventOption)
			return new EventButtonPanel(parent, (EventOption) option);
		if (option instanceof ListOption<?>)
			return new ListOptionPanel<>(parent, (ListOption<?>) option);

		throw new UnhandledCaseException(option.getClass());
	}

}