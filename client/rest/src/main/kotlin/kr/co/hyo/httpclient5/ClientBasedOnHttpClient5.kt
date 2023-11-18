package kr.co.hyo.httpclient5

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse

interface ClientBasedOnHttpClient5 {
    fun get(uri: String): SimpleHttpResponse
}
