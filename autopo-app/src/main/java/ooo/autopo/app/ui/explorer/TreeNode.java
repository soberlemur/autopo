package ooo.autopo.app.ui.explorer;

/*
 * This file is part of the Autopo project
 * Created 06/03/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressBar;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PotFile;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.project.ProjectProperty;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.removeEnd;

/**
 * @author Andrea Vacondio
 */
public class TreeNode {

    private final StringProperty displayText = new SimpleStringProperty();
    private final TreeNodeType type;
    private final String tooltip;
    private final Node graphics;
    private final Runnable onDoubleClickAction;
    private final ContextMenu contextMenu;

    private TreeNode(TreeNodeType type, String displayText, String tooltip, Node graphics, Runnable onDoubleClickAction, ContextMenu contextMenu) {
        this.displayText.set(displayText);
        this.type = type;
        this.tooltip = tooltip;
        this.graphics = graphics;
        this.onDoubleClickAction = onDoubleClickAction;
        this.contextMenu = contextMenu;
    }

    public StringProperty displayTextProperty() {
        return displayText;
    }

    public TreeNodeType type() {
        return type;
    }

    public String tooltip() {
        return tooltip;
    }

    public Node graphics() {
        return graphics;
    }

    public Optional<Runnable> onDoubleClickAction() {
        return Optional.ofNullable(onDoubleClickAction);
    }

    public Optional<ContextMenu> contextMenu() {
        return Optional.ofNullable(contextMenu);
    }

    public static TreeNode of(TreeNodeType type, String displayText) {
        return new TreeNode(type, displayText, null, null, null, null);
    }

    public static TreeNode ofProject(Project project) {
        return new TreeNode(TreeNodeType.PROJECT, project.getProperty(ProjectProperty.NAME), null, null, null, null);
    }

    public static TreeNode ofPot(PotFile potFile, Runnable onDoubleClickAction) {
        return new TreeNode(TreeNodeType.TEMPLATE,
                            potFile.potFile().getFileName().toString(),
                            potFile.potFile().toAbsolutePath().toString(),
                            new FontIcon(FluentUiRegularAL.DOCUMENT_EDIT_20),
                            onDoubleClickAction,
                            null);
    }

    public static TreeNode ofPo(PoFile poFile, Runnable onDoubleClickAction, ContextMenu contextMenu) {
        var progress = new ProgressBar(0);
        poFile.translationPercentage().subscribe(n -> Platform.runLater(() -> progress.setProgress(n.doubleValue())));
        progress.getStyleClass().add(Styles.MEDIUM);
        var node = new TreeNode(TreeNodeType.PO,
                                poFile.poFile().getFileName().toString(),
                                poFile.poFile().toAbsolutePath().toString(),
                                progress,
                                onDoubleClickAction,
                                contextMenu);
        poFile.modifiedProperty().subscribe(o -> {
            if (o) {
                node.displayText.set(node.displayText.get() + " (*)");
            } else {
                node.displayText.set(removeEnd(node.displayText.get(), " (*)"));
            }
        });
        return node;
    }
}
