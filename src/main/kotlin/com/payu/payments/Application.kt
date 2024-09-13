package payments

import domain.*
import io.connectToDatabase
import io.findTransactionById
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.saveTransaction
import routes.paymentRoutes
import service.PaymentService

fun main() {
    embeddedServer(Netty, port = 8080) {
                // Ejecutar la función de conexión a la base de datos
                connectToDatabase()

                // Configuración de Ktor
                install(ContentNegotiation) {
                    json() // Configura el soporte para JSON
                }

                // Funciones productivas que inyectaremos en PaymentService
                val verifyAntiFraud: (PayerInfo) -> Boolean = { payerInfo ->
                    payerInfo.amount < 5000 // Implementación real del servicio antifraude
                }

                val processBankPayment: (CreditCard) -> Boolean = { card ->
                    card.cvv == "123" // Implementación real del servicio bancario
                }

                val saveTransaction: (Transaction) -> Unit = ::saveTransaction

                val findTransactionById: (String) -> Transaction? = ::findTransactionById

                // Inyectar las funciones productivas en PaymentService
                val paymentService =
                        PaymentService(
                                verifyAntiFraud = verifyAntiFraud,
                                processBankPayment = processBankPayment,
                                saveTransaction = saveTransaction,
                                findTransactionById = findTransactionById
                        )

                routing {
                    paymentRoutes(paymentService) // Inyectamos el PaymentService en las rutas
                }
            }
            .start(wait = true)
}
