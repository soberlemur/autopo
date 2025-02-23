package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import jakarta.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Subscription;
import ooo.autopo.app.io.Choosers;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.project.LoadProjectRequest;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.project.ProjectProperty;
import ooo.autopo.model.project.SaveProjectRequest;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.nio.file.Files;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class FileExplorer extends BorderPane {

    private Project currentProject;
    private final TreeItem<TreeNode> root = new TreeItem<>();
    private final TreeItem<TreeNode> templateRootItem = new TreeItem<>(new TreeNode(NodeType.TEMPLATE, i18n().tr("Template"), null, null));

    @Inject
    public FileExplorer() {
        this.getStyleClass().add("file-explorer");
        eventStudio().addAnnotatedListeners(this);

        var expand = new Button();
        expand.getStyleClass().addAll(Styles.SMALL);
        expand.setGraphic(new FontIcon(FluentUiFilledAL.ARROW_MAXIMIZE_20));
        expand.setOnAction(e -> traverseTreeItems(root, true));
        expand.setTooltip(new Tooltip(i18n().tr("Expand")));

        var collapse = new Button();
        collapse.getStyleClass().addAll(Styles.SMALL);
        collapse.setGraphic(new FontIcon(FluentUiFilledAL.ARROW_MINIMIZE_20));
        collapse.setOnAction(e -> traverseTreeItems(root, false));
        collapse.setTooltip(new Tooltip(i18n().tr("Collapse")));

        var spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        var toolbar = new ToolBar(spacer, expand, collapse);
        toolbar.getStyleClass().add("file-explorer-toolbar");
        this.setTop(toolbar);

        var rename = new MenuItem(i18n().tr("Rename project"));
        rename.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        rename.setOnAction(e -> {
            var dialog = new RenameProjectDialog(root.getValue().name());
            dialog.initOwner(getScene().getWindow());
            dialog.showAndWait().ifPresent(name -> {
                if (StringUtils.isNotBlank(name)) {
                    currentProject.setProperty(ProjectProperty.NAME, name);
                    actualizeRootName();
                    eventStudio().broadcast(new SaveProjectRequest(currentProject));
                }
            });
        });
        var renameProjectContextMenu = new ContextMenu(rename);

        var selectTemplate = new MenuItem(i18n().tr("Select template"));
        selectTemplate.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        selectTemplate.setOnAction(e -> {
            var fileChooser = Choosers.fileChooser(i18n().tr("Select a template"), FileType.POT);

            var template = ofNullable(fileChooser.showOpenSingleDialog(this.getScene().getWindow())).filter(Files::isRegularFile)
                                                                                                    .map(t -> currentProject.location().relativize(t))
                                                                                                    .orElse(null);

            if (nonNull(template)) {
                currentProject.setProperty(ProjectProperty.TEMPLATE_PATH, template.toString());
                actualizeTemplate();
                eventStudio().broadcast(new SaveProjectRequest(currentProject));
            }

        });
        var templateContextMenu = new ContextMenu(selectTemplate);

        var treeView = new TreeView<>(root);
        treeView.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(TreeNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setContextMenu(null);
                    setTooltip(null);
                    setGraphic(null);
                } else {
                    setText(item.name());
                    setGraphic(item.graphics());
                    switch (item.type()) {
                    case PROJECT -> setContextMenu(renameProjectContextMenu);
                    case TEMPLATE -> setContextMenu(templateContextMenu);
                    case PO -> setContextMenu(null);
                    }
                    if (StringUtils.isNotBlank(item.tooltip())) {
                        setTooltip(new Tooltip(item.tooltip()));
                    } else {
                        setTooltip(null);
                    }
                }
            }
        });
        treeView.getStyleClass().addAll(Styles.DENSE, Tweaks.ALT_ICON, Tweaks.EDGE_TO_EDGE);
        this.setCenter(treeView);

    }

    private void traverseTreeItems(TreeItem<?> node, boolean expand) {
        if (nonNull(node)) {
            node.setExpanded(expand);
            for (TreeItem<?> child : node.getChildren()) {
                if (!child.isLeaf()) {
                    traverseTreeItems(child, expand);
                }
            }
        }
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onLoadProject(LoadProjectRequest request) {
        this.disableProperty().unbind();
        this.disableProperty().bind(Bindings.equal(LoadingStatus.LOADING, request.project().status()));
        this.currentProject = request.project();
        this.root.getChildren().clear();
        final Subscription[] subscription = new Subscription[1];
        subscription[0] = request.project().status().subscribe(status -> {
            if (status == LoadingStatus.LOADED) {
                actualizeRootName();
                root.getChildren().add(templateRootItem);
                actualizeTemplate();
                ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
            }
        });
    }

    private void actualizeRootName() {
        root.setValue(new TreeNode(NodeType.PROJECT, this.currentProject.getProperty(ProjectProperty.NAME), null, null));
    }

    private void actualizeTemplate() {
        var templatePath = ofNullable(this.currentProject.getProperty(ProjectProperty.TEMPLATE_PATH)).map(currentProject.location()::resolve)
                                                                                                     .orElse(null);
        if (nonNull(templatePath)) {
            templateRootItem.getChildren().clear();
            var templateItem = new TreeItem<>(new TreeNode(NodeType.TEMPLATE,
                                                           templatePath.getFileName().toString(),
                                                           templatePath.toAbsolutePath().toString(),
                                                           new FontIcon(FluentUiRegularAL.DOCUMENT_EDIT_20)));
            templateRootItem.getChildren().add(templateItem);
        }
    }

    public record TreeNode(NodeType type, String name, String tooltip, Node graphics) {
    }

    private enum NodeType {
        PROJECT,
        TEMPLATE,
        PO
    }
}
