package com.example.sppProject.cucumberTest.test

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["classpath:features"],
    glue = ["com.example.sppProject.cucumberTest"]
)
class CucumberTestOptions