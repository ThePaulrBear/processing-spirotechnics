package paul.wintz.processingspirotechnics;

import java.util.Random;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import paul.wintz.canvas.Painter;
import processing.core.*;

public class PGraphicsCanvasTest {

	private static TestPApplet testPApplet;

	private PGraphicsCanvas canvas;
	private final Random random = new Random();
	private final Painter painter = new Painter();

	private static final class TestPApplet extends PApplet {

		@Override
		public void settings() {
			size(4, 4);
		}

		@Override
		public void setup() {
			new ProcessingUtils(this);
		}

		public final static void run(){
			testPApplet = new TestPApplet();
			PApplet.runSketch(new String[] { "TestPApplet" }, testPApplet);

			while(!ProcessingUtils.isInititialized()){
				//wait for the testPapplet to initialize
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		if(!ProcessingUtils.isInititialized()){
			TestPApplet.run();
		}

		canvas = new PGraphicsCanvas(40, 50);
	}

	@After
	public void tearDown() throws Exception {
		testPApplet.exit();
	}

	@Test
	public final void testPGraphicsCanvas() {
		final int width = 40;
		final int height = 50;
		final PGraphicsCanvas newCanvas = new PGraphicsCanvas(width, height);
		newCanvas.clear();
		assertEquals(width, newCanvas.getWidth());
		assertEquals(height, newCanvas.getHeight());
	}

	private static void assertIllegalArgumentException(final Exception e) {
		assertEquals(e.getMessage(), IllegalArgumentException.class, e.getClass());
	}


	@Test
	public final void testClear() {
		canvas.clear();
	}

	@Test
	public final void testBackground() {
		for(int i = 0; i < 10; i++){
			painter.setFill(random.nextInt());
			canvas.background(painter);
			//			final PGraphics image = layeredCanvas.getImage();
			//			image.loadPixels();
			//
			//			assertEquals("Failed on " + i + "th pass", backgroundPainter.getFill(), image.pixels[0]);
		}
	}

	@Test
	public final void testSetSize() {
		for(int width = 1; width < 1002; width += 500){
			for(int height = 1; height < 1002; height += 500){
				canvas.setSize(width, height);
				assertSize(width, height, canvas);
				assertSize(width, height, canvas.getImage());
			}
		}

		testIllegalSizeArgument(0, 10);
		testIllegalSizeArgument(10, 0);
		testIllegalSizeArgument(-20, 40);
		testIllegalSizeArgument(50, -3);
		testIllegalSizeArgument(-3, -3);
		testIllegalSizeArgument(0, 0);
	}

	private void testIllegalSizeArgument(int width, int height){
		if(width > 0 && height > 0)
			throw new IllegalArgumentException("At least one of the dimensions must be invalid (<1)");

		try{
			canvas.setSize(width, height);
		} catch (final Exception e){
			assertIllegalArgumentException(e);
		}
	}


	@Test
	public final void testCreateLayer() {
		for(int width = 1; width < 3000; width += 500){
			for(int height = 1; height < 3000; height += 500){
				canvas.setSize(width, height);
				assertSize(width, height, canvas.createLayer());
			}
		}
	}

	@Test
	public final void testGetImage() {
		for(int i = 1; i < 12; i++){
			final int width = 1 + random.nextInt(40);
			final int height = 1 + random.nextInt(40);
			new PGraphicsCanvas(width, height).getImage();
		}
	}

	@Test
	public final void testLine() {
		for(int i = 0; i < 100; i++){
			final float x0 = random.nextFloat();
			final float y0 = random.nextFloat();
			final float x1 = random.nextFloat();
			final float y1 = random.nextFloat();

			canvas.line(x0, y0, x1, y1, painter);
		}
	}

	@Test
	public final void testEllipse() {
		for(int i = 0; i < 100; i++){
			final float x0 = random.nextFloat();
			final float y0 = random.nextFloat();
			final float x1 = random.nextFloat();
			final float y1 = random.nextFloat();

			canvas.ellipse(x0, y0, x1, y1, painter, null);
		}
	}

	@Test
	public final void testArc() {
		for(int i = 0; i < 100; i++){
			final float x0 = random.nextFloat();
			final float y0 = random.nextFloat();
			final float x1 = random.nextFloat();
			final float y1 = random.nextFloat();

			final float start = random.nextFloat();
			final float end = random.nextFloat();

			canvas.arc(x0, y0, x1, y1, start, end, painter);
		}
	}

	@Test
	public final void testQuad() {
		for(int i = 0; i < 100; i++){

			final float x1 = random.nextFloat();
			final float y1 = random.nextFloat();
			final float x2 = random.nextFloat();
			final float y2 = random.nextFloat();
			final float x3 = random.nextFloat();
			final float y3 = random.nextFloat();
			final float x4 = random.nextFloat();
			final float y4 = random.nextFloat();

			canvas.quad(x1, y1, x2, y2, x3, y3, x4, y4, painter);
		}
	}

	//	@Test
	//	public final void testDrawPath() {
	//		fail("Not yet implemented"); // TODO implement test case
	//	}
	//
	//	@Test
	//	public final void testDrawPolygon() {
	//		fail("Not yet implemented"); // TODO implement test case
	//	}

	@Test
	public final void testHandleNewFrame() {
		canvas.handleNewFrame();
	}

	//	@Test
	//	public final void testSaveImage() {
	//		fail("Not yet implemented"); // TODO implement test case
	//	}
	//
	//	@Test
	//	public final void testGetRotationTransformation() {
	//		fail("Not yet implemented"); // TODO implement test case
	//	}
	//
	//	@Test
	//	public final void testGetTranslationTransformation() {
	//		fail("Not yet implemented"); // TODO implement test case
	//	}
	//
	//	@Test
	//	public final void testGetScaleTransformation() {
	//		fail("Not yet implemented"); // TODO implement test case
	//	}

	private static void assertSize(int expectedWidth, int expectedHeight, PGraphicsCanvas canvas){
		assertEquals(expectedWidth, canvas.getWidth());
		assertEquals(expectedHeight, canvas.getHeight());
	}

	private void assertSize(int expectedWidth, int expectedHeight, PGraphics image) {
		assertEquals(expectedWidth, image.width);
		assertEquals(expectedHeight, image.height);
	}

}
