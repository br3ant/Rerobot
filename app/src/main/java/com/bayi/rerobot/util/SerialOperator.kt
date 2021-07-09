package com.bayi.rerobot.util


import com.google.gson.Gson

object SerialOperator {

    private var data: String = ""

    fun handlerData(bytes: ByteArray, size: Int): SerialData? {

        try {
            val readBytes = bytes.copyOf(size)
            val hexString = BytesArrayUtils.bytesToHexString(readBytes)
            if (hexString.startsWith("fe")) {
                data = hexString
            } else {
                data += hexString
            }
            if (data.endsWith("fe00")) {
                val dataBytes = BytesArrayUtils.hexStringToBytes(data)
                if (dataBytes.size > 6) {
                    val serialData = Gson().fromJson(String(dataBytes, 4, data.substring(2, 6).toInt(16)), SerialData::class.java)
                    data = ""
                    return serialData
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }


    class SerialData {
        val param1: Param = Param()

        class Param() {
            val keyword: String = ""
            val angle: String = "0"
        }
    }
}