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

import javafx.scene.Node;

import java.util.function.Supplier;

/**
 * @author Andrea Vacondio
 */
public interface OverlayItem {

    String id();

    String name();

    Supplier<Node> panel();

    /**
     * @return true if the overlay should be closed when a second show request arrives. Ex. the log panel has a button to show it, first click shows the panel,
     * second click hides it.
     */
    default boolean closeOnSecondPress() {
        return false;
    }
}
