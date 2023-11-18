package kr.co.hyo.resttemplate

interface ClientBasedOnRestTemplate {
    fun <T> get(url: String, clazz: Class<T>): T?
}
