package com.reactnativenavigation.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;

import com.reactnativenavigation.params.StyleParams;

class TopBarBorder extends ShapeDrawable {
    private StyleParams.Color backgroundColor;
    private final Paint pathPaint;
    private Border border;

    private class Border {
        private Path path;
        int width;
        int height;

        Border(int width, int height) {
            this.width = width;
            this.height = height;
            path = createPath();
        }

        private Path createPath() {
            Point a = new Point(0, height);
            Point b = new Point(width, height);
            Path path = new Path();
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            return path;
        }

        boolean dimensionsChanged(int width, int height) {
            return this.width != width || this.height != height;
        }
    }

    TopBarBorder(StyleParams styleParams) {
        super(new RectShape());
        backgroundColor = styleParams.topBarColor;
        pathPaint = createPathPaint(styleParams.topBarBorderColor, styleParams.topBarBorderWidth);
    }

    private Paint createPathPaint(StyleParams.Color color, float strokeWidth) {
        Paint res = new Paint();
        res.setStyle(Paint.Style.STROKE);
        res.setColor(color.getColor());
        res.setStrokeWidth(strokeWidth);
        return res;
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        paint.setColor(backgroundColor.getColor(Color.WHITE));
        super.onDraw(shape, canvas, paint);
        createBorder(canvas);
        canvas.drawPath(border.path, pathPaint);
    }

    private void createBorder(Canvas canvas) {
        if (border == null || border.dimensionsChanged(canvas.getWidth(), canvas.getHeight())) {
            border = new Border(canvas.getWidth(), canvas.getHeight());
        }
    }
}
