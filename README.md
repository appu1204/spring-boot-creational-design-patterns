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
- [Handwritten & Study Notes](#handwritten--study-notes)
- [Contributing & Roadmap](#contributing--roadmap)

---

## Repository Structure

Each design pattern lives in its own standalone Spring Boot application folder so you can clone, run, and experiment with one pattern at a time:

```text
spring-boot-creational-design-patterns/
├── singleton-pattern/
│   └── payment-singleton-demo/     # Java 21 + Spring Boot 4.2.0 (Naive vs. DCL vs. Bill Pugh Singleton)
├── notes/                          # (Coming soon) Handwritten & study notes
├── .gitignore
└── README.md
```

---

## Creational Design Patterns Comparison

Creational patterns abstract the **object creation process** so your Spring Boot services stay decoupled, performant, and easy to extend.

| Pattern | Core Intent | When to Use in Spring Boot | Key Difference | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Singleton** | Ensure a class has **only one shared instance** with a global access point | Expensive shared resources (e.g., connection clients, caches, config registries); also Spring's default `@Bean` scope | Focuses on **single instance reuse** across threads | ✅ [Implemented](./singleton-pattern/payment-singleton-demo) |
| **Factory Method** | Define an interface for creating an object, letting **subclasses/implementations decide** which class to instantiate | Selecting a single service implementation dynamically (e.g., Stripe vs. PayPal payment processor, Email vs. SMS notifier) | Creates **one product** via a factory method / Spring map lookup | 🚧 Planned |
| **Abstract Factory** | Create **families of related or dependent objects** without specifying concrete classes | Multi-provider or multi-theme setups (e.g., AWS vs. GCP storage + queue + secret clients) | Creates **a suite of related products** that must work together | 🚧 Planned |
| **Builder** | Construct **complex objects step by step**, separating construction from representation | Requests/DTOs/configs with many optional fields (e.g., complex order, report, or HTTP client configuration) | Focuses on **step-by-step assembly** of one complex object | 🚧 Planned |
| **Prototype** | Create new objects by **cloning an existing instance** rather than constructing from scratch | Expensive initialization where new independent copies are needed per request (e.g., document templates, rule snapshots) | Creates **copies of a pre-built object** (`@Scope("prototype")` or `.clone()`) | 🚧 Planned |

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

## Handwritten & Study Notes

Add your handwritten notes (PDFs, images, or Markdown summaries) inside a `notes/` folder and link them below:

| Topic | Notes Link | Key Takeaways |
| :--- | :--- | :--- |
| **Creational Patterns Overview** | `notes/creational-patterns-overview.pdf` *(add file)* | Why object creation needs abstraction in Spring Boot |
| **Singleton Pattern** | `notes/singleton-pattern.pdf` *(add file)* | Eager vs. Lazy, DCL (`volatile`), Bill Pugh Holder, Enum Singleton, Spring `@Scope("singleton")` |
| **Factory Method vs. Abstract Factory** | `notes/factory-patterns.pdf` *(add file)* | Single product vs. product family |
| **Builder & Prototype** | `notes/builder-prototype.pdf` *(add file)* | Fluent construction vs. cloning |

---

## Contributing & Roadmap

- [x] **Singleton Pattern** (`singleton-pattern/payment-singleton-demo`)
- [ ] **Factory Method Pattern**
- [ ] **Abstract Factory Pattern**
- [ ] **Builder Pattern**
- [ ] **Prototype Pattern**
- [ ] **Handwritten & Study Notes**
