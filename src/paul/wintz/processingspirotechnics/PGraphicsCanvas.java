package paul.wintz.processingspirotechnics;

import java.util.*;

import paul.wintz.canvas.*;
import paul.wintz.utils.Vector;
import processing.core.*;

public class PGraphicsCanvas implements Layer<PGraphics> {

	private final PGraphics layer;
	private int width, height;
	private float scale, rotation, centerX, centerY;

	public PGraphicsCanvas(int width, int height) {
		this.width = width;
		this.height = height;
		layer = createLayer();
		layer.beginDraw();
	}

	/**
	 * Draw a line from (x0,y0) to (x1,y1)
	 *
	 * @throws IllegalArgumentException
	 *             if the stroke is less than zero, or the layers does not exist.
	 */
	@Override
	public void line(float x0, float y0, float x1, float y1, Painter painter) throws IllegalArgumentException {
		bindPainter(painter);
		layer.line(x0, y0, x1, y1);
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, Queue<Transformation<PGraphics>> transientTransforms) {
		bindPainter(painter);
		if (transientTransforms != null) {
			pushTransientTransformations(layer, transientTransforms);
		}
		{
			layer.ellipseMode(PConstants.CENTER);
			layer.ellipse(xCenter, yCenter, width, height);
		}
		if (transientTransforms != null) {
			popTransientTransformations(layer);
		}
	}

