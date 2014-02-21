package org.kie.wires.client.util;

import java.util.List;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ProgressEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.Color;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;

public class LienzoUtils {

    private static int progressWidth = 1;
    public static boolean loadingProgressBar;
    public static List<Shape<?>> progressShapes;
    private static int xProgressBar = 400;
    private static int yProgressBar = 300;

    public static void drawProgressBar(final Group group, final Layer layer, final LienzoPanel panel, final Event<ProgressEvent> progressEvent) {
        progressWidth = 1;
        progressShapes = Lists.newArrayList();
        final int substrateWidth = 300;
        final int progressHeight = 34;

        final Text progressPercentage = new Text("Loading...", BayesianUtils.fontFamilyProgressBar,
                BayesianUtils.fontSizeProgressBar).setFillColor(ColorName.WHITE.getValue())
                .setStrokeColor(BayesianUtils.substrateColor).setTextBaseLine(TextBaseLine.MIDDLE)
                .setTextAlign(TextAlign.CENTER);

        drawComponentProgressBar(Color.rgbToBrowserHexColor(197, 216, 214), substrateWidth, BayesianUtils.substrateHeight, 200,
                Color.rgbToBrowserHexColor(197, 216, 214), false, group);
        Timer timer = new Timer() {
            @Override
            public void run() {
                progressWidth += 1;
                if ((progressWidth > substrateWidth - 4)) {
                    this.cancel();
                    progressShapes = null;
                } else if (!loadingProgressBar) {
                    this.cancel();
                    progressEvent.fire(new ProgressEvent(LienzoUtils.progressShapes));
                    progressShapes = null;
                } else {
                    
                    drawComponentProgressBar(Color.rgbToBrowserHexColor(102, 183, 176), progressWidth, progressHeight, 300,
                            Color.rgbToBrowserHexColor(102, 183, 176), true, group);

                    progressPercentage.setText("Loading...").setX(xProgressBar + 100).setY(yProgressBar + 18);
                    progressShapes.add(progressPercentage);
                    group.add(progressPercentage);
                    layer.draw();
                }

            }
        };
        timer.scheduleRepeating(1);
    }

    public static void drawComponentProgressBar(String color, int width, int height, int zindex, String borderColor,
            boolean progress, Group group) {
        final Rectangle component = new Rectangle(width, height);
        if (progress) {
            component.setFillGradient(BayesianUtils.getProgressGradient());
        } else {
            component.setFillGradient(BayesianUtils.getSubstrateGradient()).setShadow(BayesianUtils.getSubstrateShadow())
                    .setStrokeColor(BayesianUtils.substrateColor).setStrokeWidth(1);
        }
        component.setX(xProgressBar).setY(yProgressBar).setDraggable(false);
        progressShapes.add(component);
        group.add(component);
    }

    

    public static Style getFloatingStyle(LienzoPanel floatingPanel, int positionX, int positionY, int zIndex) {
        Style style = floatingPanel.getElement().getStyle();
        style.setPosition(Position.ABSOLUTE);
        style.setLeft(positionX, Unit.PX);
        style.setTop(positionY, Unit.PX);
        style.setZIndex(zIndex);
        return style;
    }

}