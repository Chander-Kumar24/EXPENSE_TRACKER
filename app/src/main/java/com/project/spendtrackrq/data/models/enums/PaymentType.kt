package com.project.spendtrackrq.data.models.enums

enum class PaymentType {
    UPI,        // Unified Payments Interface
    NEFT,       // National Electronic Funds Transfer
    RTGS,       // Real-Time Gross Settlement
    IMPS,       // Immediate Payment Service
    CARD,       // Debit/Credit card payments
    WALLET,     // E-wallet transactions (Paytm, PhonePe, etc.)
    CASH,       // Cash deposits or withdrawals
    CHEQUE,     // Cheque payments
    AUTO_DEBIT, // Standing instructions / recurring payments
    OTHER       // Any other type not listed above
}