	@Override
	public void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle,
			Painter painter) {
		bindPainter(painter);
		layer.arc(xCenter, yCenter, width, height, startAngle, endAngle);
	}

	@Override
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Painter painter) {
		bindPainter(painter);
		layer.quad(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	@Override
	public void drawPath(List<Vector> points, Painter painter, Queue<Transformation<PGraphics>> transientTransforms) {
		bindPainter(painter);
		if (transientTransforms != null) {
			pushTransientTransformations(layer, transientTransforms);
		}
		{
			layer.beginShape();
			for (final Vector pnt : points) {
				layer.curveVertex((float) pnt.x(), (float) pnt.y());
			}
			layer.endShape();
		}
		if (transientTransforms != null) {
			popTransientTransformations(layer);
		}
	}

	@Override
	public void drawPolygon(List<Vector> points, Painter painter, Queue<Transformation<PGraphics>> transientTransforms) {
		bindPainter(painter);
		if (transientTransforms != null) {
			pushTransientTransformations(layer, transientTransforms);
		}
		{
			layer.beginShape();
			synchronized (points) {
				for (final Vector pnt : points) {
					layer.vertex((float) pnt.x(), (float) pnt.y());
				}
			}
			layer.endShape();
		}
		if (transientTransforms != null) {
			popTransientTransformations(layer);
		}
	}


	/**
	 * Draw a dot at the given coordinates. This is different from a circle in
	 * that the size of the circle does not scale, so it is always the portion
	 * of the layers.
	 *
	 * @param x
	 * @param y
	 * @param transforms
	 * @param color
	 * @param size
	 * @param layers
	 */
	public void dot(float x, float y, float radius, Painter painter, Queue<Transformation<PGraphics>> transforms) {
		ellipse(x, y, radius / scale, radius / scale, painter, transforms);
	}

	public void dot(Vector v, float radius, Painter painter, Queue<Transformation<PGraphics>> transforms) {
		dot((float) v.x(), (float) v.y(), radius, painter, transforms);
	}

	@Override
	public void endpointToEndpoint(Vector start, Vector end, Painter painter) {
		line((float) start.x(), (float) start.y(), (float) end.x(), (float) end.y(), painter);
	}

	public void vector(Vector startPos, Vector vector, Painter painter) {
		vector(startPos, vector, 1.0, painter);
	}

	public void vector(Vector startPos, Vector vector, double scale, Painter painter) {
		line((float) startPos.x(), (float) startPos.y(), (float) (startPos.x() + scale * vector.x()),
				(float) (startPos.y() + scale * vector.y()), painter);
	}

	/**
	 * Draw an empty circle.
	 *
	 * @param x
	 *            x-coordinate of center of circle
	 * @param y
	 *            y-coordinate of center of circle
	 * @param radius
	 * @param strokeWeight
	 * @param color
	 * @param layers
	 */
	@Override
	public void circle(float x, float y, float radius, Painter painter) {
		ellipse(x, y, (2 * radius), (2 * radius), painter, null);
	}

	@Override
	public void quad(Vector v1, Vector v2, Vector v3, Vector v4, Painter painter) {
		quad((float) v1.x(), (float) v1.y(), (float) v2.x(), (float) v2.y(), (float) v3.x(), (float) v3.y(),
				(float) v4.x(), (float) v4.y(), painter);
	}

	public void drawPath(List<Vector> points, Painter painter) {
		drawPath(points, painter, null);
	}



	@Override
	public void clear() {
		layer.clear();
	}

	@Override
	public void background(Painter painter) {
		final int fill = painter.getFill();
		layer.background(fill);
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		layer.setSize(width, height);
		layer.beginDraw();
	}

	protected final PGraphics createLayer() {
		return ProcessingUtils.createPGraphics(width, height);
	}

	@Override
	public PGraphics getImage() {
		layer.endDraw();
		return layer;
	}

	private void bindPainter(Painter painter) {

		if (painter.isFilled()) {
			layer.fill(painter.getFill());
		} else {
			layer.noFill();
		}

		if (painter.isStroked()) {
			layer.stroke(painter.getStroke());
			layer.strokeWeight(painter.getStrokeWeight() / scale);
		} else {
			layer.noStroke();
		}

	}

	@Override
	public void handleNewFrame() {
		translate(centerX * width, centerY * height);
		rotate(rotation);
		scale(scale);
	}

	private void translate(double x, double y) {

		layer.translate((float) x, (float) y);

	}

	private void rotate(double angle) {

		layer.rotate((float) angle);

	}

	private void scale(double scale) {

		layer.scale((float) scale);

	}

	@Override
	public void setScale(float scale) {
		this.scale = scale;
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
	public String toString() {
		return String.format("Layer (%d x %d) ", getWidth(), getHeight());
	}

	private int pushedTransientTransformationsCount = 0;

	/**
	 * Sets short-term transformations.
	 *
	 * @param layers
	 * @param transientTransforms
	 */
	private void pushTransientTransformations(PGraphics layer, Queue<Transformation<PGraphics>> transientTransforms) {
		if (transientTransforms == null)
			throw new IllegalArgumentException("transforms w null");
		if (pushedTransientTransformationsCount != 0)
			throw new IllegalStateException(
					"There must be no pushed transformations, but instead there were " + pushedTransientTransformationsCount);
		pushedTransientTransformationsCount++;
		layer.pushMatrix();
		for (final Transformation<PGraphics> t : transientTransforms) {
			t.apply(layer);
		}
	}

	/**
	 * Unsets the short-term transformations.
	 *
	 * @param layers
	 */
	private void popTransientTransformations(PGraphics layer) {
		if (pushedTransientTransformationsCount != 1)
			throw new IllegalStateException(
					"There must be one pushed transformations, but instead there were " + pushedTransientTransformationsCount);
		pushedTransientTransformationsCount--;
		layer.popMatrix();
	}

	@Override
	public Transformation<PGraphics> getRotationTransformation(float angle) {
		return new PGraphicsRotation(angle);
	}

	@Override
	public Transformation<PGraphics> getTranslationTransformation(float xShift, float yShift) {
		return new PGraphicsTranslation(xShift, yShift);
	}

	@Override
	public Transformation<PGraphics> getScaleTransformation(float xScale, float yScale) {
		return new PGraphicsScale(xScale, yScale);
	}

	public static final class PGraphicsRotation implements Transformation<PGraphics> {
		private final float angle;

		public PGraphicsRotation(float angle) {
			this.angle = angle;
		}

		@Override
		public void apply(PGraphics layer) {
			layer.rotate(angle);
		}
	}

	public static final class PGraphicsTranslation implements Transformation<PGraphics> {
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

	public static final class PGraphicsScale implements Transformation<PGraphics> {
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

	@Override
	public void drawOnto(PGraphics target) {

		if(target.width != layer.width || target.height != layer.height)
			throw new RuntimeException("Dimensions must match");

		layer.endDraw();
		target.image(layer, 0, 0);
		layer.beginDraw();

	}

	@Override
	public void dot(float x, float y, float radius, Painter painter) {
		dot(x, y, radius, painter, null);
	}

	@Override
	public void dot(Vector pos, float radius, Painter painter) {
		dot((float) pos.x(), (float) pos.y(), radius, painter);
	}

	@Override
	public double getScale() {
		return scale;
	}

}
