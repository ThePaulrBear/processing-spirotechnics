package paul.wintz.processingspirotechnics;

import static com.google.common.base.Preconditions.*;

import java.util.*;

import paul.wintz.canvas.*;
import paul.wintz.utils.Vector2D;
import processing.core.*;

public class PGraphicsCanvas implements Layer<PGraphics> {

	private final PGraphics layer;
	private int width, height;
	private float scale, rotation, centerX, centerY;

	private final Object lock = new Object();

	public PGraphicsCanvas(int width, int height) {
		this.width = width;
		this.height = height;
		layer = createLayer();
		layer.beginDraw();
	}

	/**
	 * Draw a line from (x0,y0) to (x1,y1)
	 */
	@Override
	public void line(float x0, float y0, float x1, float y1, Painter painter) {
		bindPainter(painter);
		layer.line(x0, y0, x1, y1);
	}

	@Override
	public void ellipse(float xCenter, float yCenter, float width, float height, Painter painter, Queue<Transformation> transientTransforms) {
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
	public void arc(float xCenter, float yCenter, float width, float height, float startAngle, float endAngle, Painter painter) {
		bindPainter(painter);
		layer.arc(xCenter, yCenter, width, height, startAngle, endAngle);
	}

	@Override
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Painter painter) {
		bindPainter(painter);
		layer.quad(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	@Override
	public void drawPath(List<Vector2D> points, Painter painter, Queue<Transformation> transientTransforms) {
		bindPainter(painter);
		if (transientTransforms != null) {
			pushTransientTransformations(layer, transientTransforms);
		}
		{
			layer.beginShape();
			for (final Vector2D pnt : points) {
				layer.curveVertex((float) pnt.x(), (float) pnt.y());
			}
			layer.endShape();
		}
		if (transientTransforms != null) {
			popTransientTransformations(layer);
		}
	}

	@Override
	public void drawPolygon(final List<Vector2D> points, Painter painter, Queue<Transformation> transientTransforms) {
		bindPainter(painter);
		if (transientTransforms != null) {
			pushTransientTransformations(layer, transientTransforms);
		}
		{
			layer.beginShape();
			synchronized (lock) {
				for (final Vector2D pnt : points) {
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
	public void dot(float x, float y, float radius, Painter painter, Queue<Transformation> transforms) {
		ellipse(x, y, radius / scale, radius / scale, painter, transforms);
	}

	public void dot(Vector2D v, float radius, Painter painter, Queue<Transformation> transforms) {
		dot((float) v.x(), (float) v.y(), radius, painter, transforms);
	}

	@Override
	public void endpointToEndpoint(Vector2D start, Vector2D end, Painter painter) {
		line((float) start.x(), (float) start.y(), (float) end.x(), (float) end.y(), painter);
	}

	public void vector2D(Vector2D startPos, Vector2D vector2D, Painter painter) {
		vector2D(startPos, vector2D, 1.0, painter);
	}

	public void vector2D(Vector2D startPos, Vector2D vector2D, double scale, Painter painter) {
		line((float) startPos.x(), (float) startPos.y(), (float) (startPos.x() + scale * vector2D.x()),
				(float) (startPos.y() + scale * vector2D.y()), painter);
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
	public void quad(Vector2D v1, Vector2D v2, Vector2D v3, Vector2D v4, Painter painter) {
		quad((float) v1.x(), (float) v1.y(), (float) v2.x(), (float) v2.y(), (float) v3.x(), (float) v3.y(),
				(float) v4.x(), (float) v4.y(), painter);
	}

	public void drawPath(List<Vector2D> points, Painter painter) {
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
	private void pushTransientTransformations(PGraphics layer, Queue<Transformation> transientTransforms) {
		checkNotNull(transientTransforms, "transforms were null");
		checkState(pushedTransientTransformationsCount == 0, "There must be no pushed transformations, but instead there were %s", pushedTransientTransformationsCount);

		pushedTransientTransformationsCount++;
		layer.pushMatrix();
		for (final Transformation transform : transientTransforms) {
			applyTransform(transform);
		}
	}

	/**
	 * Unsets the short-term transformations.
	 *
	 * @param layers
	 */
	private void popTransientTransformations(PGraphics layer) {
		checkState(pushedTransientTransformationsCount == 1, "There must be one pushed transformations, but instead there were %s", pushedTransientTransformationsCount);
		pushedTransientTransformationsCount--;
		layer.popMatrix();
	}

	@Override
	public void drawOnto(PGraphics target) {
		checkArgument(target.width  == layer.width, "Widths do not match");
		checkArgument(target.height == layer.height, "Heights do not match");

		layer.endDraw();
		target.image(layer, 0, 0);
		layer.beginDraw();

	}

	@Override
	public void dot(float x, float y, float radius, Painter painter) {
		dot(x, y, radius, painter, null);
	}

	@Override
	public void dot(Vector2D pos, float radius, Painter painter) {
		dot((float) pos.x(), (float) pos.y(), radius, painter);
	}

	@Override
	public double getScale() {
		return scale;
	}

	private void applyTransform(Transformation transformation) {

		if(transformation instanceof Rotation) {

			Rotation rotationTransform = (Rotation) transformation;
			layer.rotate(rotationTransform.angle);

		} else if(transformation instanceof Translation) {

			Translation translationTransform = (Translation) transformation;
			layer.translate(translationTransform.x, translationTransform.y);

		} else if(transformation instanceof Scale) {

			Scale scaleTransform = (Scale) transformation;
			layer.scale(scaleTransform.x, scaleTransform.y);

		} else
			throw new ClassCastException("Unexpected transformation: " + transformation);

	}


}
