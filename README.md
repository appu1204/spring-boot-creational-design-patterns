# Spring Boot Creational Design Patterns

A beginner-friendly, runnable collection of **Creational Design Patterns implemented with Spring Boot (Java 21)**, designed to show **why** each pattern exists, **how** they differ in real backend scenarios, and **what happens** when you compare naive code vs. pattern-based implementations.

> **Repository:** [github.com/appu1204/spring-boot-creational-design-patterns](https://github.com/appu1204/spring-boot-creational-design-patterns)

---

## Table of Contents

- [Repository Structure](#repository-structure)
- [Creational Design Patterns Comparison](#creational-design-patterns-comparison)
- [1. Singleton Pattern (`payment-singleton-demo`)](#1-singleton-pattern-payment-singleton-demo)
  - [How to Run](#how-to-run)
  - [API Endpoints & Expected Output](#api-endpoints--expected-output)
- [2. Factory Method Pattern (`payment-service`)](#2-factory-method-pattern-payment-service)
  - [How to Run](#how-to-run-1)
  - [API Endpoints & Expected Output](#api-endpoints--expected-output-1)
- [3. Abstract Factory Pattern (`payment-billing`)](#3-abstract-factory-pattern-payment-billing)
  - [How to Run](#how-to-run-2)
  - [API Endpoints & Expected Output](#api-endpoints--expected-output-2)
- [Handwritten & Study Notes](#handwritten--study-notes)
- [Contributing & Roadmap](#contributing--roadmap)

---

## Repository Structure

Each design pattern lives in its own standalone Spring Boot application folder so you can clone, run, and experiment with one pattern at a time:

```text
spring-boot-creational-design-patterns/
├── singleton-pattern/
│   └── payment-singleton-demo/     # Java 21 + Spring Boot 4.2.0 (Naive vs. DCL vs. Bill Pugh Singleton)
├── factory-method-pattern/
│   └── payment-service/            # Java 21 + Spring Boot 4.2.0 (Naive tight coupling vs. Factory + @Lazy)
├── abstract-factory-pattern/
│   └── payment-billing/            # Java 21 + Spring Boot 4.2.0 (Regional Billing Families: INDIA vs. USA)
├── notes/                          # (Coming soon) Handwritten & study notes
├── .gitignore
└── README.md
```

---

## Creational Design Patterns Comparison

Creational patterns abstract the **object creation process** so your Spring Boot services stay decoupled, performant, and easy to extend.

| Pattern | Core Intent | When to Use in Spring Boot | Key Difference | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Singleton** | Ensure a class has **only one shared instance** with a global access point | Expensive shared resources (e.g., connection clients, caches, config registries); also Spring's default `@Bean` scope | Focuses on **single instance reuse** across threads | [Implemented](./singleton-pattern/payment-singleton-demo) |
| **Factory Method** | Define an interface for creating an object, letting **subclasses/implementations decide** which class to instantiate | Selecting a single service implementation dynamically (e.g., Stripe vs. PayPal payment processor, Email vs. SMS notifier) | Creates **one product** via a factory method / Spring bean lookup | [Implemented](./factory-method-pattern/payment-service) |
| **Abstract Factory** | Create **families of related or dependent objects** without specifying concrete classes | Multi-provider or multi-region setups (e.g., India GST + Razorpay + e-Invoice vs. US Sales Tax + Stripe + US Receipt) | Creates **a suite of related products** that must work together | [Implemented](./abstract-factory-pattern/payment-billing) |
| **Builder** | Construct **complex objects step by step**, separating construction from representation | Requests/DTOs/configs with many optional fields (e.g., complex order, report, or HTTP client configuration) | Focuses on **step-by-step assembly** of one complex object | Planned |
| **Prototype** | Create new objects by **cloning an existing instance** rather than constructing from scratch | Expensive initialization where new independent copies are needed per request (e.g., document templates, rule snapshots) | Creates **copies of a pre-built object** (`@Scope("prototype")` or `.clone()`) | Planned |

---

## 1. Singleton Pattern (`payment-singleton-demo`)

Located in [`singleton-pattern/payment-singleton-demo`](./singleton-pattern/payment-singleton-demo), this Spring Boot application simulates a **heavy bank payment connection** (`2000 ms` startup cost per object) and compares three approaches:

1. **Naive (`NaiveBankConnection`)** — Creates a brand-new connection object on every request (`new NaiveBankConnection()`), causing repeated `~2000 ms` delays and extra heap allocations.
2. **Double-Checked Locking (`DclBankConnection`)** — Thread-safe lazy initialization using `volatile` + `synchronized`. Only the **first** request pays the `~2000 ms` initialization cost; all subsequent requests reuse the same instance in `~0 ms`.
3. **Bill Pugh Singleton (`BillPughBankConnection`)** — Thread-safe lazy initialization using a `private static class ConnectionHolder`. Relies on JVM class-loading guarantees with zero `synchronized` locking overhead.

### Prerequisites

- **Java 21**
- **Maven** (or use the included Maven Wrapper `./mvnw` / `mvnw.cmd`)

### How to Run

From the repository root:

```bash
cd singleton-pattern/payment-singleton-demo
```

**On Linux / macOS:**
```bash
./mvnw spring-boot:run
```

**On Windows (PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`.

### API Endpoints & Expected Output

Base path: `/api/payments`

#### 1. Without Singleton (Naive)
Creates a new `NaiveBankConnection` on every call.

```bash
curl -X POST "http://localhost:8080/api/payments/without-singleton?amount=250.0"
```

**Example Response (every request takes `~2000 ms` and increments `totalObjCreatedAtHeap`):**
```json
{
  "status": "SUCCESS",
  "instanceId": "CONN-NAIVE-#1-@3a2b4c5d",
  "durationMs": 2003,
  "totalObjCreatedAtHeap": 1,
  "note": "Slow! Every request connect the bank from scratch."
}
```

#### 2. With Double-Checked Locking (DCL Singleton)
```bash
curl -X POST "http://localhost:8080/api/payments/with-dcl?amount=250.0"
```

**Example Response (1st request `~2000 ms`, subsequent requests `~0 ms` reusing `#1`):**
```json
{
  "status": "SUCCESS",
  "instanceId": "CONN-DCL-#1-@6f7e8d9c",
  "durationMs": 0,
  "totalObjCreatedAtHeap": 1,
  "note": "Fast! Reused open connection."
}
```

#### 3. With Bill Pugh Singleton
```bash
curl -X POST "http://localhost:8080/api/payments/with-billpugh?amount=250.0"
```

**Example Response (1st request `~2000 ms`, subsequent requests `~0 ms` reusing `#1`):**
```json
{
  "status": "SUCCESS",
  "instanceId": "CONN-BILLPUGH-#1-@1b2c3d4e",
  "durationMs": 0,
  "totalObjCreatedAtHeap": 1,
  "note": "Fast! Reused open connection."
}
```

#### 4. Simulate Concurrent Load
Fires `count` concurrent threads (`ExecutorService` + `CountDownLatch`) to show how many objects get created under load.

Supported `mode` query values: `naive`, `singleton` (default, uses DCL), or `billpugh`.

```bash
# Test naive under 10 concurrent requests
curl -X POST "http://localhost:8080/api/payments/simulate-load?mode=naive&count=10"

# Test Bill Pugh Singleton under 10 concurrent requests
curl -X POST "http://localhost:8080/api/payments/simulate-load?mode=billpugh&count=10"
```

**Example Response (`mode=billpugh&count=10`):**
```json
{
  "modeTested": "billpugh",
  "totalRequestsSent": 10,
  "uniqueObjectsCreated": 1,
  "totalTimeTakenMs": 2004,
  "instancesList": [
    "CONN-BILLPUGH-#1-@1b2c3d4e"
  ],
  "summary": "EXCELLENT: All requests shared exactly 1 instance!"
}
```

---

## 2. Factory Method Pattern (`payment-service`)

Located in [`factory-method-pattern/payment-service`](./factory-method-pattern/payment-service), this Spring Boot application demonstrates why **decoupling object creation with a Factory** matters when multiple payment gateways exist (`STRIPE`, `PAYPAL`, `CRYPTO`):

1. **Naive Controller (`NaivePaymentController`)** — Uses `if/else` and **eagerly instantiates** `new StripeProcessor()`, `new PayPalProcessor()`, and `new CryptoProcessor()` on every request. Because `CryptoProcessor` simulates an offline blockchain node (`RuntimeException` in its constructor), **even `STRIPE` and `PAYPAL` requests fail!**
2. **Factory Pattern (`PaymentProcessorFactory` + `@Lazy`)** — Defines a common `PaymentProcessor` interface implemented by `@Component("STRIPE")`, `@Component("PAYPAL")`, and `@Component("CRYPTO") @Lazy`. `PaymentProcessorFactory` resolves only the requested bean from Spring's `ApplicationContext`, isolating failures so `STRIPE` and `PAYPAL` work normally even when `CRYPTO` is offline.

### How to Run

From the repository root:

```bash
cd factory-method-pattern/payment-service
```

**On Linux / macOS:**
```bash
./mvnw spring-boot:run
```

**On Windows (PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`. *(Stop any other demo first if it is already using port `8080`.)*

### API Endpoints & Expected Output

#### 1. Naive Endpoint (Tight Coupling & Eager Instantiation)
**Endpoint:** `POST /api/payments/naive/pay-now`

```bash
curl -X POST "http://localhost:8080/api/payments/naive/pay-now" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-101",
    "amount": 150.0,
    "currency": "USD",
    "paymentMethod": "STRIPE",
    "customerEmail": "buyer@example.com"
  }'
```

**Expected Result:** Fails with HTTP `500 Internal Server Error` (`CRITICAL: Blockchain node connection refused! Crypto gateway offline.`) because `NaivePaymentController` eagerly calls `new CryptoProcessor()` even though the user selected `STRIPE`.

#### 2. Factory Endpoint (Decoupled & Fault-Isolated)
**Endpoint:** `POST /api/payments/pay-now`

**Example A — `STRIPE` or `PAYPAL` succeeds (`200 OK`):**
```bash
curl -X POST "http://localhost:8080/api/payments/pay-now" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-101",
    "amount": 150.0,
    "currency": "USD",
    "paymentMethod": "STRIPE",
    "customerEmail": "buyer@example.com"
  }'
```

```json
{
  "status": "SUCCESS",
  "transactionId": "ch_stripe_a1b2c3d4e5",
  "amount": 150.0,
  "currency": "USD",
  "paymentMethod": "STRIPE",
  "message": "Card charged successfully via Stripe Gateway",
  "timestamp": "2026-11-25T12:00:00.000Z"
}
```

**Example B — `CRYPTO` or unsupported method returns a clean `400 Bad Request`:**
```bash
curl -X POST "http://localhost:8080/api/payments/pay-now" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-102",
    "amount": 150.0,
    "currency": "USD",
    "paymentMethod": "CRYPTO",
    "customerEmail": "buyer@example.com"
  }'
```

```text
Payment Error: Payment method 'CRYPTO' is unavailable or failed: ...
```

---

## 3. Abstract Factory Pattern (`payment-billing`)

Located in [`abstract-factory-pattern/payment-billing`](./abstract-factory-pattern/payment-billing), this Spring Boot application shows how **Abstract Factory creates matching families of related objects** (`TaxCalculator`, `PaymentGateway`, `InvoiceGenerator`) per `Region` (`INDIA` or `USA`):

- **`IndiaBillingFactory` (`@Component("INDIA")`)**: Produces `IndiaGstCalculator` (18% GST: 9% CGST + 9% SGST) + `RazorpayPaymentGateway` (`INR`) + `IndiaGstInvoiceGenerator` (`INV-IN-...`, Govt e-Invoice GSTIN/IRN).
- **`UsaBillingFactory` (`@Component("USA")`)**: Produces `UsSalesTaxCalculator` (8.25% California Sales Tax) + `StripePaymentGateway` (`USD`) + `UsReceiptGenerator` (`REC-US-...`, US IRS commercial receipt).
- **`BillingFactoryProvider`**: Injects `Map<String, RegionalBillingFactory>` from Spring and resolves the entire regional family cleanly by `region.name()`.
- **Anti-Pattern Comparison (`processWithoutFactory`)**: Directly instantiates `new RazorpayPaymentGateway()` (`INR`) alongside `new UsSalesTaxCalculator()` and `new UsReceiptGenerator()`, illustrating the real bug Abstract Factory prevents—mixing incompatible components from different regions.

### How to Run

From the repository root:

```bash
cd abstract-factory-pattern/payment-billing
```

**On Linux / macOS:**
```bash
./mvnw spring-boot:run
```

**On Windows (PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`. *(Stop any other demo first if it is already using port `8080`.)*

### API Endpoints & Expected Output

Base path: `/api/billing`

#### 1. Clean Abstract Factory Endpoint
**Endpoint:** `POST /api/billing/pay-now`

**Example A — `INDIA` Region (`200 OK`):**
```bash
curl -X POST "http://localhost:8080/api/billing/pay-now" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-501",
    "customerId": "CUST-01",
    "amount": 1000.0,
    "region": "INDIA"
  }'
```

```json
{
  "orderId": "ORD-501",
  "region": "INDIA",
  "currency": "INR",
  "baseAmount": 1000.0,
  "taxAmount": 180.0,
  "taxBreakdown": "Indian GST 18% (CGST: ₹90.00, SGST: ₹90.00)",
  "totalAmountPaid": 1180.0,
  "paymentTransactionId": "pay_rzp_1732590000000",
  "paymentGateway": "Razorpay (India UPI/Netbanking/Cards)",
  "invoiceNumber": "INV-IN-ORD-501",
  "invoiceLegalDetails": "Govt e-Invoice Compliant | GSTIN: 27AABCT3518Q1ZV | IRN: ... | Signed QR: Generated",
  "complianceStatus": "100% COMPLIANT - ALL REGIONAL COMPONENTS MATCHED"
}
```

**Example B — `USA` Region (`200 OK`):**
```bash
curl -X POST "http://localhost:8080/api/billing/pay-now" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-502",
    "customerId": "CUST-02",
    "amount": 100.0,
    "region": "USA"
  }'
```

```json
{
  "orderId": "ORD-502",
  "region": "USA",
  "currency": "USD",
  "baseAmount": 100.0,
  "taxAmount": 8.25,
  "taxBreakdown": "California Sales Tax 8.25% ($8.25)",
  "totalAmountPaid": 108.25,
  "paymentTransactionId": "ch_stripe_1732590000000",
  "paymentGateway": "Stripe (US ACH/Credit Cards)",
  "invoiceNumber": "REC-US-ORD-502",
  "invoiceLegalDetails": "US IRS Standard Commercial Receipt | Merchant EIN: 12-3456789 | Sales Tax collected under CA Reg #9876543",
  "complianceStatus": "100% COMPLIANT - ALL REGIONAL COMPONENTS MATCHED"
}
```

#### 2. No-Pattern Endpoint (Mismatched Regional Components)
**Endpoint:** `POST /api/billing/noPattern/pay-now`

```bash
curl -X POST "http://localhost:8080/api/billing/noPattern/pay-now" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-503",
    "customerId": "CUST-03",
    "amount": 100.0,
    "region": "INDIA"
  }'
```

**Expected Output:** Charges via `Razorpay` (`INR`) while applying `California Sales Tax 8.25%` and issuing `REC-US-ORD-503`, returning `"complianceStatus": "NON-COMPLIANT: TAX & INVOICE REGION MISMATCH DETECTED!"`.

---

## Handwritten & Study Notes

Add your handwritten notes (PDFs, images, or Markdown summaries) inside a `notes/` folder and link them below:

| Topic | Notes Link | Key Takeaways |
| :--- | :--- | :--- |
| **Creational Patterns Overview** | `notes/creational-patterns-overview.pdf` *(add file)* | Why object creation needs abstraction in Spring Boot |
| **Singleton Pattern** | `notes/singleton-pattern.pdf` *(add file)* | Eager vs. Lazy, DCL (`volatile`), Bill Pugh Holder, Enum Singleton, Spring `@Scope("singleton")` |
| **Factory Method vs. Abstract Factory** | `notes/factory-patterns.pdf` *(add file)* | Single product vs. product family, Spring bean lookup (`ApplicationContext` / `Map<String, T>`) |
| **Builder & Prototype** | `notes/builder-prototype.pdf` *(add file)* | Fluent construction vs. cloning |

---

## Contributing & Roadmap

- [x] **Singleton Pattern** (`singleton-pattern/payment-singleton-demo`)
- [x] **Factory Method Pattern** (`factory-method-pattern/payment-service`)
- [x] **Abstract Factory Pattern** (`abstract-factory-pattern/payment-billing`)
- [ ] **Builder Pattern**
- [ ] **Prototype Pattern**
- [ ] **Handwritten & Study Notes**
