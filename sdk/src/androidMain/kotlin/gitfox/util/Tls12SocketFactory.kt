package gitfox.util

import android.os.Build
import com.github.aakira.napier.Napier
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.net.InetAddress
import java.net.Socket
import java.security.KeyStore
import javax.net.ssl.*

// https://github.com/square/okhttp/issues/2372#issuecomment-244807676
internal class Tls12SocketFactory(private val delegate: SSLSocketFactory) : SSLSocketFactory() {
    override fun getDefaultCipherSuites(): Array<String> = delegate.defaultCipherSuites
    override fun getSupportedCipherSuites(): Array<String> = delegate.supportedCipherSuites

    override fun createSocket(
        s: Socket,
        host: String,
        port: Int,
        autoClose: Boolean
    ): Socket? =
        delegate
            .createSocket(s, host, port, autoClose)
            .patchForTls12()

    override fun createSocket(
        host: String,
        port: Int
    ): Socket? =
        delegate
            .createSocket(host, port)
            .patchForTls12()

    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket? =
        delegate
            .createSocket(host, port, localHost, localPort)
            .patchForTls12()

    override fun createSocket(
        host: InetAddress,
        port: Int
    ): Socket? =
        delegate
            .createSocket(host, port)
            .patchForTls12()

    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket? =
        delegate
            .createSocket(address, port, localAddress, localPort)
            .patchForTls12()

    override fun createSocket(): Socket =
        super.createSocket().patchForTls12()

    companion object {
        private val TLS_V12_ONLY = arrayOf("TLSv1.2")
        private fun Socket.patchForTls12() = this.apply {
            if (this is SSLSocket) {
                enabledProtocols = TLS_V12_ONLY
            }
        }

        private val trustManager: X509TrustManager by lazy {
            val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            factory.init(null as KeyStore?)
            factory.trustManagers.first { it is X509TrustManager } as X509TrustManager
        }

        private val tlsSocketFactory: Tls12SocketFactory
            get() {
                val sslContext = SSLContext.getInstance("TLSv1.2").apply {
                    init(null, arrayOf(trustManager), null)
                }
                return Tls12SocketFactory(sslContext.socketFactory)
            }

        private val connectionSpecs = listOf<ConnectionSpec>(
            ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build(),
            ConnectionSpec.COMPATIBLE_TLS,
            ConnectionSpec.CLEARTEXT
        )

        fun OkHttpClient.Builder.enableTls12() = apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                try {
                    sslSocketFactory(tlsSocketFactory, trustManager)
                    connectionSpecs(connectionSpecs)
                } catch (e: Exception) {
                    Napier.e("Error while setting TLS 1.2 compatibility", e)
                }
            }
        }
    }
}
