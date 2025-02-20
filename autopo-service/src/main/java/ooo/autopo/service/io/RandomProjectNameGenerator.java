package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 19/02/25
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
