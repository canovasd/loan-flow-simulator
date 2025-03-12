package com.credit.score.simulator.calculator

import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
import java.time.Period

interface AgeCalculator {
    suspend fun calculateAge(birthDate: LocalDate): Int
}

@Component
class AgeCalculatorImpl(private val clock: Clock) : AgeCalculator {
    override suspend fun calculateAge(birthDate: LocalDate): Int {
        return Period.between(
            birthDate,
            LocalDate.now(clock)
        ).years
    }
}