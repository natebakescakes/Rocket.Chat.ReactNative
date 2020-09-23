package chat.rocket.reactnative.cschat;

import android.util.Log
import com.good.gd.net.GDSocket
import okhttp3.CipherSuite
import okhttp3.CipherSuite.Companion
import okhttp3.TlsVersion
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.security.Principal
import java.security.cert.Certificate
import javax.net.ssl.HandshakeCompletedListener
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSessionContext
import javax.net.ssl.SSLSocket
import javax.security.cert.X509Certificate

class CSSSLSocket(private val host: String?, port: Int, timeout: Int = 60*1000) : SSLSocket() {
    private var realSocket : GDSocket = GDSocket(true)

    init {
        realSocket.disableHostVerification()
        realSocket.disablePeerVerification()
        realSocket.connect(InetSocketAddress.createUnresolved(host, port), timeout)
        Log.d("HTTPS [realSocket Connection] Host", host.toString())
    }

    //SSL Socket methods -- fake
    override fun setEnableSessionCreation(flag: Boolean) {
    }

    override fun getNeedClientAuth() = false

    override fun getEnabledCipherSuites() = arrayOf(Companion.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256.javaName)

    override fun addHandshakeCompletedListener(listener: HandshakeCompletedListener?) {
    }

    override fun setNeedClientAuth(need: Boolean) {
    }

    override fun getSupportedCipherSuites() = emptyArray<String>()

    override fun setWantClientAuth(want: Boolean) {
    }

    override fun getSupportedProtocols() = emptyArray<String>()

    override fun startHandshake() {
    }

    override fun getSession(): SSLSession {
        return object : SSLSession {
            override fun getLastAccessedTime(): Long {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun removeValue(name: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getSessionContext(): SSLSessionContext {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getValueNames(): Array<String> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getProtocol() = enabledProtocols[0]

            override fun getId(): ByteArray {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun putValue(name: String?, value: Any?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getPeerCertificateChain(): Array<X509Certificate> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getPeerPort(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getApplicationBufferSize(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getPeerPrincipal(): Principal {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getCipherSuite() = getEnabledCipherSuites()[0]

            override fun getPacketBufferSize(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getLocalCertificates() = emptyArray<Certificate>()

            override fun invalidate() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getCreationTime(): Long {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getLocalPrincipal(): Principal {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getPeerCertificates() = emptyArray<Certificate>()

            override fun getValue(name: String?): Any {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isValid(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getPeerHost() = host

        }
    }

    override fun setUseClientMode(mode: Boolean) {
    }

    override fun setEnabledProtocols(protocols: Array<out String>?) {
    }

    override fun setEnabledCipherSuites(suites: Array<out String>?) {
    }

    override fun removeHandshakeCompletedListener(listener: HandshakeCompletedListener?) {
    }

    override fun getUseClientMode() = false

    override fun getEnableSessionCreation() = false

    override fun getEnabledProtocols() = arrayOf(TlsVersion.TLS_1_2.javaName)

    override fun getWantClientAuth() = false

    //Socket
    override fun setReuseAddress(on: Boolean) {
        realSocket.setReuseAddress(on)
    }

    override fun isInputShutdown(): Boolean {
        return realSocket.isInputShutdown()
    }

    override fun connect(endpoint: SocketAddress?) {
        realSocket.connect(endpoint)
    }

    override fun connect(endpoint: SocketAddress?, timeout: Int) {
        realSocket.connect(endpoint, timeout)
    }

    override fun setKeepAlive(on: Boolean) {
        realSocket.setKeepAlive(on)
    }

    override fun getPort(): Int {
        return realSocket.getPort()
    }

    override fun setSoLinger(on: Boolean, linger: Int) {
        realSocket.setSoLinger(on, linger)
    }

    override fun getSoLinger(): Int {
        return realSocket.getSoLinger()
    }

    override fun setTrafficClass(tc: Int) {
        realSocket.setTrafficClass(tc)
    }

    override fun shutdownInput() {
        realSocket.shutdownInput()
    }

    override fun close() {
        realSocket.close()
    }

    override fun isClosed(): Boolean {
        return realSocket.isClosed()
    }

    override fun setSoTimeout(timeout: Int) {
        realSocket.setSoTimeout(timeout)
    }

    override fun getSendBufferSize(): Int {
        return realSocket.getSendBufferSize()
    }

    override fun isBound(): Boolean {
        return realSocket.isBound()
    }

    override fun getLocalSocketAddress(): SocketAddress {
        return realSocket.getLocalSocketAddress()
    }

    override fun getReceiveBufferSize(): Int {
        return realSocket.getReceiveBufferSize()
    }

    override fun sendUrgentData(data: Int) {
        realSocket.sendUrgentData(data)
    }

    override fun getKeepAlive(): Boolean {
        return realSocket.getKeepAlive()
    }

    override fun getTcpNoDelay(): Boolean {
        return realSocket.getTcpNoDelay()
    }

    override fun getLocalPort(): Int {
        return realSocket.getLocalPort()
    }

    override fun setSendBufferSize(size: Int) {
        realSocket.setSendBufferSize(size)
    }

    override fun getRemoteSocketAddress(): SocketAddress {
        return realSocket.getRemoteSocketAddress()
    }

    override fun setReceiveBufferSize(size: Int) {
        realSocket.setReceiveBufferSize(size)
    }

    override fun getTrafficClass(): Int {
        return realSocket.getTrafficClass()
    }

    override fun getSoTimeout(): Int {
        return realSocket.getSoTimeout()
    }

    override fun isConnected(): Boolean {
        return realSocket.isConnected()
    }

    override fun setOOBInline(on: Boolean) {
        realSocket.setOOBInline(on)
    }

    override fun setTcpNoDelay(on: Boolean) {
        realSocket.setTcpNoDelay(on)
    }

    override fun getOOBInline(): Boolean {
        return realSocket.getOOBInline()
    }

    override fun getInputStream(): InputStream {
        return realSocket.getInputStream()
    }

    override fun getReuseAddress(): Boolean {
        return realSocket.getReuseAddress()
    }

    override fun getLocalAddress(): InetAddress {
        return realSocket.getLocalAddress()
    }

    override fun isOutputShutdown(): Boolean {
        return realSocket.isOutputShutdown()
    }

    override fun getChannel(): SocketChannel {
        return realSocket.getChannel()
    }

    override fun setPerformancePreferences(connectionTime: Int, latency: Int, bandwidth: Int) {
        realSocket.setPerformancePreferences(connectionTime, latency, bandwidth)
    }

    override fun getInetAddress(): InetAddress {
        return realSocket.getInetAddress()
    }

    override fun bind(bindpoint: SocketAddress?) {
        realSocket.bind(bindpoint)
    }

    override fun getOutputStream(): OutputStream {
        return realSocket.getOutputStream()
    }

    override fun shutdownOutput() {
        realSocket.shutdownOutput()
    }
}
