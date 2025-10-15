package com.avenuecode.weatherforecast.zipcode.service;

import com.avenuecode.weatherforecast.config.ZipCodeApiProperties;
import com.avenuecode.weatherforecast.zipcode.dto.ZipCodeDataDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ZipCodeServiceTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ZipCodeApiProperties properties;

    @InjectMocks
    private ZipCodeService zipCodeService;

    @Test
    void getZipCodeData_whenApiReturnsValidResponse_thenSuccessResponse() {
        String zipCode = "94108";
        ZipCodeDataDTO expectedZipCodeData = new ZipCodeDataDTO(37.7906691, -122.4063213);
        ZipCodeDataDTO[] zipCodeResponse = new ZipCodeDataDTO[]{ expectedZipCodeData };

        when(restClient.get().uri(any(Function.class)).retrieve().body(ZipCodeDataDTO[].class)).thenReturn(zipCodeResponse);

        ZipCodeDataDTO result = zipCodeService.getZipCodeData(zipCode);

        assertNotNull(result);
        assertEquals(expectedZipCodeData, result);
    }

        @Test
        void getZipCodeData_whenApiReturnsEmptyData_thenException() {
            String zipCode = "123123123";
            ZipCodeDataDTO[] zipCodeResponse = new ZipCodeDataDTO[]{};

            when(restClient.get().uri(any(Function.class)).retrieve().body(ZipCodeDataDTO[].class)).thenReturn(zipCodeResponse);

            assertThrows(ResponseStatusException.class, () -> zipCodeService.getZipCodeData(zipCode));
        }

//        @Test
//        void getZipCodeData_whenApiReturns4xx_ExpectException() {
//            String zipCode = "123123123";
//            ZipCodeDataDTO[] zipCodeResponse = new ZipCodeDataDTO[]{};
//
//            when(restClient.get().uri(any(Function.class)).retrieve().body(ZipCodeDataDTO[].class)).thenReturn(zipCodeResponse);
//
//            assertThrows(ResponseStatusException.class, () -> zipCodeService.getZipCodeData(zipCode));
//        }
}
