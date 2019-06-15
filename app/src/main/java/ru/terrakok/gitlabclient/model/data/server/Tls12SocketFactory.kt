package ru.terrakok.gitlabclient.model.data.server

import android.os.Build
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import javax.net.ssl.*
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import timber.log.Timber

/**
 * Implementation of [SSLSocketFactory] that adds [TlsVersion.TLS_1_2] as an enabled protocol for every [SSLSocket]
 * created by [delegate].
 *
 * [See this discussion for more details.](https://github.com/square/okhttp/issues/2372#issuecomment-244807676)
 *
 * @see SSLSocket
 * @see SSLSocketFactory
 */
class Tls12SocketFactory(private val delegate: SSLSocketFactory) : SSLSocketFactory() {
    companion object {
        /**
         * @return [X509TrustManager] from [TrustManagerFactory]
         *
         * @throws [NoSuchElementException] if not found. According to the Android docs for [TrustManagerFactory], this
         * should never happen because PKIX is the only supported algorithm
         */
        private val trustManager by lazy {
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                .apply { init(null as KeyStore?) }
                .trustManagers
                .first { it is X509TrustManager } as X509TrustManager
        }

        /**
         * If on [Build.VERSION_CODES.LOLLIPOP] or lower, sets [OkHttpClient.Builder.sslSocketFactory] to an instance of
         * [Tls12SocketFactory] that wraps the default [SSLContext.getSocketFactory] for [TlsVersion.TLS_1_2].
         *
         * Does nothing when called on [Build.VERSION_CODES.LOLLIPOP_MR1] or higher.
         *
         * For some reason, Android supports TLS v1.2 from [Build.VERSION_CODES.JELLY_BEAN], but the spec only has it
         * enabled by default from API [Build.VERSION_CODES.KITKAT]. Furthermore, some devices on
         * [Build.VERSION_CODES.LOLLIPOP] don't have it enabled, despite the spec saying they should.
         *
         * @return the (potentially modified) [OkHttpClient.Builder]
         */
        @JvmStatic
        fun OkHttpClient.Builder.enableTls12() = apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                try {
                    val tlsSocketFactory = SSLContext.getInstance(TlsVersion.TLS_1_2.javaName())
                        .apply { init(null, arrayOf(trustManager), null) }
                        .socketFactory
                        .let(::Tls12SocketFactory)

                    sslSocketFactory(tlsSocketFactory, trustManager)

                    val tls12ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                    connectionSpecs(
                        listOf(
                            tls12ConnectionSpec,
                            ConnectionSpec.COMPATIBLE_TLS,
                            ConnectionSpec.CLEARTEXT
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Error while setting TLS 1.2 compatibility")
                }
            }
        }
    }

    /**
     * Forcefully adds [TlsVersion.TLS_1_2] as an enabled protocol if called on an [SSLSocket]
     *
     * @return the (potentially modified) [Socket]
     */
    private fun Socket.patchForTls12(): Socket {
        if (this is SSLSocket) {
            enabledProtocols = arrayOf(TlsVersion.TLS_1_2.javaName())
        }
        return this
    }

    override fun getDefaultCipherSuites(): Array<String> = delegate.defaultCipherSuites

    override fun getSupportedCipherSuites(): Array<String> = delegate.supportedCipherSuites

    @Throws(IOException::class)
    override fun createSocket(
        s: Socket,
        host: String,
        port: Int,
        autoClose: Boolean
    ): Socket? = delegate
        .createSocket(s, host, port, autoClose)
        .patchForTls12()

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(
        host: String,
        port: Int
    ): Socket? = delegate
        .createSocket(host, port)
        .patchForTls12()

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket? = delegate
        .createSocket(host, port, localHost, localPort)
        .patchForTls12()

    @Throws(IOException::class)
    override fun createSocket(
        host: InetAddress,
        port: Int
    ): Socket? = delegate
        .createSocket(host, port)
        .patchForTls12()

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket? = delegate
        .createSocket(address, port, localAddress, localPort)
        .patchForTls12()
}