package com.credit.score.simulator.controller

import com.credit.score.simulator.CreditScoreSimulatorApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [CreditScoreSimulatorApp::class],
)
@AutoConfigureWebTestClient
class LoanSimulationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun baseCase() {
        val requestPayload = """
            {
              "loanValue": 10000,
              "birthDate": "2005-01-19",
              "paymentTermInMonths": 60
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.loanSimulation.installmentRate").isEqualTo(188.71)
            .jsonPath("$.loanSimulation.finalValue").isEqualTo(11322.6)
            .jsonPath("$.loanSimulation.totalInterest").isEqualTo(1322.6)
            .jsonPath("$.loanSimulation.yearlyRate").isEqualTo(0.05)
    }

    @Test
    fun testSimulationAge25To40() {
        val requestPayload = """
            {
              "loanValue": 10000,
              "birthDate": "1993-01-01",
              "paymentTermInMonths": 60
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.loanSimulation.installmentRate").isEqualTo(179.69)
            .jsonPath("$.loanSimulation.finalValue").isEqualTo(10781.4)
            .jsonPath("$.loanSimulation.totalInterest").isEqualTo(781.4)
            .jsonPath("$.loanSimulation.yearlyRate").isEqualTo(0.03)
    }

    @Test
    fun testSimulationAge40To60() {
        val requestPayload = """
            {
              "loanValue": 10000,
              "birthDate": "1973-01-01",
              "paymentTermInMonths": 60
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.loanSimulation.installmentRate").isEqualTo(175.28)
            .jsonPath("$.loanSimulation.finalValue").isEqualTo(10516.8)
            .jsonPath("$.loanSimulation.totalInterest").isEqualTo(516.8)
            .jsonPath("$.loanSimulation.yearlyRate").isEqualTo(0.02)
    }

    @Test
    fun testSimulationAgeAbove60() {
        val requestPayload = """
            {
              "loanValue": 10000,
              "birthDate": "1950-01-01",
              "paymentTermInMonths": 60
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.loanSimulation.installmentRate").isEqualTo(184.17)
            .jsonPath("$.loanSimulation.finalValue").isEqualTo(11050.2)
            .jsonPath("$.loanSimulation.totalInterest").isEqualTo(1050.2)
            .jsonPath("$.loanSimulation.yearlyRate").isEqualTo(0.04)
    }

    @Test
    fun blockFutureDate() {
        val requestPayload = """
            {
              "loanValue": 10000,
              "birthDate": "2030-01-01",
              "paymentTermInMonths": 60
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.errorMessage").value<String> { errorMsg ->
                assertThat(errorMsg).isEqualTo("Data de nascimento não pode ser futura")
            }
    }

    @Test
    fun blockNegativePaymentTerm() {
        // Cenário: Prazo negativo para pagamento. Deve retornar erro.
        val requestPayload = """
            {
              "loanValue": 10000,
              "birthDate": "1990-01-01",
              "paymentTermInMonths": -12
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.errorMessage").value<String> { errorMsg ->
                assertThat(errorMsg).isEqualTo("Informe um número positivo de meses")
            }
    }

    @Test
    fun blockNegativeLoanValue() {
        val requestPayload = """
            {
              "loanValue": -5000,
              "birthDate": "1990-01-01",
              "paymentTermInMonths": 60
            }
        """.trimIndent()

        webTestClient.post()
            .uri("/api/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestPayload)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.errorMessage").value<String> { errorMsg ->
                assertThat(errorMsg).isEqualTo("Informe um valor de empréstimo maior ou igual 1")
            }
    }
}
