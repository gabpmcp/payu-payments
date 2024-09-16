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
import io.migrateDatabase
import io.saveTransaction
import kotlin.io.println
import routes.paymentRoutes
import service.PaymentService

fun main() {
    println("Starting application...")

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
                try {
                    // Migración de la base de datos
                    migrateDatabase()

                    // Ejecutar la función de conexión a la base de datos
                    connectToDatabase()

                    println("Inside embeddedServer")

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
                        println("Rutas configuradas correctamente")
                    }
                    println("Server started")
                } catch (e: Exception) {
                    println("Error during database migration: ${e.message}")
                    e.printStackTrace()
                }
            }
            .start(wait = true)

    println("Server started")
}
