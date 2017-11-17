package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.*;

import javax.swing.*;

import paul.wintz.userinterface.HasValue;
import paul.wintz.userinterface.optiontypes.*;

@SuppressWarnings("serial")
abstract class OptionPanel<T extends UserInputOption> extends JPanel {
	protected final T option;

	protected JLabel label;

	OptionPanel(JPanel parentPanel, T option) {
		super();
		this.option = option;

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
		final StringBuilder sb = new StringBuilder(option.getDescription());
		if (option instanceof HasValue<?>) {
			sb.append(": ").append(((HasValue<?>) option).getValue().toString());
		}
		label.setText(sb.toString());
	}

	public static OptionPanel<?> makeOptionPanel(UserInputOption option, OptionsGroupPanel parent) {
		checkNotNull(option, "Option was null");
		checkNotNull(parent, "Parent panel was null");

		if (option instanceof IntegerRangeOption)
			return new SliderOptionPanel(parent, (IntegerRangeOption) option);
		else if (option instanceof NumberOption)
			return new NumberOptionPanel(parent, (NumberOption) option);
		else if (option instanceof BooleanOption)
			return new ToggleOptionPanel(parent, (BooleanOption) option);
		else if (option instanceof FractionOption)
			return new FractionOptionPanel(parent, (FractionOption) option);
		else if (option instanceof EventOption)
			return new EventButtonPanel(parent, (EventOption) option);
		else if (option instanceof ListOption<?>)
			return new ListOptionPanel<>(parent, (ListOption<?>) option);

		throw new RuntimeException("Option type not supported: " + option.getClass().getSuperclass());
	}

}