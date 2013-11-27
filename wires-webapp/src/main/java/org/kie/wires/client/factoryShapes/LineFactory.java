package org.kie.wires.client.factoryShapes;

import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableLine;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.DragBounds;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LineFactory extends ShapeFactory<Line> {

    private static String DESCRIPTION = "Line";

    private static int shapes;

    public LineFactory() {
    }

    public LineFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent,
            Map<Integer, Integer> shapesByCategory) {
        super(panel, shapeAddEvent);
        shapes = shapesByCategory.get(this.getCategory());
        this.drawBoundingBox(group);
    }

    @Override
    public void drawBoundingBox(Group group) {
        this.addBoundingHandlers(super.createBoundingBox(group, shapes), group);
        this.addShapeHandlers(drawShape(), group);
        group.add(super.createDescription(DESCRIPTION, shapes));
    }

    @Override
    public Shape<Line> drawShape() {
        Line line = new EditableLine(getX1(), getY1(), getX2(), getY2());
        line.setDragBounds(new DragBounds(150, 260, 150, 150));
        line.setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE)
                .setDraggable(false);
        return line;
    }

    @Override
    public void addShapeHandlers(Shape<Line> shape, Group group) {
        shape.addNodeMouseDownHandler(getNodeMouseDownEvent(group));
        group.add(shape);

    }

    @Override
    protected void addBoundingHandlers(Rectangle boundingBox, Group group) {
        boundingBox.addNodeMouseDownHandler(getNodeMouseDownEvent(group));
    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent(final Group group) {
        NodeMouseDownHandler nodeMouseDownHandler = new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                final Line floatingShape = new EditableLine(getFloatingX1(), getFloatingY1(), getFloatingX2(), getFloatingY2());
                floatingShape.setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE)
                        .setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setDraggable(false);
                setFloatingPanel(floatingShape, 30, 30, event, null);
            }
        };

        return nodeMouseDownHandler;

    }

    private double getX1() {
        return 12 + super.calculateX(shapes);
    }

    private double getY1() {
        return 8 + super.calculateY(shapes);
    }

    private double getX2() {
        return 42 + super.calculateX(shapes);
    }

    private double getY2() {
        return 30 + super.calculateY(shapes);
    }

    private double getFloatingX1() {
        return 0;
    }

    // this value must be calculated
    private double getFloatingY1() {
        return 0;
    }

    // this value must be calculated
    private double getFloatingX2() {
        return 30;
    }

    // this value must be calculated
    private double getFloatingY2() {
        return 30;
    }

    @Override
    protected int getCategory() {
        return ShapeType.LINE.getCategory();
    }

}