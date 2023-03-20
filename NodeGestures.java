package com.biztec.cryptonite.DragDrop;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;


public class NodeGestures {

    private final DragContext nodeDragContext = new DragContext();

    PannableCanvas canvas;
    private boolean dragStarted = false;

    public NodeGestures(PannableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseReleasedEventHandlerChild() {
        return event -> dragStarted = false;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandlerChild() {
        return event -> {
            if (!event.isPrimaryButtonDown())
                return;

            double scale = canvas.getScale();

            Node node = (Node) event.getSource();

            if (!dragStarted)
                return;

            node.setTranslateX(nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY + ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            if (node.getTranslateX() < 0)
                node.setTranslateX(0);
            if (node.getTranslateY() < 0)
                node.setTranslateY(0);
            if (node.getTranslateX() > (canvas.getWidth()-node.getBoundsInParent().getWidth()))
                node.setTranslateX(canvas.getWidth()-node.getBoundsInParent().getWidth());
            if (node.getTranslateY() > canvas.getHeight()-node.getBoundsInParent().getHeight())
                node.setTranslateY(canvas.getHeight()-node.getBoundsInParent().getHeight());

            event.consume();
        };
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandlerChild(Node child) {
        return event -> {
            if (!event.isPrimaryButtonDown())
                return;

            Node node = (Node) event.getSource();
            Bounds boundsInScene = child.localToScene(child.getBoundsInLocal());

            if (!boundsInScene.contains(new Point2D(event.getSceneX(), event.getSceneY())))
                return;

            dragStarted = true;

            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();

            nodeDragContext.translateAnchorX = node.getTranslateX();
            nodeDragContext.translateAnchorY = node.getTranslateY();
        };
    }

    private final EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        if(!event.isPrimaryButtonDown())
            return;

        nodeDragContext.mouseAnchorX = event.getSceneX();
        nodeDragContext.mouseAnchorY = event.getSceneY();

        Node node = (Node) event.getSource();

        nodeDragContext.translateAnchorX = node.getTranslateX();
        nodeDragContext.translateAnchorY = node.getTranslateY();
    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        if (!event.isPrimaryButtonDown())
            return;

        double scale = canvas.getScale();

        Node node = (Node) event.getSource();

        node.setTranslateX(nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
        node.setTranslateY(nodeDragContext.translateAnchorY + ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

        if (node.getTranslateX() < 0)
            node.setTranslateX(0);
        if (node.getTranslateY() < 0)
            node.setTranslateY(0);
        if (node.getTranslateX() > (canvas.getWidth()-node.getBoundsInParent().getWidth()))
            node.setTranslateX(canvas.getWidth()-node.getBoundsInParent().getWidth());
        if (node.getTranslateY() > canvas.getHeight()-node.getBoundsInParent().getHeight())
            node.setTranslateY(canvas.getHeight()-node.getBoundsInParent().getHeight());

        event.consume();
    };
}