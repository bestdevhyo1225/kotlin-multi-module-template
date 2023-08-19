package kr.co.hyo

import kr.co.hyo.socket.NettyTcpNonSslClientSocket
import java.util.Scanner

class NettyTcpClientApplication

fun main() {
    val host = "127.0.0.1"
    val port = 9004
    try {
        print("enter message length: ")
        val sc = Scanner(System.`in`)
        val messageLength: Int = sc.nextLine().toInt()
        val nettyTcpNonSslClientSocket = NettyTcpNonSslClientSocket(host = host, port = port)
        nettyTcpNonSslClientSocket.run(messageLength = messageLength)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}
