package kr.co.hyo.socket

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class TcpNonSslClientSocket(
    private val host: String,
    private val port: Int,
) {

    fun run(messageLength: Int) {
        try {
            val socket = Socket()
            val inetSocketAddress = InetSocketAddress(host, port)
            socket.connect(inetSocketAddress)

            val tcpClientSocket = TcpClientSocket(socket = socket)
            tcpClientSocket.sendFixedLength(messageLength = messageLength)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
}
