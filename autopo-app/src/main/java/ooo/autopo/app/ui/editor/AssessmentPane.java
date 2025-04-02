package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 26/03/25
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import ooo.autopo.app.ui.Style;
import ooo.autopo.model.ai.TranslationAssessment;
import ooo.autopo.model.po.PoEntry;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javafx.beans.binding.Bindings.createStringBinding;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class AssessmentPane extends VBox {

    private Label rate;
    private TextArea feedback;
    private TextArea suggestion;

    public AssessmentPane() {
        getStyleClass().addAll(Style.CONTAINER.css());
        this.rate = new Label(i18n().tr("Rate:"));
        this.rate.getStyleClass().add(Styles.TEXT_CAPTION);
        var feddbackTitle = new Label(i18n().tr("Feedback"));
        feddbackTitle.getStyleClass().add(Styles.TEXT_CAPTION);
        feedback = new TextArea();
        feedback.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        feedback.setWrapText(true);
        feedback.setEditable(false);

        var suggestionTitle = new Label(i18n().tr("Suggested replacement"));
        suggestionTitle.getStyleClass().add(Styles.TEXT_CAPTION);
        suggestion = new TextArea();
        suggestion.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        suggestion.setWrapText(true);
        suggestion.setEditable(false);

        var accept = new Button(i18n().tr("Accept suggestion"));
        accept.disableProperty().bind(suggestion.textProperty().isEmpty());
        accept.setOnAction(e -> ofNullable(app().currentPoEntry()).ifPresent(PoEntry::acceptSuggestion));
        getChildren().addAll(rate, feddbackTitle, feedback, suggestionTitle, suggestion, accept);

        app().runtimeState().poEntry().subscribe(e -> {
            feedback.textProperty().unbind();
            feedback.setText("");
            suggestion.textProperty().unbind();
            suggestion.setText("");
            rate.textProperty().unbind();
            rate.setText(i18n().tr("Rate:"));
            if (nonNull(e)) {
                feedback.textProperty()
                        .bind(createStringBinding(() -> ofNullable(e.assessment().get()).map(TranslationAssessment::feedback).orElse(""), e.assessment()));
                suggestion.textProperty()
                          .bind(createStringBinding(() -> ofNullable(e.assessment().get()).map(TranslationAssessment::suggestedReplacement).orElse(""),
                                                    e.assessment()));
                rate.textProperty()
                    .bind(createStringBinding(() -> ofNullable(e.assessment().get()).map(TranslationAssessment::score)
                                                                                    .map(s -> i18n().tr("Rate: {0}", Integer.toString(s)))
                                                                                    .orElse(""), e.assessment()));
            }
        });
    }
}
