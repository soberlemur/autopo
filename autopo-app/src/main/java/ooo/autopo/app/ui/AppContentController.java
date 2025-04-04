package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import ooo.autopo.model.ui.HideOverlayItemRequest;
import ooo.autopo.model.ui.SetOverlayItemRequest;
import ooo.autopo.model.ui.SetTitleRequest;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.function.Function.identity;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class AppContentController {

    private final AppContainer container;
    private final Map<String, OverlayItem> overlays;
    private OverlayItem activeOverlay;

    @Inject
    public AppContentController(AppContainer container, @Named("overlays") List<OverlayItem> overlays) {
        this.container = container;
        this.overlays = overlays.stream().collect(Collectors.toMap(OverlayItem::id, identity()));
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void onSetActiveOverlay(SetOverlayItemRequest request) {
        Logger.trace("Request to set overlay to '{}'", request.id());
        var item = overlays.get(request.id());
        if (nonNull(item)) {
            if (!item.equals(this.activeOverlay) || !item.closeOnSecondPress()) {
                this.activeOverlay = item;
                container.overlay(item.panel().get());
                eventStudio().broadcast(new SetTitleRequest(item.name()));
            } else {
                hideOverlay(HideOverlayItemRequest.INSTANCE);
            }
        }
    }

    @EventListener
    public void hideOverlay(HideOverlayItemRequest request) {
        Logger.trace("Request to hide the overlay node");
        container.hideOverlay();
        this.activeOverlay = null;
        eventStudio().broadcast(SetTitleRequest.SET_DEFAULT_TITLE_REQUEST);
    }
}
