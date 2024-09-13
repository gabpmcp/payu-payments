import kotlin.test.Test
import kotlin.test.assertEquals
import domain.*
import service.PaymentService

class PaymentServiceTest {

    @Test
    fun `processPurchase should approve transaction when all validations pass`() {
        // Mock de funciones para las dependencias
        val mockVerifyAntiFraud: (PayerInfo) -> Boolean = { true } // Simula que pasa la validación antifraude
        val mockProcessBankPayment: (CreditCard) -> Boolean = { true } // Simula que el banco aprueba el pago
        val mockSaveTransaction: (Transaction) -> Unit = { } // No hace nada, solo cumple con la firma
        val mockFindTransactionById: (String) -> Transaction? = { null } // No es relevante para esta prueba

        // Crear una instancia de PaymentService con las dependencias simuladas
        val paymentService = PaymentService(
            verifyAntiFraud = mockVerifyAntiFraud,
            processBankPayment = mockProcessBankPayment,
            saveTransaction = mockSaveTransaction,
            findTransactionById = mockFindTransactionById
        )

        // Datos de prueba
        val payerInfo = PayerInfo("John Doe", "4111111111111111", "12/25", "123", 4000.0)

        // Ejecutar la función que se va a probar
        val transaction = paymentService.processPurchase(payerInfo)

        // Verificar los resultados
        assertEquals(TransactionStatus.APPROVED, transaction.status)
    }

    @Test
    fun `processPurchase should reject transaction when antifraud fails`() {
        // Simula que la validación antifraude falla
        val mockVerifyAntiFraud: (PayerInfo) -> Boolean = { false }
        val mockProcessBankPayment: (CreditCard) -> Boolean = { true } // No es relevante ya que fallará antes
        val mockSaveTransaction: (Transaction) -> Unit = { }
        val mockFindTransactionById: (String) -> Transaction? = { null }

        val paymentService = PaymentService(
            verifyAntiFraud = mockVerifyAntiFraud,
            processBankPayment = mockProcessBankPayment,
            saveTransaction = mockSaveTransaction,
            findTransactionById = mockFindTransactionById
        )

        val payerInfo = PayerInfo("John Doe", "4111111111111111", "12/25", "123", 4000.0)

        // Ejecutar la función que se va a probar
        val transaction = paymentService.processPurchase(payerInfo)

        // Verificar que la transacción fue rechazada
        assertEquals(TransactionStatus.REJECTED, transaction.status)
    }
}
