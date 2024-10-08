package io

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import domain.PayerInfo
import domain.Transaction
import domain.TransactionStatus
import io.github.cdimascio.dotenv.dotenv
import java.sql.DriverManager
import java.time.LocalDateTime
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

val dotenv by lazy { dotenv() }

fun testDatabaseConnection() {
    val url = dotenv["URL"]
    val user = dotenv["DB_USER"]
    val password = dotenv["DB_PASSWORD"]

    try {
        Class.forName("org.postgresql.Driver")
        val connection = DriverManager.getConnection(url, user, password)
        println("Connection successful!")
        connection.close()
    } catch (e: Exception) {
        println("Connection failed!")
        e.printStackTrace()
    }
}

fun migrateDatabase() {
    val url = dotenv["URL"]
    val user = dotenv["DB_USER"]
    val password = dotenv["DB_PASSWORD"]

    val flyway =
            Flyway.configure()
                    .dataSource(url, user, password)
                    .driver(dotenv["DRIVER"])
                    .locations("classpath:migration")
                    .loggers("slf4j")
                    .load()

    flyway.migrate()
}

fun connectToDatabase() {
    val hikariConfig =
            HikariConfig().apply {
                jdbcUrl = dotenv["URL"]
                driverClassName = dotenv["DRIVER"]
                username = dotenv["DB_USER"]
                password = dotenv["DB_PASSWORD"]
                maximumPoolSize = 10
            }
    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)
}

object TransactionsTable : Table("transactions") {
    val id = varchar("id", 50).uniqueIndex()
    val status = varchar("status", 20)
    val payerName = varchar("payer_name", 255)
    val creditCard = varchar("credit_card", 16)
    val expirationDate = varchar("expiration_date", 5)
    val cvv = varchar("cvv", 3)
    val amount = double("amount")
    val createdAt = varchar("created_at", 20)
}

fun saveTransaction(transaction: Transaction) {
    println("Enter to Transaction with ID ${transaction.id} saving...")
    transaction {
        TransactionsTable.insert {
            it[id] = transaction.id
            it[status] = transaction.status.name
            it[payerName] = transaction.details.name
            it[creditCard] = transaction.details.creditCard
            it[expirationDate] = transaction.details.expirationDate
            it[cvv] = transaction.details.cvv
            it[amount] = transaction.details.amount
            it[createdAt] = LocalDateTime.now().toString()
        }
    }
    println("Transaction with ID ${transaction.id} saved successfully.")
}

fun findTransactionById(id: String): Transaction? {
    return transaction {
        // Usar la expresión booleana correcta para filtrar por el ID
        TransactionsTable.select(TransactionsTable.id)
                .where { TransactionsTable.id eq id }
                .map {
                    Transaction(
                            id = it[TransactionsTable.id],
                            status = TransactionStatus.valueOf(it[TransactionsTable.status]),
                            details =
                                    PayerInfo(
                                            name = it[TransactionsTable.payerName],
                                            creditCard = it[TransactionsTable.creditCard],
                                            expirationDate = it[TransactionsTable.expirationDate],
                                            cvv = it[TransactionsTable.cvv],
                                            amount = it[TransactionsTable.amount]
                                    )
                    )
                }
                .singleOrNull() // Devuelve nulo si no encuentra la transacción
    }
}
