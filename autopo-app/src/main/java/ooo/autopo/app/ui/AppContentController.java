package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 17/02/25
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
import jakarta.inject.Named;
import ooo.autopo.model.ui.HideOverlayItem;
import ooo.autopo.model.ui.SetOverlayItem;
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
    public void onSetActiveOverlay(SetOverlayItem request) {
        Logger.trace("Request to set overlay to '{}'", request.id());
        var item = overlays.get(request.id());
        if (nonNull(item)) {
            if (!item.equals(this.activeOverlay) || !item.closeOnSecondPress()) {
                this.activeOverlay = item;
                container.overlay(item.panel().get());
                eventStudio().broadcast(new SetTitleRequest(item.name()));
            } else {
                hideOverlay(HideOverlayItem.INSTANCE);
            }
        }
    }

    @EventListener
    public void hideOverlay(HideOverlayItem request) {
        Logger.trace("Request to hide the overlay node");
        container.hideOverlay();
        this.activeOverlay = null;
        eventStudio().broadcast(SetTitleRequest.SET_DEFAULT_TITLE_REQUEST);
    }
}
