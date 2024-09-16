package service

import domain.*

class PaymentService(
        private val verifyAntiFraud: (PayerInfo) -> Boolean,
        private val processBankPayment: (CreditCard) -> Boolean,
        private val saveTransaction: (Transaction) -> Unit,
        private val findTransactionById: (String) -> Transaction?
) {
    fun processPurchase(payerInfo: PayerInfo): Transaction {
        val card = CreditCard.fromNumber(payerInfo.creditCard, payerInfo.cvv)
        val transaction = Transaction(generateId(), TransactionStatus.PENDING, payerInfo)

        println("Saving initial transaction with ID: ${transaction.id}")
        saveTransaction(transaction)
        println("Initial transaction saved.")

        val newStatus =
                when {
                    !verifyAntiFraud(payerInfo) -> TransactionStatus.REJECTED
                    processBankPayment(card) -> TransactionStatus.APPROVED
                    else -> TransactionStatus.REJECTED
                }

        val updatedTransaction = transaction.copy(status = newStatus)
        println(
                "Saving updated transaction with ID: ${updatedTransaction.id} and status: ${updatedTransaction.status}"
        )
        saveTransaction(updatedTransaction)
        println("Updated transaction saved.")

        return updatedTransaction
    }

    fun processRefund(id: String): Transaction? {
        val transaction = findTransactionById(id) ?: return null
        return if (transaction.status == TransactionStatus.APPROVED) {
            val refundedTransaction = transaction.copy(status = TransactionStatus.REFUNDED)
            saveTransaction(refundedTransaction)
            refundedTransaction
        } else {
            null
        }
    }

    private fun generateId() = "TX-${System.currentTimeMillis()}"
}
