package com.example.claudeapi.fee.application;

import com.example.claudeapi.fee.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeeService 테스트")
class FeeServiceTest {

    @Mock
    private FeeRepository feeRepository;

    @InjectMocks
    private FeeService feeService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("유효한 수수료 정보로 생성하면")
        class Context_with_valid_request {

            @Test
            @DisplayName("새 수수료를 생성하고 반환한다")
            void it_creates_fee() {
                // given
                FeeRequest request = new FeeRequest(1L, 1000L, FeeUnit.WON);

                Fee fee = Fee.create(
                    request.getProductNumber(),
                    request.getFeeValue(),
                    request.getFeeUnit()
                );

                Fee savedFee = new Fee(
                    1L,
                    fee.getProductNumber(),
                    fee.getFeeValue(),
                    fee.getFeeUnit()
                );

                when(feeRepository.save(any(Fee.class))).thenReturn(savedFee);

                // when
                FeeResponse response = feeService.create(request);

                // then
                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getProductNumber()).isEqualTo(1L);
                assertThat(response.getFeeValue()).isEqualTo(1000L);
                assertThat(response.getFeeUnit()).isEqualTo(FeeUnit.WON);
                verify(feeRepository).save(any(Fee.class));
            }
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_findById {

        @Nested
        @DisplayName("존재하는 ID로 조회하면")
        class Context_with_existing_id {

            @Test
            @DisplayName("수수료 정보를 반환한다")
            void it_returns_fee() {
                // given
                Fee fee = new Fee(1L, 1L, 1000L, FeeUnit.WON);

                when(feeRepository.findById(1L)).thenReturn(Optional.of(fee));

                // when
                FeeResponse response = feeService.findById(1L);

                // then
                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getProductNumber()).isEqualTo(1L);
                assertThat(response.getFeeValue()).isEqualTo(1000L);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 ID로 조회하면")
        class Context_with_non_existing_id {

            @Test
            @DisplayName("FeeNotFoundException을 던진다")
            void it_throws_exception() {
                // given
                when(feeRepository.findById(999L)).thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> feeService.findById(999L))
                        .isInstanceOf(FeeNotFoundException.class)
                        .hasMessageContaining("999");
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 수수료를 수정하면")
        class Context_with_existing_fee {

            @Test
            @DisplayName("수정된 수수료를 반환한다")
            void it_updates_fee() {
                // given
                Fee existingFee = new Fee(1L, 1L, 1000L, FeeUnit.WON);
                FeeRequest request = new FeeRequest(2L, 2000L, FeeUnit.PERCENT);

                when(feeRepository.findById(1L)).thenReturn(Optional.of(existingFee));
                when(feeRepository.save(any(Fee.class))).thenReturn(
                    new Fee(1L, 2L, 2000L, FeeUnit.PERCENT)
                );

                // when
                FeeResponse response = feeService.update(1L, request);

                // then
                assertThat(response.getProductNumber()).isEqualTo(2L);
                assertThat(response.getFeeValue()).isEqualTo(2000L);
                assertThat(response.getFeeUnit()).isEqualTo(FeeUnit.PERCENT);
            }
        }
    }
}
