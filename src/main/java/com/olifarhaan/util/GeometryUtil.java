package com.olifarhaan.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeometryUtil {
    private static final int SRID = 4326; // WGS84 coordinate system
    private final GeometryFactory geometryFactory;

    public GeometryUtil() {
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
    }

    public Point createPoint(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
} 