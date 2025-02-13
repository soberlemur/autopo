package ooo.autopo.app.validation;/*
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

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.synedra.validatorfx.Decoration;
import net.synedra.validatorfx.Severity;
import net.synedra.validatorfx.StyleClassDecoration;
import net.synedra.validatorfx.ValidationMessage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Andrea Vacondio
 */
public class CompositeDecoration implements Decoration {

    private static final Image ERROR_IMAGE = new Image(Objects.requireNonNull(CompositeDecoration.class.getResourceAsStream("/validation/error.png")));
    private static final Image WARNING_IMAGE = new Image(Objects.requireNonNull(CompositeDecoration.class.getResourceAsStream("/validation/warning.png")));

    private final Set<Decoration> decorations = new HashSet<>();

    public CompositeDecoration(Node decorationNode, Pos position, double xOffset, double yOffset, String... styleClasses) {
        decorations.add(new BaseDecoration(decorationNode, position, xOffset, yOffset));
        decorations.add(new StyleClassDecoration(styleClasses));
    }

    public static Decoration createCompositeDecoration(ValidationMessage message) {
        return new CompositeDecoration(createDecorationNode(message), Pos.TOP_LEFT, 0, 2, "validatorfx-" + message.getSeverity().toString().toLowerCase());
    }

    private static Node createDecorationNode(ValidationMessage message) {
        var graphic = Severity.ERROR == message.getSeverity() ? createErrorNode() : createWarningNode();
        graphic.getStyleClass().addAll("validation-mark", message.getSeverity().toString().toLowerCase());
        Tooltip.install(graphic, createTooltip(message));
        return graphic;
    }

    private static Tooltip createTooltip(ValidationMessage message) {
        var tooltip = new Tooltip(message.getText());
        tooltip.setAutoFix(true);
        tooltip.getStyleClass().add(message.getSeverity().toString().toLowerCase());
        return tooltip;
    }

    private static ImageView createErrorNode() {
        return new ImageView(ERROR_IMAGE);
    }

    private static ImageView createWarningNode() {
        return new ImageView(WARNING_IMAGE);
    }

    @Override
    public void add(Node targetNode) {
        decorations.forEach(d -> d.add(targetNode));
    }

    @Override
    public void remove(Node targetNode) {
        decorations.forEach(d -> d.remove(targetNode));
    }

}
