package com.xecoding.portfolio.common

import com.xecoding.portfolio.di.ApiResponseModule
import com.xecoding.portfolio.di.TestNetworkModule
import com.xecoding.portfolio.di.TestRepositoryModule
import com.xecoding.portfolio.di.TestUseCaseModule
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@RunWith(JUnit4::class)
abstract class AbstractBaseKoinTest : KoinTest {

    @Before
    open fun setUp() {
        startKoin {
            modules(
                arrayListOf(
                    ApiResponseModule,
                    TestNetworkModule,
                    TestUseCaseModule,
                    TestRepositoryModule
                )
            )
        }
    }

    @After
    open fun tearDown() {
        stopKoin()
    }
}