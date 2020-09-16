package com.upgrade.challenge.reservationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.upgrade.challenge.commonservicelibrary.service.model.CampsiteModel;
import com.upgrade.challenge.commonservicelibrary.service.model.ReservationModel;
import com.upgrade.challenge.commonservicelibrary.service.model.UserModel;
import com.upgrade.challenge.reservationservice.handler.ReservationHandler;
import com.upgrade.challenge.reservationservice.repository.ReservationRepository;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationEntity;
import com.upgrade.challenge.commonservicelibrary.service.CampsiteService;
import com.upgrade.challenge.commonservicelibrary.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationServiceApplicationTests {

	@MockBean
	UserService userServiceMock;

	@MockBean
	CampsiteService campsiteService;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationHandler reservationHandler;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@Test
	public void concurrentTestCreateReservationApi() throws InterruptedException, ExecutionException {

		UserModel userModel = new UserModel(UUID.randomUUID().toString(), "test", "test", "test@test.com");
		CampsiteModel campsiteModel = new CampsiteModel(UUID.randomUUID().toString(), "TEST_CAMPSITE");
		ReservationModel reservationModel = new ReservationModel(userModel, null, LocalDate.now().plusDays(1),
				LocalDate.now().plusDays(2));
		given(userServiceMock.getUserByEmail(userModel.getEmail()))
				.willReturn(CompletableFuture.completedFuture(Arrays.asList(userModel)));
		given(campsiteService.getCampsiteById(campsiteModel.getCampsiteId()))
				.willReturn(CompletableFuture.completedFuture(campsiteModel));

		try {
			callCreateReservationApiMultipleTimes(5, campsiteModel, reservationModel);
		} catch (Exception e) {
		}

		Iterable<ReservationEntity> reservations = reservationRepository.findAll();
		assertEquals(1, ((Collection<?>) reservations).size(), "One element should have been inserted only");
	}

	private void callCreateReservationApiMultipleTimes(int count, CampsiteModel campsiteModel,
			ReservationModel reservationModel) {
		List<Future<ReservationModel>> futures = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			futures.add(executor.submit(() -> {
				try {
					return reservationHandler.createReservation(campsiteModel.getCampsiteId(), reservationModel).get();
				} catch (Throwable e) {
				}
				return null;
			}));
		}

		for (Future<ReservationModel> future : futures) {
			try {
				future.get();
			} catch (Throwable e) {
			}
		}

		try {
			executor.getThreadPoolExecutor().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		executor.shutdown();
	}
}
