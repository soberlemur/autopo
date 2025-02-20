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

import jakarta.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Subscription;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.project.LoadProjectRequest;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.project.ProjectProperty;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.nio.file.Paths;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class FileExplorer extends BorderPane {

    private Project currentProject;
    private TreeItem<String> root = new TreeItem<>();

    @Inject
    public FileExplorer() {
        this.getStyleClass().add("file-explorer");
        eventStudio().addAnnotatedListeners(this);

        var expand = new Button();
        expand.getStyleClass().add("file-explorer-toolbar-button");
        expand.setGraphic(new FontIcon(FluentUiFilledAL.ARROW_MAXIMIZE_20));
        expand.setOnAction(e -> root.setExpanded(true));
        expand.setTooltip(new Tooltip(i18n().tr("Expand")));

        var collapse = new Button();
        collapse.getStyleClass().add("file-explorer-toolbar-button");
        collapse.setGraphic(new FontIcon(FluentUiFilledAL.ARROW_MINIMIZE_20));
        collapse.setOnAction(e -> root.setExpanded(false));
        collapse.setTooltip(new Tooltip(i18n().tr("Collapse")));

        var toolbar = new HBox(expand, collapse);
        toolbar.getStyleClass().add("file-explorer-toolbar");
        HBox.setHgrow(toolbar, javafx.scene.layout.Priority.ALWAYS);
        this.setTop(toolbar);

        var treeView = new TreeView<>(root);
        treeView.getStyleClass().add("file-explorer-tree");
        this.setCenter(treeView);

    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onLoadProject(LoadProjectRequest request) {
        this.disableProperty().unbind();
        this.disableProperty().bind(Bindings.equal(LoadingStatus.LOADING, request.project().status()));
        this.currentProject = request.project();
        final Subscription[] subscription = new Subscription[1];
        subscription[0] = request.project().status().subscribe(status -> {
            if (status == LoadingStatus.LOADED) {
                root.setValue(this.currentProject.getProperty(ProjectProperty.NAME));
                var templateRootItem = new TreeItem<>(i18n().tr("Template"));
                var templatePath = ofNullable(this.currentProject.getProperty(ProjectProperty.TEMPLATE_PATH)).map(Paths::get).orElse(null);
                if (Objects.nonNull(templatePath)) {
                    var templateItemGraphics = new FontIcon(FluentUiRegularAL.DOCUMENT_EDIT_20);
                    Tooltip.install(templateItemGraphics, new Tooltip(templatePath.toAbsolutePath().toString()));
                    var templateItem = new TreeItem<>(templatePath.getFileName().toString(), templateItemGraphics);
                    templateRootItem.getChildren().add(templateItem);
                }
                root.getChildren().add(templateRootItem);
                ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
            }
        });
    }
}
