package chat.rocket.reactnative.cschat;


import android.util.Log
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.net.InetAddress
import java.net.Socket
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory
import javax.net.ssl.*

@Suppress("ObjectLiteralToLambda")
class HttpClientBuilderFactory {
    fun create() : OkHttpClient.Builder {
        //dummy trust manager as certificate validation is done by GDSocket
        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }

        val socketFactory = object : SocketFactory() {
            override fun createSocket() = CSSocket()
            override fun createSocket(host: String?, port: Int) = throw UnsupportedOperationException()

            override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int) = throw UnsupportedOperationException()

            override fun createSocket(host: InetAddress?, port: Int) = throw UnsupportedOperationException()

            override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int) = throw UnsupportedOperationException()

        }

        val sslSocketFactory = object : SSLSocketFactory() {
            override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
//                check this function tooo
                Log.d("sslSocketFactory", "ln 45: going into sslSocketFactory")
                val soTimeout = s?.soTimeout
                val connectTimeout = if (s is CSSocket) s.connectTimeout else 1 * 1000
                Log.d("sslSocketFactory", "Connected: " + s?.isConnected.toString())

//                s?.close()
                return CSSSLSocket(host, port, connectTimeout).let {
                    Log.d("sslSocketFactory", "ln 50: going into return function")
                    it.soTimeout = soTimeout ?: 0
                    Log.d("sslSocketFactory", "SSLConnected: " + it.isConnected.toString())
                    return it
                }

            }

            override fun getDefaultCipherSuites() = throw UnsupportedOperationException()

            override fun createSocket(host: String?, port: Int) = throw UnsupportedOperationException()

            override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int) = throw UnsupportedOperationException()

            override fun createSocket(host: InetAddress?, port: Int) = throw UnsupportedOperationException()

            override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int) = throw UnsupportedOperationException()

            override fun getSupportedCipherSuites() = throw UnsupportedOperationException()
        }

        return OkHttpClient.Builder()
                .hostnameVerifier(object : HostnameVerifier{
                    override fun verify(hostname: String?, session: SSLSession?): Boolean {
                        Log.d("HTTPS [HttpClientBuilderFactory]", "verify")
                        return true
                    }
                })
                .dns(object : Dns {
                    override fun lookup(hostname: String): List<InetAddress> {
                        Log.d("HTTPS [HttpClientBuilderFactory]", "lookup")
                        return arrayListOf(InetAddress.getByAddress(hostname, byteArrayOf(0,0,0,0)))
                    }
                })
                .socketFactory(socketFactory)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
    }
}
