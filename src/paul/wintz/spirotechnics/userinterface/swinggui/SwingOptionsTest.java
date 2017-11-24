package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.EventQueue;

import paul.wintz.uioptiontypes.*;
import paul.wintz.uioptiontypes.integers.*;
import paul.wintz.utils.logging.Lg;

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
						makeTestOption(),
						new BooleanOption(true, "Boolean Option")))
				);
		optionList.addOptions(new OptionGroup("Tab 2",
				new OptionGroup("Option Group 2",
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption()),
				new OptionGroup("Option Group 2",
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption(),
						makeTestOption()))
				);
		return optionList;
	}


	private static NumberOption makeTestOption() {
		return IntegerOption.builder().description("Test").initial(1).range(0, 5).build();
	}


}
