package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.EventQueue;

import paul.wintz.logging.Lg;
import paul.wintz.userinterface.optiontypes.*;

public class SwingOptionsTest {

	private static final String TAG = Lg.makeTAG(SwingOptionsTest.class);


	// For testing
	public static void main(String... args) {

		final OptionGroup optionList = makeOptions();

		EventQueue.invokeLater(() -> {
			try {
				final OptionsJFrame frame = new OptionsJFrame(optionList);
				frame.setVisible(true);
			} catch (final Exception e) {
				Lg.e(TAG, "Cannot create OptionsJFrame", e);
			}
		});

	}


	private static OptionGroup makeOptions() {
		final OptionGroup optionList = new OptionGroup();
		optionList.addOptions(new OptionGroup("Tab 1",
				new OptionGroup("Option Group 1",
						new IntegerRangeOption(5, 20, 100, "Slider 1"),
						new BooleanOption(true, "Boolean Option")))
				);
		optionList.addOptions(new OptionGroup("Tab 2",
				new OptionGroup("Option Group 2",
						new IntegerRangeOption(1, 0, 10, "Slider 2"),
						new IntegerRangeOption(1, 0, 5, "Slider 3"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider")),
				new OptionGroup("Option Group 2",
						new IntegerRangeOption(1, 0, 10, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider"),
						new IntegerRangeOption(1, 0, 5, "Slider")))
				);
		return optionList;
	}


}
