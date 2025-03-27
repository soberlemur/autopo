package ooo.autopo.app.ui.project;

/*
 * This file is part of the Autopo project
 * Created 19/03/25
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
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import net.synedra.validatorfx.Validator;
import ooo.autopo.app.ui.components.CloseOverlayButton;
import ooo.autopo.app.ui.components.NotClosable;
import ooo.autopo.model.project.ProjectProperty;
import ooo.autopo.model.project.SaveProjectRequest;
import ooo.autopo.model.ui.HideOverlayItem;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class ProjectSettingsPane extends BorderPane implements NotClosable {

    private final SimpleBooleanProperty modified = new SimpleBooleanProperty(false);
    private Validator validator = new Validator();

    public ProjectSettingsPane() {
        var settings = new GridPane();
        settings.getStyleClass().addAll("spaced-container", "project-settings-panel");
        settings.add(new Label(i18n().tr("Project name:")), 0, 0);

        var nameField = new TextField();
        nameField.setMinWidth(400);
        nameField.setPromptText("Project name");
        nameField.textProperty().subscribe((o, n) -> modified.set(true));

        GridPane.setFillWidth(nameField, true);
        settings.add(nameField, 1, 0);

        var descriptionLabel = new Label(i18n().tr(
                "This description will be provided to the AI model to improve translation accuracy by giving it context. Example: 'UI components for an e-commerce website that sells avocados."));
        settings.add(descriptionLabel, 0, 1, 3, 1);

        var descriptionArea = new TextArea();
        descriptionArea.setMinHeight(100);
        descriptionArea.setWrapText(true);
        descriptionArea.setPromptText("Project description");
        descriptionArea.textProperty().subscribe((o, n) -> modified.set(true));
        app().runtimeState().project().subscribe(p -> {
            nameField.setText(p.getProperty(ProjectProperty.NAME));
            descriptionArea.setText(p.getProperty(ProjectProperty.DESCRIPTION));
            modified.set(false);
        });

        validator.createCheck().dependsOn("name", nameField.textProperty()).withMethod(c -> {
            if (StringUtils.isBlank(c.get("name"))) {
                c.error(i18n().tr("Please enter a valid project name"));
            }
        }).decorates(nameField).immediate();

        GridPane.setFillWidth(descriptionArea, true);
        settings.add(descriptionArea, 0, 2, 3, 1);

        var saveButton = new Button();
        saveButton.setText(i18n().tr("_Save"));
        saveButton.setGraphic(new FontIcon(FluentUiRegularMZ.SAVE_20));
        saveButton.getStyleClass().addAll(Styles.SMALL);
        saveButton.setOnAction(e -> {
            app().currentProject().setProperty(ProjectProperty.NAME, nameField.getText());
            app().currentProject().setProperty(ProjectProperty.DESCRIPTION, descriptionArea.getText());
            eventStudio().broadcast(new SaveProjectRequest(app().currentProject()));
            eventStudio().broadcast(HideOverlayItem.INSTANCE);
            modified.set(false);
        });
        saveButton.disableProperty().bind(Bindings.or(modified.not(), validator.containsErrorsProperty()));
        var toolbar = new ToolBar(saveButton, new CloseOverlayButton());
        setTop(toolbar);
        setCenter(settings);
    }

}
