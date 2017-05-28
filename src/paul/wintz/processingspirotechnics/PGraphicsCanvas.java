package paul.wintz.processingspirotechnics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import paul.wintz.canvas.Canvas;
import paul.wintz.canvas.Painter;
import paul.wintz.utils.Vector;
import processing.core.PConstants;
import processing.core.PGraphics;

public class PGraphicsCanvas implements Canvas<PGraphics> {

	private final ArrayList<PGraphics> layers;
	private int width, height;
	private float scale, rotation, centerX, centerY;

	public PGraphicsCanvas(int width, int height, int numLayers) {
		if(numLayers < 1) throw new IllegalArgumentException("There must be at least one layer");
		this.width = width;
		this.height = height;
		layers = new ArrayList<>(numLayers);
		for(int i = 0; i < numLayers; i++){
			layers.add(createLayer());
		}
	}

	/**
	 * Draw a line from (x0,y0) to (x1,y1)
	 * @throws IllegalArgumentException if the stroke is less than zero, or the layer does not exist.
	 */
	@Override
	public void line(float x0, float y0, float x1, float y1, Painter painter) throws IllegalArgumentException {
		final PGraphics layer = bindPainter(painter);
		layer.line(x0, y0, x1, y1);
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, Queue<Transformation<PGraphics>> transforms){
		final PGraphics layer = bindPainter(painter);
		if(transforms!= null) {
			pushTransformations(layer, transforms);
		}
		{
			layer.ellipseMode(PConstants.CENTER);
			layer.ellipse(xCenter, yCenter, width, height);
		}
		if(transforms!= null) {
			popTransformations(layer);
		}
	}

