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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.FlashMap;

import static github.matty.springframework.test.web.servlet.result.FlashAttributeResultMatchersExt.flashExt;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlashAttributeResultMatchersExtTest {

    @Mock
    MvcResult mvcResult;

    @Mock
    BindingResult bindingResult;

    @BeforeAll
    void before() {
        openMocks(this);
    }

    @Test
    void testAttributeErrorCount() throws Exception {
        FlashMap errors = flashMapWithErrors();
        when(mvcResult.getFlashMap()).thenReturn(errors);

        flashExt().attributeErrorCount("BindingResult", 1).match(mvcResult);
        assertThrows(AssertionError.class, () -> flashExt().attributeErrorCount("BindingResult", 0).match(mvcResult));
    }

    @Test
    void testAttributeFieldErrors() throws Exception {
        when(bindingResult.hasFieldErrors("test_field")).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        FlashMap fm = new FlashMap();
        fm.put("BindingResult", bindingResult);

        when(mvcResult.getFlashMap()).thenReturn(fm);

        flashExt().attributeHasFieldErrors("BindingResult", "test_field").match(mvcResult);
        assertThrows(AssertionError.class, () -> flashExt().attributeHasFieldErrors("BindingResult", "nomatch_field").match(mvcResult));

    }

    /**
     * Flash map with errors.
     */
    private FlashMap flashMapWithErrors() {
        when(bindingResult.getErrorCount()).thenReturn(1);

        FlashMap fm = new FlashMap();
        fm.put("BindingResult", bindingResult);
        return fm;
    }
}
