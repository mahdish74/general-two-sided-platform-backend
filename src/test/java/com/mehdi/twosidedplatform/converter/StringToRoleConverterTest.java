package com.mehdi.twosidedplatform.converter;

import com.mehdi.twosidedplatform.entity.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class StringToRoleConverterTest {

    private final StringToRoleConverter converter = new StringToRoleConverter();

    @Test
    void convert_validInput_returnsEnum() {
        assertEquals(Role.ADMIN, converter.convert("admin"));
    }

    @Test
    void convert_invalidInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("invalid_role"));
    }
}