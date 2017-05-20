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
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param color
	 * @param stroke
	 * @param layer
	 * @throws IllegalArgumentException if the stroke is less than zero, or the layer does not exist.
	 */
	public void line(float x0, float y0, float x1, float y1, Painter painter) throws IllegalArgumentException {
		PGraphics layer = bindPainter(painter);
		layer.line((float) x0, (float) y0, (float) x1, (float) y1);
	}

	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, Queue<Transformation<PGraphics>> transforms){
		PGraphics layer = bindPainter(painter);
		if(transforms!= null) pushTransformations(layer, transforms);
		{
			layer.ellipseMode(PConstants.CENTER);
			layer.ellipse((float) xCenter, (float) yCenter, (float) width, (float) height);
		}
		if(transforms!= null) popTransformations(layer);
	}
	
	@Override
	public void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle, Painter painter) {
		PGraphics layer = bindPainter(painter);
		layer.arc(xCenter, yCenter, width, height, startAngle, endAngle);
	}
	
	@Override
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Painter painter){
		PGraphics layer = bindPainter(painter);
		layer.quad(x1, y1, x2, y2, x3, y3, x4, y4);
	}
	
	public void drawPath(List<Vector> points, Painter painter, Queue<Transformation<PGraphics>> transforms){
		PGraphics layer = bindPainter(painter);
		if(transforms!= null) pushTransformations(layer, transforms);
		{
			layer.beginShape();
			for(Vector pnt: points){
				layer.curveVertex((float) pnt.x(), (float) pnt.y());
			}
			layer.endShape();
		}
		if(transforms!= null) popTransformations(layer);
	}

	@Override
	public void drawPolygon(List<Vector> points, Painter painter, Queue<Transformation<PGraphics>> transforms) {
		PGraphics layer = bindPainter(painter);
		if(transforms!= null) pushTransformations(layer, transforms);
		{
			layer.beginShape();
			for(Vector pnt: points){
				layer.vertex((float) pnt.x(), (float) pnt.y());
			}
			layer.endShape();
		}
		if(transforms!= null) popTransformations(layer);
	}
	
	@Override
	public void clearAll() {
		for(PGraphics layer : layers) {
			layer.clear();
		}
	}
	
	@Override
	public void clearLayer(Painter painter) {
		layers.get(painter.layer).clear();
	}

	@Override
	public void background(Painter painter){
		int fill = 0;//TODO: painter.getFill();
		if(fill < 0) throw new IllegalStateException("Fill cannot be less than zero!");
		layers.get(painter.layer).background(fill);
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		
		for(PGraphics layer : layers){
			layer.setSize(width, height);
		}
	}
	
	protected PGraphics createLayer() {
		PGraphics layer = ProcessingUtils.createGraphics(width , height);
		layer.strokeCap(PConstants.SQUARE);
		return layer;
	} 

	@Override
	public PGraphics getImage() {
		PGraphics image = createLayer();
		image.beginDraw();
		
		for(PGraphics layer : layers) {
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
		
		PGraphics layer = layers.get(painter.layer);
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
		for(PGraphics layer : layers){
			layer.translate((float) x, (float) y);
		}
	}

	private void rotate(double angle) {
		for(PGraphics layer : layers){
			layer.rotate((float) angle);
		}
	}

	private void scale(double scale) {
		for(PGraphics layer : layers){
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
		for(Transformation<PGraphics> t : transforms){
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
	public paul.wintz.canvas.Canvas.Transformation<PGraphics> getRotationTransformation(float angle) {
		return new PGRotation((float) angle);
	}

	@Override
	public paul.wintz.canvas.Canvas.Transformation<PGraphics> getTranslationTransformation(float xShift, float yShift) {
		return new PGTranslation((float) xShift, (float) yShift);
	}

	@Override
	public paul.wintz.canvas.Canvas.Transformation<PGraphics> getScaleTransformation(float xScale, float yScale) {
		return new PGScale((float) xScale, (float) yScale);
	}
	
	public static final class PGRotation extends Transformation<PGraphics>{
		private float angle;
		public PGRotation(float angle) {
			this.angle = angle;
		}
		
		@Override
		public void apply(PGraphics layer) {
			layer.rotate(angle);
		}		
	}

	public static final class PGTranslation extends Transformation<PGraphics>{
		private float xShift;
		private float yShift;
		
		public PGTranslation(float xShift, float yShift) {
			this.xShift = xShift;
			this.yShift = yShift;
		}

		@Override
		public void apply(PGraphics layer) {
			layer.translate(xShift, yShift);			
		}
	}
	
	public static final class PGScale extends Transformation<PGraphics>{
		private float xScale;
		private float yScale;
		
		public PGScale(float xScale, float yScale) {
			this.xScale = xScale;
			this.yScale = yScale;
		}

		@Override
		public void apply(PGraphics layer) {
			layer.scale(xScale, yScale);
		}
	}
}
