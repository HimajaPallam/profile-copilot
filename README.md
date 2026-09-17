# Profile Orchestration Copilot (Retrieval-Only POC)

An agentic AI demo built with **Java + Spring Boot + Spring AI**, showing an LLM
that answers a support rep's natural-language question about a customer by
deciding, on its own, which internal "backend services" to call - a customer
profile service, an account service, and an activity/history service - then
aggregating the results into one answer.

The rep supplies the **customerId directly** (as they would from their own
CRM/case screen) rather than a name - two customers can easily share a name,
and resolving identity from a name is exactly the kind of ambiguity you don't
want an LLM guessing at. The agent's only job is deciding *which* read-only
tools to call for that known customer, not *who* the customer is.

## Architecture

```
 input - curl / browser UI
        |
        v
 POST /api/copilot/ask  { "customerId": "CUST1001", "question": "..." }
        |
        v
 CopilotController --------------------------+
        |                                    |
        v                                    |
 Spring AI ChatClient  <---- Ollama (local llama3.2, tool-calling)
        |                                    |
        | model decides which tool(s) to call, in what order
        v                                    |
 ProfileTools  (the agent's "tool belt")------+
   - getCustomerProfile(customerId)
   - getAccountSummary(customerId)
   - getRecentActivity(customerId, days)
        |
        v
 CustomerProfileService / AccountService / ActivityService
        |
        v
 H2 in-memory database (seeded with 3 fake customers on startup)
```

The response includes both the model's final natural-language `answer` and a
`toolTrace` - the exact list of tool calls the model made to produce that
answer. That trace is a proof the model is reasoning about *which* tool(s) 
it needs for a given question, rather than always fetching everything.

## Prerequisites

- Java 21+ and Maven (both already used to write this project)
- [Ollama](https://ollama.com/download) installed on your machine, with
  `llama3.2` already pulled (this project is configured to use it)

## Setup

**1. Make sure Ollama is running:**

```bash
ollama serve
```

(If it's already running as a background service, skip this.)

**2. Run the app:**

```bash
mvn spring-boot:run
```

**3. Open the browser:**

Visit `http://localhost:8080` in a browser - there's a tiny built-in chat
page with a customer ID field and example questions you can click to try.

Or hit the API directly:

```bash
curl -s http://localhost:8080/api/copilot/ask \
  -H "Content-Type: application/json" \
  -d '{"customerId": "CUST1001", "question": "What is this customer'\''s risk tolerance and when did they last change their beneficiary?"}' | jq
```

## Seeded demo customers

| Customer ID | Name          | Notes                                          |
|-------------|---------------|-------------------------------------------------|
| CUST1001    | John Carter   | Moderate risk; beneficiary changed 12 days ago  |
| CUST1002    | Priya Nair    | Aggressive risk; recent TSLA→NVDA trade         |
| CUST1003    | Miguel Santos | Conservative risk; KYC pending review           |

Try a narrow question ("What's this customer's risk tolerance?") versus a
broad one ("Give me a full snapshot") against the same customerId, and
compare the `toolTrace` in each response - that's the clearest way to show
the agent is genuinely deciding what it needs, not just calling everything
every time.

## What this demonstrates

- **Agentic tool orchestration**: the LLM decides which Java methods to call,
  based on a plain-English question, given a known customer identity.
- **Scoped tool selection**: a narrow question triggers one tool call, a
  broad one triggers three - worth showing side by side.
- **Observability into agent reasoning**: the `toolTrace` in every response
  makes the model's tool-selection visible instead of a black box.
- **Safety-by-scope**: no tool can mutate data, and identity resolution is
  deliberately kept out of the model's hand
