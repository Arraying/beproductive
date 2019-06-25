package de.arraying.beproductive.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class CollectionUtil {

    /**
     * Creates a set from variadic elements.
     * @param elements The elements.
     * @param <T> The type of set.
     * @return A never null but possibly empty set.
     */
    @SafeVarargs
    public static <T> Set<T> set(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

}
