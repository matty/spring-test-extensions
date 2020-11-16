/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.matty.springframework.test.web.servlet.result;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.FlashAttributeResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.FlashMap;

import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Extensions for {@link FlashAttributeResultMatchers}.
 */
public class FlashAttributeResultMatchersExt extends FlashAttributeResultMatchers {

    /**
     * Assert the flash map attribute has field error.
     */
    public ResultMatcher attributeHasFieldErrors(String name, String... fieldNames) {
        return result -> {
            FlashMap fm = getFlashMap(result);
            Errors errors = getBindingResult(fm, name);

            assertTrue("No errors for attribute '" + name + "'", errors.hasErrors());
            for (String fieldName : fieldNames) {
                boolean hasFieldErrors = errors.hasFieldErrors(fieldName);
                assertTrue("No errors for field '" + fieldName + "' of attribute '" + name + "'", hasFieldErrors);
            }
        };
    }

    /**
     * Assert the flash map attribute has errors.
     */
    public ResultMatcher attributeErrorCount(String name, int expectedErrorCount) {
        return result -> {
            FlashMap fm = getFlashMap(result);
            Errors errors = getBindingResult(fm, name);
            assertEquals("Binding/validation error count for attribute '" + name + "',",
                    expectedErrorCount, errors.getErrorCount());
        };
    }

    private FlashMap getFlashMap(MvcResult result) {
        FlashMap flashMap = result.getFlashMap();
        assertNotNull("No FlashMap found", flashMap);
        return flashMap;
    }

    /**
     * Retrieve {@link BindingResult} from the attribute name.
     *
     * @param flashMap {@link FlashMap}.
     * @param name     Attribute name.
     * @return {@link BindingResult}.
     */
    private BindingResult getBindingResult(FlashMap flashMap, String name) {
        BindingResult result = (BindingResult) flashMap.get(name);
        assertNotNull("No BindingResult found for attribute: " + name, result);
        return result;
    }

    /**
     * Access flash attribute assertion extensions.
     *
     * @return {@link FlashAttributeResultMatchersExt}.
     */
    public static FlashAttributeResultMatchersExt flashExt() {
        return new FlashAttributeResultMatchersExt();
    }
}
