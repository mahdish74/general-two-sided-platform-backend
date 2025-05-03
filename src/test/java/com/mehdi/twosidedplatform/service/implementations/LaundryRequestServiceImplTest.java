package com.mehdi.twosidedplatform.service.implementations;

import com.mehdi.twosidedplatform.dto.LaundryRequestDto;
import com.mehdi.twosidedplatform.entity.LaundryRequest;
import com.mehdi.twosidedplatform.entity.User;
import com.mehdi.twosidedplatform.entity.enums.LaundryRequestStatus;
import com.mehdi.twosidedplatform.repository.LaundryRequestRepository;
import com.mehdi.twosidedplatform.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LaundryRequestServiceImplTest {

    @Mock
    private LaundryRequestRepository laundryRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LaundryRequestServiceImpl laundryRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_shouldSaveAndReturnDto() {
        // Given
        LaundryRequestDto dto = new LaundryRequestDto();
        dto.setCustomerId(1L);
        dto.setAddress("123 Main St");
        dto.setPreferredPickupTime(LocalDateTime.of(2025, 5, 3, 10, 0));
        dto.setSpecialInstructions("Use non-scented detergent");

        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(mockUser));

        LaundryRequest savedRequest = new LaundryRequest();
        savedRequest.setId(99L);
        savedRequest.setCustomer(mockUser);
        savedRequest.setStatus(LaundryRequestStatus.NEW);
        when(laundryRequestRepository.save(any())).thenReturn(savedRequest);

        // When
        LaundryRequestDto result = laundryRequestService.createRequest(dto);

        // Then
        assertNotNull(result.getId());
        assertEquals(LaundryRequestStatus.NEW, result.getStatus());
        verify(laundryRequestRepository).save(any(LaundryRequest.class));
    }
}
