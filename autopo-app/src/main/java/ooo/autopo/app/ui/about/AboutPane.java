package ooo.autopo.app.ui.about;
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
import jakarta.inject.Inject;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooo.autopo.app.ui.Style;
import ooo.autopo.app.ui.components.UrlButton;
import ooo.autopo.model.AppDescriptor;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;

import static ooo.autopo.app.io.ObjectCollectionWriter.writeContent;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.AppDescriptorProperty.COPYRIGHT;
import static ooo.autopo.model.AppDescriptorProperty.DONATE_URL;
import static ooo.autopo.model.AppDescriptorProperty.HOME_LABEL;
import static ooo.autopo.model.AppDescriptorProperty.HOME_URL;
import static ooo.autopo.model.AppDescriptorProperty.LICENSE_NAME;
import static ooo.autopo.model.AppDescriptorProperty.LICENSE_URL;
import static ooo.autopo.model.AppDescriptorProperty.NAME;
import static ooo.autopo.model.AppDescriptorProperty.SCM_URL;
import static ooo.autopo.model.AppDescriptorProperty.SUPPORT_URL;
import static ooo.autopo.model.AppDescriptorProperty.TRACKER_URL;
import static ooo.autopo.model.AppDescriptorProperty.VENDOR_URL;
import static ooo.autopo.model.AppDescriptorProperty.VERSION;

/**
 * @author Andrea Vacondio
 */
public class AboutPane extends HBox {

    @Inject
    public AboutPane(AppDescriptor descriptor) {
        getStyleClass().addAll("about-panel", "spaced-container");
        var left = new VBox(Style.DEFAULT_SPACING);
        var appSection = new VBox(Style.DEFAULT_SPACING);
        appSection.getStyleClass().add("section");
        addSectionTitle(descriptor.property(NAME, "Autopo"), left);
        appSection.getChildren().addAll(new Label(String.format("ver. %s", descriptor.property(VERSION))));
        addHyperlink(null, descriptor.property(VENDOR_URL), descriptor.property(COPYRIGHT), appSection);
        addHyperlink(null, descriptor.property(LICENSE_URL), descriptor.property(LICENSE_NAME), appSection);
        addHyperlink(FluentUiFilledAL.HOME_20, descriptor.property(HOME_URL), descriptor.property(HOME_LABEL), appSection);
        left.getChildren().add(appSection);

        addSectionTitle(i18n().tr("Environment"), left);
        var envSection = new VBox(Style.DEFAULT_SPACING);
        envSection.getStyleClass().add("section");
        var runtime = new Label(String.format("%s %s", System.getProperty("java.runtime.name"), System.getProperty("java.runtime.version")));
        var vendor = new Label(String.format(i18n().tr("Vendor: %s"), System.getProperty("java.vendor")));
        var runtimePath = new Label(String.format(i18n().tr("Java runtime path: %s"), System.getProperty("java.home")));
        var fx = new Label(String.format(i18n().tr("JavaFX runtime version: %s"), System.getProperty("javafx.runtime.version")));
        Button copyButton = new Button(i18n().tr("Copy to clipboard"));
        copyButton.setGraphic(FontIcon.of(FluentUiRegularAL.COPY_20));
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
        envSection.getChildren().addAll(runtime, vendor, runtimePath, fx, copyButton);
        left.getChildren().add(envSection);

        var right = new VBox(Style.DEFAULT_SPACING);
        addSectionTitle(i18n().tr("Support"), right);
        var supportSection = new VBox(Style.DEFAULT_SPACING);
        supportSection.getStyleClass().add("section");
        addHyperlink(FluentUiRegularAL.BUG_20, descriptor.property(TRACKER_URL), i18n().tr("Bug and feature requests"), supportSection);
        addHyperlink(FluentUiFilledMZ.QUESTION_CIRCLE_20, descriptor.property(SUPPORT_URL), i18n().tr("Support"), supportSection);
        addHyperlink(FluentUiFilledAL.CODE_20, descriptor.property(SCM_URL), i18n().tr("Fork me on GitHub"), supportSection);
        addHyperlink(FluentUiRegularMZ.MONEY_20, descriptor.property(DONATE_URL), i18n().tr("Donate"), supportSection);
        right.getChildren().add(supportSection);
        getChildren().addAll(left, right);
    }

    private void addSectionTitle(String title, Pane pane) {
        Label label = new Label(title);
        label.getStyleClass().add(Styles.TITLE_3);
        pane.getChildren().add(label);
    }

    private void addHyperlink(Ikon icon, String url, String text, Pane pane) {
        UrlButton button = UrlButton.styledUrlButton(text, url, icon);
        button.getStyleClass().setAll("hyperlink");
        pane.getChildren().add(button);
    }
}
