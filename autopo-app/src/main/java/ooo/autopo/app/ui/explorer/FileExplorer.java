package ooo.autopo.app.ui.explorer;

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
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Subscription;
import ooo.autopo.app.io.Choosers;
import ooo.autopo.app.ui.RenameProjectDialog;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.project.ProjectLoadRequest;
import ooo.autopo.model.project.ProjectProperty;
import ooo.autopo.model.project.SaveProjectRequest;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.nio.file.Files;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class FileExplorer extends BorderPane {

    private final TreeItem<TreeNode> root = new TreeItem<>();
    private final TreeItem<TreeNode> templateRootItem = new TreeItem<>(TreeNode.of(TreeNodeType.TEMPLATE, i18n().tr("Template")));
    private final TreeItem<TreeNode> translationsRootItem = new TreeItem<>(TreeNode.of(TreeNodeType.PO_PARENT, i18n().tr("Translations")));

    @Inject
    public FileExplorer() {
        this.getStyleClass().add("file-explorer");
        eventStudio().addAnnotatedListeners(this);

        var expand = new Button();
        expand.setGraphic(new FontIcon(FluentUiFilledAL.ARROW_MAXIMIZE_20));
        expand.getStyleClass().addAll(Styles.SMALL);
        expand.setOnAction(e -> traverseTreeItems(root, true));
        expand.setTooltip(new Tooltip(i18n().tr("Expand")));

        var collapse = new Button();
        collapse.setGraphic(new FontIcon(FluentUiFilledAL.ARROW_MINIMIZE_20));
        collapse.getStyleClass().addAll(Styles.SMALL);
        collapse.setOnAction(e -> traverseTreeItems(root, false));
        collapse.setTooltip(new Tooltip(i18n().tr("Collapse")));

        var spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        var toolbar = new ToolBar(spacer, expand, collapse);
        this.setTop(toolbar);

        var rename = new MenuItem(i18n().tr("Rename project"));
        rename.setOnAction(e -> {
            var dialog = new RenameProjectDialog(root.getValue().displayTextProperty().get());
            dialog.initOwner(getScene().getWindow());
            dialog.showAndWait().ifPresent(name -> {
                if (StringUtils.isNotBlank(name)) {
                    ofNullable(app().currentProject()).ifPresent(p -> {
                        p.setProperty(ProjectProperty.NAME, name);
                        actualizeRootName();
                        eventStudio().broadcast(new SaveProjectRequest(p));
                    });
                }
            });
        });
        var renameProjectContextMenu = new ContextMenu(rename);

        var selectTemplate = new MenuItem(i18n().tr("Select template"));
        selectTemplate.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        selectTemplate.setOnAction(e -> selectTemplate());
        var selectTemplateContextMenu = new ContextMenu(selectTemplate);

        var treeView = getTreeNodeTreeView(selectTemplateContextMenu, renameProjectContextMenu);
        treeView.getStyleClass().addAll(Styles.DENSE, Tweaks.ALT_ICON, Tweaks.EDGE_TO_EDGE, "files-tree-view");
        this.setCenter(treeView);
    }

    private TreeView<TreeNode> getTreeNodeTreeView(ContextMenu selectTemplateContextMenu, ContextMenu renameProjectContextMenu) {

        var treeView = new TreeView<>(root);
        treeView.setCellFactory(tv -> new TreeCell<>() {
            private Optional<Runnable> onDoubleClick;

            {
                addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (onDoubleClick.isPresent() && !isEmpty() && event.getClickCount() == 2 && getTreeItem().isLeaf()) {
                        event.consume();
                        onDoubleClick.get().run();
                    }
                });
            }

            @Override
            protected void updateItem(TreeNode item, boolean empty) {
                super.updateItem(item, empty);
                setGraphicTextGap(5);
                textProperty().unbind();
                if (empty || item == null) {
                    setText(null);
                    setContextMenu(null);
                    setTooltip(null);
                    setGraphic(null);
                    onDoubleClick = Optional.empty();
                } else {
                    textProperty().bind(item.displayTextProperty());
                    setGraphic(item.graphics());
                    switch (item.type()) {
                    case PROJECT -> setContextMenu(renameProjectContextMenu);
                    case TEMPLATE -> setContextMenu(selectTemplateContextMenu);
                    case PO_PARENT -> setContextMenu(null);
                    case PO -> item.contextMenu().ifPresent(this::setContextMenu);
                    }
                    setTooltip(ofNullable(item.tooltip()).filter(StringUtils::isNotBlank).map(Tooltip::new).orElse(null));
                    onDoubleClick = item.onDoubleClickAction();
                }
            }
        });
        return treeView;
    }

    private void selectTemplate() {
        var fileChooser = Choosers.fileChooser(i18n().tr("Select a template"), FileType.POT);

        var template = ofNullable(fileChooser.showOpenSingleDialog(this.getScene().getWindow())).filter(Files::isRegularFile).orElse(null);

        if (nonNull(template)) {
            ofNullable(app().currentProject()).ifPresent(p -> {
                p.pot(template);
                actualizeTemplate();
                eventStudio().broadcast(new SaveProjectRequest(p));
            });
        }
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
    public void onLoadProject(ProjectLoadRequest request) {
        this.disableProperty().unbind();
        this.disableProperty().bind(Bindings.equal(LoadingStatus.LOADING, request.project().status()));
        this.root.getChildren().clear();
        final Subscription[] subscription = new Subscription[1];
        subscription[0] = request.project().status().subscribe(status -> {
            if (status == LoadingStatus.LOADED) {
                Platform.runLater(() -> {
                    actualizeRootName();
                    root.getChildren().add(templateRootItem);
                    root.getChildren().add(translationsRootItem);
                    actualizeTemplate();
                    actualizeTranslations();
                    traverseTreeItems(root, true);
                    ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                });
            }
        });
    }

    private void actualizeRootName() {
        root.setValue(TreeNode.ofProject(app().currentProject()));
    }

    private void actualizeTemplate() {
        if (nonNull(app().currentProject().pot().get())) {
            templateRootItem.getChildren().clear();
            templateRootItem.getChildren().add(new TreeItem<>(TreeNode.ofPot(app().currentProject().pot().get(), this::selectTemplate)));
        }
    }

    private void actualizeTranslations() {
        translationsRootItem.getChildren().clear();
        app().currentProject()
             .translations()
             .stream()
             .map(t -> TreeNode.ofPo(t, () -> app().runtimeState().poFile(t), new PoContextMenu(t)))
             .map(TreeItem::new)
             .forEach(translationsRootItem.getChildren()::add);
    }

}
