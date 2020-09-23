package chat.rocket.reactnative.cschat;

import android.util.Log
import com.good.gd.net.GDSocket
import java.net.InetSocketAddress
import java.net.SocketAddress

class CSSocket : GDSocket(false) {
    var connectTimeout : Int = 0
    override fun connect(endpoint: SocketAddress?, timeout: Int) {
        Log.d("HTTPS [CSSocket connect]", endpoint.toString())
        val osa = endpoint as InetSocketAddress
        val sa = InetSocketAddress.createUnresolved(osa.hostName, osa.port)
        connectTimeout = timeout
        super.connect(sa, timeout)
    }

    override fun connect(endpoint: SocketAddress?) {
        Log.d("HTTPS [CSSocket connect]", endpoint.toString())
        val osa = endpoint as InetSocketAddress
        val sa = InetSocketAddress.createUnresolved(osa.hostName, osa.port)
        super.connect(sa)
    }
}
