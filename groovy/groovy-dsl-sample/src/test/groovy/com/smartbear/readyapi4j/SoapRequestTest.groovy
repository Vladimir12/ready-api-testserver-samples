package com.smartbear.readyapi4j

import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.RecipeExecutor
import org.junit.Test

import static com.smartbear.readyapi.client.model.ProjectResultReport.StatusEnum.FINISHED
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipeOnServer
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.remoteRecipeExecutor

class SoapRequestTest {
    private static final String TEST_SERVER_URL = 'http://testserver.readyapi.io:8080'
    private static final String TEST_SERVER_USER = 'demoUser'
    private static final String TEST_SERVER_PASSWORD = 'demoPassword'

    @Test
    void simpleSoapRequestUsingSoapUIEngine() throws Exception {
        Execution execution = executeRecipe {
            soapRequest {
                wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
                binding = 'GlobalWeatherSoap12'
                operation = 'GetWeather'
                namedParam 'CountryName', 'Sweden'
                pathParam '//*:CityName', 'Stockholm'
                asserting {
                    notSoapFault
                    schemaCompliance
                }
            }
        }

        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }

    @Test
    void simpleSoapRequestRunUsingTestServer() throws Exception {
        Execution execution = executeRecipeOnServer TEST_SERVER_URL, TEST_SERVER_USER, TEST_SERVER_PASSWORD, {
            soapRequest {
                wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
                binding = 'GlobalWeatherSoap12'
                operation = 'GetWeather'
                namedParam 'CountryName', 'Sweden'
                pathParam '//*:CityName', 'Stockholm'
                asserting {
                    notSoapFault
                    schemaCompliance
                }
            }
        }

        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }

    @Test
    void simpleSoapRequestRunUsingTheProvidedExecutor() throws Exception {
        //The executor can be initialized in set-up method if used in multiple tests
        RecipeExecutor executor = remoteRecipeExecutor(TEST_SERVER_URL, TEST_SERVER_USER, TEST_SERVER_PASSWORD)

        Execution execution = executeRecipe executor, {
            soapRequest {
                wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
                binding = 'GlobalWeatherSoap12'
                operation = 'GetWeather'
                namedParam 'CountryName', 'Sweden'
                pathParam '//*:CityName', 'Stockholm'
                asserting {
                    notSoapFault
                    schemaCompliance
                }
            }
        }

        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }
}
