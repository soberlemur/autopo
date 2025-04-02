package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 19/02/25
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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Andrea Vacondio
 */
public class RandomProjectNameGenerator {

    private final List<String> names;

    private RandomProjectNameGenerator() {
        this.names = List.of("Hot Banana",
                             "Sardonic Potato",
                             "Smooth Chinchilla",
                             "Witty Pineapple",
                             "Pink Turtle",
                             "Funky Mango",
                             "Clever Walrus",
                             "Playful Stick",
                             "Dancing Avocado",
                             "Ninja Carrot",
                             "Introvert Cucumber",
                             "Snarky Eggplant",
                             "Bald Flamingo",
                             "Sassy Papaya",
                             "Dangling Llama",
                             "Educated Cupboard",
                             "Whiny Mirror",
                             "Hairy Pomegranate",
                             "Sporty Sloth",
                             "Illiterate Crab",
                             "Fierce Chicken",
                             "Methodical Penguin",
                             "Stubborn Coconut",
                             "Blissful Chair",
                             "Tidy Toad",
                             "Shiny Booger",
                             "Biting Tungsten");
    }

    /**
     * @return a random name
     */
    public String getName() {
        return names.get(ThreadLocalRandom.current().nextInt(names.size()));
    }

    public static RandomProjectNameGenerator instance() {
        return ProjectNamesHolder.GENERATOR;
    }

    /**
     * Lazy initialization holder
     *
     * @author Andrea Vacondio
     */
    private static final class ProjectNamesHolder {

        private ProjectNamesHolder() {
            // hide constructor
        }

        static final RandomProjectNameGenerator GENERATOR = new RandomProjectNameGenerator();
    }
}
