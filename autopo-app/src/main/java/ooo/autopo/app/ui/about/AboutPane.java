package ooo.autopo.app.ui.about;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooo.autopo.app.AutopoDescriptor;
import ooo.autopo.app.ui.Style;
import ooo.autopo.app.ui.components.UrlButton;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;

import static ooo.autopo.app.AutopoDescriptorProperty.COPYRIGHT;
import static ooo.autopo.app.AutopoDescriptorProperty.DONATE_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.HOME_LABEL;
import static ooo.autopo.app.AutopoDescriptorProperty.HOME_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.LICENSE_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.NAME;
import static ooo.autopo.app.AutopoDescriptorProperty.SCM_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.SUPPORT_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.TRACKER_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.VENDOR_URL;
import static ooo.autopo.app.AutopoDescriptorProperty.VERSION;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.service.io.ObjectCollectionWriter.writeContent;

/**
 * @author Andrea Vacondio
 */
public class AboutPane extends HBox {

    @Inject
    public AboutPane(AutopoDescriptor descriptor) {
        getStyleClass().addAll("about-panel", "spaced-container");
        var left = new VBox();
        left.getStyleClass().add("section");
        addSectionTitle(descriptor.property(NAME, "Autopo"), left);
        left.getChildren().addAll(new Label(String.format("ver. %s", descriptor.property(VERSION))));
        addHyperlink(null, descriptor.property(VENDOR_URL), descriptor.property(COPYRIGHT), left);
        addHyperlink(null, descriptor.property(LICENSE_URL), "EULA", left);
        addHyperlink(FluentUiFilledAL.HOME_20, descriptor.property(HOME_URL), descriptor.property(HOME_LABEL), left);

        addSectionTitle(i18n().tr("Environment"), left);
        var runtime = new Label(String.format("%s %s", System.getProperty("java.runtime.name"), System.getProperty("java.runtime.version")));
        var vendor = new Label(String.format(i18n().tr("Vendor: %s"), System.getProperty("java.vendor")));
        var runtimePath = new Label(String.format(i18n().tr("Java runtime path: %s"), System.getProperty("java.home")));
        var fx = new Label(String.format(i18n().tr("JavaFX runtime version: %s"), System.getProperty("javafx.runtime.version")));
        Button copyButton = new Button(i18n().tr("Copy to clipboard"));
        copyButton.setGraphic(FontIcon.of(FluentUiRegularAL.COPY_20));
        copyButton.getStyleClass().addAll(Style.BUTTON.css());
        copyButton.setId("copyEnvDetails");
        copyButton.setOnAction(a -> {
            ClipboardContent content = new ClipboardContent();
            writeContent(Arrays.asList(descriptor.property(NAME, "Autopo"),
                                       descriptor.property(VERSION),
                                       runtime.getText(),
                                       vendor.getText(),
                                       runtimePath.getText(),
                                       fx.getText())).to(content);
            Clipboard.getSystemClipboard().setContent(content);
        });
        left.getChildren().addAll(runtime, vendor, runtimePath, fx, copyButton);
        VBox right = new VBox(6);
        addSectionTitle(i18n().tr("Support"), right);
        addHyperlink(FluentUiRegularAL.BUG_20, descriptor.property(TRACKER_URL), i18n().tr("Bug and feature requests"), right);
        addHyperlink(FluentUiFilledMZ.QUESTION_CIRCLE_20, descriptor.property(SUPPORT_URL), i18n().tr("Support"), right);
        addHyperlink(FluentUiFilledAL.CODE_20, descriptor.property(SCM_URL), i18n().tr("Fork me on GitHub"), right);
        addHyperlink(FluentUiRegularMZ.MONEY_20, descriptor.property(DONATE_URL), i18n().tr("Donate"), right);
        getChildren().addAll(left, right);
    }

    private void addSectionTitle(String title, Pane pane) {
        Label label = new Label(title);
        label.getStyleClass().add("section-title");
        pane.getChildren().add(label);
    }

    private void addHyperlink(Ikon icon, String url, String text, Pane pane) {
        UrlButton button = UrlButton.styledUrlButton(text, url, icon);
        button.getStyleClass().setAll("hyperlink");
        pane.getChildren().add(button);
    }
}
