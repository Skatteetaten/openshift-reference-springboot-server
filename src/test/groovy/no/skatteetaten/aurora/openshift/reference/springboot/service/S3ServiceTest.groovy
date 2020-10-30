package no.skatteetaten.aurora.openshift.reference.springboot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.test.context.ContextConfiguration

import io.findify.s3mock.S3Mock
import no.skatteetaten.aurora.openshift.reference.springboot.ApplicationConfig
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import spock.lang.Specification

@RestClientTest
@ContextConfiguration(classes = [ApplicationConfig, S3Service])
class S3ServiceTest extends Specification {
  @Autowired
  S3Client s3Client

  @Autowired
  S3Service s3Service

  def s3Mock = new S3Mock.Builder().withInMemoryBackend().withPort(9000).build()

  def "Verify is able to store and retrieve object from minio"() {
    given:
      s3Mock.start()
      s3Client.createBucket(CreateBucketRequest.builder().bucket("default").build())
      def expectedFileContent = "my awesome test file"

    when:
      s3Service.putFileContent("myFile.txt", expectedFileContent)
      def fileContent = s3Service.getFileContent("myFile.txt")

    then:
      noExceptionThrown()
      fileContent == expectedFileContent
  }

}
