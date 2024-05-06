// SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
//
// SPDX-License-Identifier: Apache-2.0

package io.github.lmos.arc.spring

import io.github.lmos.arc.agents.AgentProvider
import io.github.lmos.arc.agents.functions.LLMFunctionProvider
import io.github.lmos.arc.agents.getAgentByName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AgentScriptTest {

    @Autowired
    lateinit var agentProvider: AgentProvider

    @Autowired
    lateinit var llmFunctionProvider: LLMFunctionProvider

    @Test
    fun `test agent defined as script`(): Unit = runBlocking {
        var i = 0
        while (agentProvider.getAgentByName("weather") == null && i < 30) {
            delay(1000)
            i++
        }
        assertThat(agentProvider.getAgentByName("weather")).isNotNull
    }

    @Test
    fun `test function defined as script`(): Unit = runBlocking {
        var i = 0

        while (i < 30) {
            try {
                llmFunctionProvider.provide("get_weather")
                break // If no exception is thrown, break the loop
            } catch (e: NoSuchElementException) {
                delay(1000)
                i++
            }
        }

        assertThatNoException().isThrownBy { (llmFunctionProvider.provide("get_weather")) }
    }
}
