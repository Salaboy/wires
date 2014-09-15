package com.bayesian.network.client.factory;

import com.emitrom.lienzo.client.core.shape.Circle;

public class LayerCircleFactory extends LayerFactory<Circle> {

    private static final int RADIUS = 7;

    private static final String DESCRIPTION = "Circle";

    private static int layers;

    public LayerCircleFactory( final Integer lay ) {
        layers = lay;
        this.drawBoundingBox( null );
    }

    @Override
    public void drawBoundingBox( final String template ) {
        super.createBoundingBox( layers );
        this.drawLayer();
        super.createDescription( DESCRIPTION,
                                 layers );
    }

    @Override
    public void drawLayer() {
        final Circle circle = new Circle( RADIUS );
        super.setAttributes( circle,
                             this.getX(),
                             this.getY() );
        templateShape.setShape( circle );
    }

    private double getX() {
        return 19;
    }

    private double getY() {
        return 15 + super.calculateY( layers );
    }

}