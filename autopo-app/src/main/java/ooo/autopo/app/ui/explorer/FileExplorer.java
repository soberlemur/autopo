package ooo.autopo.app.ui.explorer;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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
import atlantafx.base.theme.Tweaks;
import com.soberlemur.potentilla.Catalog;
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
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.notification.AddNotificationRequest;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.model.po.PoAddRequest;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PoUpdateRequest;
import ooo.autopo.model.project.ProjectLoadRequest;
import ooo.autopo.model.project.ProjectSaveRequest;
import ooo.autopo.model.ui.SetOverlayItemRequest;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.tinylog.Logger;

import java.nio.file.Files;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javafx.beans.binding.Bindings.notEqual;
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

        var editProject = new MenuItem(i18n().tr("Edit project"));
        editProject.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        editProject.setOnAction(e -> eventStudio().broadcast(new SetOverlayItemRequest("PROJECT_SETTINGS")));
        var editProjectContextMenu = new ContextMenu(editProject);

        var selectTemplate = new MenuItem(i18n().tr("Select template"));
        selectTemplate.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        selectTemplate.setOnAction(e -> selectTemplate());
        var selectTemplateContextMenu = new ContextMenu(selectTemplate);

        var addTranslation = new MenuItem(i18n().tr("Add translation"));
        addTranslation.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        addTranslation.setOnAction(e -> {
            var poPath = Choosers.fileChooser(i18n().tr("Save a .po translation file"), FileType.PO).showSaveDialog(this.getScene().getWindow());
            if (nonNull(poPath)) {
                var filename = poPath.getFileName().toString();
                if (!filename.toLowerCase().endsWith(".po")) {
                    poPath = poPath.resolveSibling(filename + ".po");
                }
                if (Files.exists(poPath)) {
                    eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR,
                                                                       i18n().tr("The file {0} already exists", poPath.getFileName().toString())));
                } else if (!poPath.toAbsolutePath().startsWith(app().currentProject().location().toAbsolutePath())) {
                    eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR,
                                                                       i18n().tr("The file {0} is not inside the current project directory",
                                                                                 poPath.getFileName().toString())));
                } else {
                    var poFile = new PoFile(poPath);
                    poFile.catalog(new Catalog().withDefaultHeader());
                    eventStudio().broadcast(new PoAddRequest(app().currentProject(), poFile), "LANGUAGE_SELECTION_STATION");
                }
            }
        });

        var updateAll = new MenuItem(i18n().tr("Update all from pot"));
        updateAll.setOnAction(e -> eventStudio().broadcast(new PoUpdateRequest(app().currentProject().pot().get(), app().currentProject().translations())));

        app().runtimeState().project().subscribe(project -> {
            addTranslation.disableProperty().unbind();
            updateAll.disableProperty().unbind();
            addTranslation.setDisable(true);
            updateAll.setDisable(true);
            if (nonNull(project)) {
                project.pot().subscribe(pot -> {
                    if (nonNull(pot)) {
                        addTranslation.disableProperty().bind(notEqual(pot.status(), LoadingStatus.LOADED));
                        updateAll.disableProperty().bind(notEqual(pot.status(), LoadingStatus.LOADED));
                    }
                });
            }
        });

        var clearObsolete = new MenuItem(i18n().tr("Remove obsolete entries from every .po file"));
        clearObsolete.setOnAction(e -> {
            for (var po : app().currentProject().translations()) {
                if (po.isLoaded()) {
                    po.clearObsolete();
                } else {
                    Logger.warn("Cannot remove obsolete entries from {} because it is not loaded yet", po.poFile().getFileName().toString());
                }
            }
        });

        var addTranslationContextMenu = new ContextMenu(addTranslation, updateAll, clearObsolete);

        var treeView = getTreeNodeTreeView(selectTemplateContextMenu, editProjectContextMenu, addTranslationContextMenu);
        treeView.getStyleClass().addAll(Styles.DENSE, Tweaks.ALT_ICON, Tweaks.EDGE_TO_EDGE, "files-tree-view");
        this.setCenter(treeView);
    }

    private TreeView<TreeNode> getTreeNodeTreeView(ContextMenu selectTemplateContextMenu, ContextMenu renameProjectContextMenu,
            ContextMenu addTranslationContextMenu) {

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
                    case PO_PARENT -> setContextMenu(addTranslationContextMenu);
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
                eventStudio().broadcast(new ProjectSaveRequest(p));
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

    @EventListener
    public void onSaveProject(ProjectSaveRequest request) {
        actualizeRootName();
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

    @EventListener(priority = Integer.MIN_VALUE)
    public void onPoAdd(PoAddRequest request) {
        request.complete().subscribe((o, n) -> {
            if (n) {
                //TODO add it in order, in the correct position
                translationsRootItem.getChildren()
                                    .add(new TreeItem<>(TreeNode.ofPo(request.poFile(),
                                                                      () -> app().runtimeState().poFile(request.poFile()),
                                                                      new PoContextMenu(request.poFile()))));
            }
        });
    }

}
