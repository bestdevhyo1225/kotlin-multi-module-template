package kr.co.hyo

import kr.co.hyo.socket.TcpNonSslClientSocket
import java.util.Scanner

class TcpClientApplication

fun main() {
    val host = "127.0.0.1"
    val port = 9004
    try {
        print("enter message length: ")
        val sc = Scanner(System.`in`)
        val messageLength: Int = sc.nextLine().toInt()
        val tcpNonSslClientSocket = TcpNonSslClientSocket(host = host, port = port)
        tcpNonSslClientSocket.run(messageLength = messageLength)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}
