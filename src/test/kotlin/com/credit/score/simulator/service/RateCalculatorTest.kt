package com.credit.score.simulator.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class RateCalculatorTest {

    @Test
    fun calculateRateTwentyYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(20)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(2004, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.05"))
    }

    @Test
    fun calculateRateTwentyFiveYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(25)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(2000, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.05"))
    }

    @Test
    fun calculateRateTwentySixYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(26)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(1999, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.03"))
    }

    @Test
    fun calculateRateFourtyYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(40)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(1985, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.03"))
    }

    @Test
    fun calculateRateFourtyOneYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(41)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(1984, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.02"))
    }

    @Test
    fun calculateRateSixtyYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(60)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(1965, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.02"))
    }

    @Test
    fun calculateRateSixtyOneYears() {
        val ageCalculator = mock<AgeCalculator>()
        whenever(runBlocking { ageCalculator.calculateAge(any(), any()) }).thenReturn(61)

        val target = RateCalculator(ageCalculator)
        val result = runBlocking { target.calculateYearlyRate(LocalDate.of(1964, 1, 1)) }

        assertThat(result).isEqualTo(BigDecimal("0.04"))
    }
}