package kr.co.hyo.httpclient5

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit.SECONDS

class ClientBasedOnHttpClient5Impl(
    private val closeableHttpAsyncClient: CloseableHttpAsyncClient,
) : ClientBasedOnHttpClient5 {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())

    override fun get(uri: String): String {
        val responseBody: String = CompletableFuture
            .supplyAsync {
                val simpleHttpRequest: SimpleHttpRequest = SimpleRequestBuilder
                    .get()
                    .setUri(uri)
                    .build()

                closeableHttpAsyncClient.start()

                logger.info { "========== Request ==========" }
                logger.info { "Request: ${simpleHttpRequest.method} ${simpleHttpRequest.uri}" }
                val futureSimpleHttpResponse: Future<SimpleHttpResponse> =
                    closeableHttpAsyncClient.execute(simpleHttpRequest, null)
                logger.info { "========== Request ==========" }

                logger.info { "========== Response ==========" }
                val simpleHttpResponse: SimpleHttpResponse = futureSimpleHttpResponse.get(20, SECONDS)
                logger.info { "Response status: ${simpleHttpResponse.code}" }
                logger.info { "Response body: ${simpleHttpResponse.bodyText}" }
                logger.info { "========== Response ==========" }

//                closeableHttpAsyncClient.close()

                simpleHttpResponse.bodyText
            }
            .get()

        return responseBody
    }
}
