package domain

import kotlinx.serialization.Serializable

sealed class CreditCard {
    abstract val number: String
    abstract val cvv: String

    data class Visa(override val number: String, override val cvv: String) : CreditCard()
    data class MasterCard(override val number: String, override val cvv: String) : CreditCard()
    data class Amex(override val number: String, override val cvv: String) : CreditCard()

    companion object {
        fun fromNumber(number: String, cvv: String): CreditCard = when {
            number.startsWith("4") -> Visa(number, cvv)
            number.startsWith("5") -> MasterCard(number, cvv)
            number.startsWith("3") -> Amex(number, cvv)
            else -> throw IllegalArgumentException("Unsupported card type")
        }
    }
}

@Serializable
data class PayerInfo(
    val name: String,
    val creditCard: String,
    val expirationDate: String,
    val cvv: String,
    val amount: Double
)

@Serializable
data class Transaction(
    val id: String,
    val status: TransactionStatus,
    val details: PayerInfo
)

enum class TransactionStatus { APPROVED, REJECTED, PENDING, REFUNDED }