	@Override
	public void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle, Painter painter) {
		final PGraphics layer = bindPainter(painter);
		layer.arc(xCenter, yCenter, width, height, startAngle, endAngle);
	}

	@Override
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Painter painter){
		final PGraphics layer = bindPainter(painter);
		layer.quad(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	@Override
	public void drawPath(List<Vector> points, Painter painter, Queue<Transformation<PGraphics>> transforms){
		final PGraphics layer = bindPainter(painter);
		if(transforms!= null) {
			pushTransformations(layer, transforms);
		}
		{
			layer.beginShape();
			for(final Vector pnt: points){
				layer.curveVertex((float) pnt.x(), (float) pnt.y());
			}
			layer.endShape();
		}
		if(transforms!= null) {
			popTransformations(layer);
		}
	}

	@Override
	public void drawPolygon(List<Vector> points, Painter painter, Queue<Transformation<PGraphics>> transforms) {
		final PGraphics layer = bindPainter(painter);
		if(transforms!= null) {
			pushTransformations(layer, transforms);
		}
		{
			layer.beginShape();
			synchronized (points) {
				for(final Vector pnt: points){
					layer.vertex((float) pnt.x(), (float) pnt.y());
				}
			}
			layer.endShape();
		}
		if(transforms!= null) {
			popTransformations(layer);
		}
	}

	@Override
	public void clearAll() {
		for(final PGraphics layer : layers) {
			layer.clear();
		}
	}

	@Override
	public void clearLayer(Painter painter) {
		layers.get(painter.layer).clear();
	}

	@Override
	public void background(Painter painter){
		final int fill = painter.getFill();
		layers.get(painter.layer).background(fill);
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		for(final PGraphics layer : layers){
			layer.setSize(width, height);
		}
	}

	protected PGraphics createLayer() {
		final PGraphics layer = ProcessingUtils.createPGraphics(width , height);
		//layer.strokeCap(PConstants.SQUARE);
		return layer;
	}

	@Override
	public PGraphics getImage() {
		final PGraphics image = createLayer();
		image.beginDraw();

		for(final PGraphics layer : layers) {
			layer.endDraw();
			image.image(layer, 0, 0);
			layer.beginDraw();
		}
		image.endDraw();
		return image;
	}

	private PGraphics bindPainter(Painter painter){
		if(painter.getStrokeWeight() < 0) throw new IllegalArgumentException("Stroke was less than zero!");
		if(painter.layer < 0 || painter.layer >= layers.size()) throw new IllegalArgumentException("Layer #" + painter.layer + " does not exist.");

		final PGraphics layer = layers.get(painter.layer);
		if(painter.isFilled()){
			layer.fill(painter.getFill());
		} else {
			layer.noFill();
		}

		if(painter.isStroked()){
			layer.stroke(painter.getStroke());
			layer.strokeWeight(painter.getStrokeWeight() / scale);
		} else {
			layer.noStroke();
		}
		return layer;
	}

	@Override
	public void handleNewFrame() {
		translate(centerX * width, centerY * height );
		rotate(rotation);
		scale(scale);
	}

	private void translate(double x, double y) {
		for(final PGraphics layer : layers){
			layer.translate((float) x, (float) y);
		}
	}

	private void rotate(double angle) {
		for(final PGraphics layer : layers){
			layer.rotate((float) angle);
		}
	}

	private void scale(double scale) {
		for(final PGraphics layer : layers){
			layer.scale((float) scale);
		}
	}

	@Override
	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public void setCenter(float centerX, float centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}

	@Override
	public void setRotation(float angle) {
		rotation = angle;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLayersCount() {
		return layers.size();
	}

	@Override
	public void saveImage(File f) {
		getImage().save(f.getAbsolutePath());
	}

	@Override
	public String toString() {
		return String.format("%d x %d canvas (%d layers)", getWidth(), getHeight(), getLayersCount());
	}

	private int pushedTransformationsCount = 0;
	/**
	 * Sets short-term transformations.
	 * @param layer
	 * @param transforms
	 */
	private void pushTransformations(PGraphics layer, Queue<Transformation<PGraphics>> transforms){
		if(transforms == null) throw new IllegalArgumentException("transforms w null");
		if(pushedTransformationsCount != 0) throw new IllegalStateException("There must be no pushed transformations, but instead there were " + pushedTransformationsCount);
		pushedTransformationsCount++;
		layer.pushMatrix();
		for(final Transformation<PGraphics> t : transforms){
			t.apply(layer);
		}
	}

	/**
	 * Unsets the short-term transformations.
	 * @param layer
	 */
	private void popTransformations(PGraphics layer){
		if(pushedTransformationsCount != 1) throw new IllegalStateException("There must be one pushed transformations, but instead there were " + pushedTransformationsCount);
		pushedTransformationsCount--;
		layer.popMatrix();
	}

	@Override
	public Canvas.Transformation<PGraphics> getRotationTransformation(float angle) {
		return new PGraphicsRotation(angle);
	}

	@Override
	public Canvas.Transformation<PGraphics> getTranslationTransformation(float xShift, float yShift) {
		return new PGraphicsTranslation(xShift, yShift);
	}

	@Override
	public Canvas.Transformation<PGraphics> getScaleTransformation(float xScale, float yScale) {
		return new PGraphicsScale(xScale, yScale);
	}

	public static final class PGraphicsRotation extends Transformation<PGraphics>{
		private final float angle;
		public PGraphicsRotation(float angle) {
			this.angle = angle;
		}

		@Override
		public void apply(PGraphics layer) {
			layer.rotate(angle);
		}
	}

	public static final class PGraphicsTranslation extends Transformation<PGraphics>{
		private final float xShift;
		private final float yShift;

		public PGraphicsTranslation(float xShift, float yShift) {
			this.xShift = xShift;
			this.yShift = yShift;
		}

		@Override
		public void apply(PGraphics layer) {
			layer.translate(xShift, yShift);
		}
	}

	public static final class PGraphicsScale extends Transformation<PGraphics>{
		private final float xScale;
		private final float yScale;

		public PGraphicsScale(float xScale, float yScale) {
			this.xScale = xScale;
			this.yScale = yScale;
		}

		@Override
		public void apply(PGraphics layer) {
			layer.scale(xScale, yScale);
		}
	}
}
