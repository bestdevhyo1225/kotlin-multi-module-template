package kr.co.hyo.socket

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.Arrays

class TcpClientSocket(
    private val socket: Socket,
) {

    fun sendFixedLength(messageLength: Int) {
        val delimiterLength = 256
        val stringBuilder = StringBuilder()
        (0 until messageLength).forEach { _ -> stringBuilder.append("a") }
        val totalData: ByteArray = stringBuilder.toString().toByteArray()

        try {
            println("sending message")
            val outputStream: OutputStream = socket.getOutputStream()
            (0 until messageLength / delimiterLength).forEach {
                val from: Int = it * delimiterLength
                val to: Int = (it + 1) * delimiterLength
                val sendData: ByteArray = Arrays.copyOfRange(totalData, from, to)
                println("sending... ${it + 1} (from: $from, to: $to)")
                outputStream.write(sendData)
                outputStream.flush()
                Thread.sleep(500)
            }
        } catch (exception: InterruptedException) {
            exception.printStackTrace()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

        try {
            println("receiving message")
            val inputStream: InputStream = socket.getInputStream()
            val readData = inputStream.readBytes()
            println("receive message: ${String(readData)}")
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
}
