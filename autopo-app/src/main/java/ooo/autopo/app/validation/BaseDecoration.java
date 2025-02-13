package ooo.autopo.app.validation;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import net.synedra.validatorfx.Decoration;
import ooo.autopo.app.RootStack;

import static ooo.autopo.app.context.ApplicationContext.app;

/**
 * @author Andrea Vacondio
 */
public class BaseDecoration implements Decoration {

    private final Node decorationNode;
    private final Pos pos;
    private final double xOffset;
    private final double yOffset;
    private final ChangeListener<Boolean> layoutListener;
    private final ChangeListener<Transform> transformListener;
    private Node decoratedNode;

    /**
     * Create GraphicDecoration that will be overlayed in the top-left corner
     *
     * @param decorationNode The node to overlay over the decorated node
     */
    public BaseDecoration(Node decorationNode) {
        this(decorationNode, Pos.TOP_LEFT);
    }

    /**
     * Create GraphicDecoration
     *
     * @param decorationNode The node to overlay over the decorated node
     * @param position       The location of the overlay
     */
    public BaseDecoration(Node decorationNode, Pos position) {
        this(decorationNode, position, 0, 0);
    }

    /**
     * Create GraphicDecoration
     *
     * @param decorationNode The node to overlay over the decorated node
     * @param position       The location of the overlay
     * @param xOffset        Horizontal offset of overlay (with respect to position)
     * @param yOffset        Vertical offset of overlay (with respect to position)
     */
    public BaseDecoration(Node decorationNode, Pos position, double xOffset, double yOffset) {
        this.decorationNode = decorationNode;
        this.decorationNode.setManaged(false);
        this.pos = position;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        layoutListener = new WeakChangeListener<>((observable, oldValue, newValue) -> layoutGraphic());
        transformListener = (observable, oldValue, newValue) -> layoutGraphic();
    }

    @Override
    public void add(Node target) {
        decoratedNode = target;
        if (decoratedNode != null) {    // #10: could have been removed again already ...
            setListener();
            layoutGraphic();
        }
    }

    @Override
    public void remove(Node target) {
        stack().getChildren().remove(decorationNode);
        stack().needsLayoutProperty().removeListener(layoutListener);
        this.decoratedNode = null;
        target.localToSceneTransformProperty().removeListener(transformListener);
    }

    private void setListener() {
        stack().needsLayoutProperty().removeListener(layoutListener);
        stack().needsLayoutProperty().addListener(layoutListener);
        decoratedNode.localToSceneTransformProperty().removeListener(transformListener);
        decoratedNode.localToSceneTransformProperty().addListener(transformListener);
    }

    private void layoutGraphic() {
        // Because we made decorationNode unmanaged, we are responsible for sizing it:
        decorationNode.autosize();
        // Now get decorationNode's layout Bounds and use for its position computations:
        Bounds decorationNodeLayoutBounds = decorationNode.getLayoutBounds();
        double decorationNodeWidth = decorationNodeLayoutBounds.getWidth();
        double decorationNodeHeight = decorationNodeLayoutBounds.getHeight();

        Bounds targetBounds = decoratedNode.getLayoutBounds();
        double x = targetBounds.getMinX();
        double y = targetBounds.getMinY();

        double targetWidth = targetBounds.getWidth();
        if (targetWidth <= 0) {
            targetWidth = decoratedNode.prefWidth(-1);
        }

        double targetHeight = targetBounds.getHeight();
        if (targetHeight <= 0) {
            targetHeight = decoratedNode.prefHeight(-1);
        }

        switch (pos.getHpos()) {
        case CENTER -> x += targetWidth / 2 - decorationNodeWidth / 2.0;
        case LEFT -> x -= decorationNodeWidth / 2.0;
        case RIGHT -> x += targetWidth - decorationNodeWidth / 2.0;
        }

        switch (pos.getVpos()) {
        case CENTER -> y += targetHeight / 2 - decorationNodeHeight / 2.0;
        case TOP -> y -= decorationNodeHeight / 2.0;
        case BOTTOM -> y += targetHeight - decorationNodeHeight / 2.0;
        case BASELINE -> y += decoratedNode.getBaselineOffset() - decorationNode.getBaselineOffset() - decorationNodeHeight / 2.0;
        }
        Bounds sceneBounds = decoratedNode.localToScene(targetBounds);
        Bounds stackBounds = stack().sceneToLocal(sceneBounds);
        decorationNode.setLayoutX(Math.round(x + xOffset + stackBounds.getMinX()));
        decorationNode.setLayoutY(Math.round(y + yOffset + stackBounds.getMinY()));
        addOrRemoveDecorationNodeToStack();
    }

    private void addOrRemoveDecorationNodeToStack() {
        boolean shouldBeThere = decoratedNode.getScene() != null && targetVisible();
        boolean isThere = stack().getChildren().contains(decorationNode);
        if (shouldBeThere != isThere) {
            if (shouldBeThere) {
                stack().getChildren().add(decorationNode);
            } else {
                stack().getChildren().remove(decorationNode);
            }
        }
    }

    private boolean targetVisible() {
        Node node = decoratedNode;
        boolean visible = true;
        while (visible && node != null) {
            visible = node.isVisible();
            node = node.getParent();
        }
        return visible;
    }

    private RootStack stack() {
        return app().instance(RootStack.class);
    }
}
