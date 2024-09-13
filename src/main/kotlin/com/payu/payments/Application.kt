package com.payu.payments

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import routes.paymentRoutes
import service.PaymentService
import domain.*

fun main() {
    embeddedServer(Netty, port = 8080) {
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

        val saveTransaction: (Transaction) -> Unit = { transaction ->
            // Implementación real de almacenamiento (podría ser persistente)
            println("Saving transaction: $transaction") 
        }

        val findTransactionById: (String) -> Transaction? = { id ->
            // Implementación real de búsqueda de transacciones
            println("Looking for transaction by id: $id")
            null // Conectar potencial BD
        }

        // Inyectar las funciones productivas en PaymentService
        val paymentService = PaymentService(
            verifyAntiFraud = verifyAntiFraud,
            processBankPayment = processBankPayment,
            saveTransaction = saveTransaction,
            findTransactionById = findTransactionById
        )

        routing {
            paymentRoutes(paymentService) // Inyectamos el PaymentService en las rutas
        }
    }.start(wait = true)
}
