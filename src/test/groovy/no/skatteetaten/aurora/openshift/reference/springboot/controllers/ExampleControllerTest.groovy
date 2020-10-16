package no.skatteetaten.aurora.openshift.reference.springboot.controllers

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.web.client.RestTemplate

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import no.skatteetaten.aurora.AuroraMetrics

@SpringBootTest(classes = [Config, RestTemplate, MetricsAutoConfiguration, AuroraMetrics], webEnvironment = NONE)
class ExampleControllerTest extends AbstractControllerTest {

  static class Config {
    @Bean
    MeterRegistry meterRegistry() {
      Metrics.globalRegistry
    }
  }

  @Autowired
  AuroraMetrics auroraMetrics

  @Autowired
  RestTemplate restTemplate

  def controller

  Boolean shouldSucceed

  def "Example test for documenting the ip endpoint"() {

    when:
      ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get('/api/example/ip'))

    then:
      result
          .andExpect(status().isOk())
          .andDo(
          document('example-ip-get',
              preprocessResponse(prettyPrint()),
              responseFields(
                  fieldWithPath("ip").type(JsonFieldType.STRING).
                      description("The ip of this service as seen from the Internet"),
              )))
  }

  def "Example test for documenting the sometimes endpoint"() {

    given:
      def url = '/api/example/sometimes'

    when:
      shouldSucceed = false
      ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(url))

    then:
      result
          .andExpect(status().is5xxServerError())
          .andDo(
          document('example-sometimes-fail-get',
              preprocessResponse(prettyPrint()),
              responseFields(
                  fieldWithPath("errorMessage").type(JsonFieldType.STRING).
                      description("The error message describing the error that occurred"),
              )))

    when:
      shouldSucceed = true
      result = mockMvc.perform(RestDocumentationRequestBuilders.get(url))

    then:
      result
          .andExpect(status().isOk())
          .andDo(
          document('example-sometimes-success-get',
              preprocessResponse(prettyPrint()),
              responseFields(
                  fieldWithPath("result").type(JsonFieldType.STRING).
                      description("The result of a successful operation"),
              )))
  }

  @Override
  protected List<Object> getControllersUnderTest() {

    controller = new ExampleController(restTemplate, auroraMetrics) {
      @Override
      protected boolean performOperationThatMayFail() {
        return shouldSucceed
      }
    }
    return [controller]
  }
}
