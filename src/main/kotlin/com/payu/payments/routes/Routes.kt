package routes

import domain.PayerInfo
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import service.PaymentService

fun Route.paymentRoutes(paymentService: PaymentService) {
    route("/payments") {
        post("/purchase") {
            println("Endpoint /payments/purchase alcanzado")
            val payerInfo = call.receive<PayerInfo>()
            println("PayerInfo: $payerInfo")
            val transaction = paymentService.processPurchase(payerInfo)
            call.respondText(Json.encodeToString(transaction) + getHateoasLinks(transaction.id))
        }

        post("/refund/{id}") {
            println("Endpoint /payments/refund alcanzado")
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val transaction =
                    paymentService.processRefund(id)
                            ?: return@post call.respond(
                                    HttpStatusCode.NotFound,
                                    "Transaction not found"
                            )
            call.respondText(Json.encodeToString(transaction) + getHateoasLinks(transaction.id))
        }
    }
}

private fun getHateoasLinks(transactionId: String): String {
    return """
        ,
        "links": [
            { "rel": "self", "href": "/payments/$transactionId" },
            { "rel": "refund", "href": "/payments/refund/$transactionId" }
        ]
    """.trimIndent()
}
