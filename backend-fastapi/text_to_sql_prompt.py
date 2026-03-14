"""
Text-to-SQL System Prompt Module
=================================
This module defines the structured system prompt used by the Text-to-SQL
engine to convert natural language questions into valid PostgreSQL queries.

It uses few-shot prompting — providing example pairs of (question → SQL)
to ground the AI model before it handles the real user query.
"""

# ===========================================================
# DATABASE SCHEMA CONTEXT  (injected into the system prompt)
# ===========================================================
DB_SCHEMA_CONTEXT = """
You are an expert PostgreSQL query generator for the Supporter Unit College Management System.

## DATABASE SCHEMA

### Table: users
| Column        | Type    | Description                            |
|---------------|---------|----------------------------------------|
| id            | INTEGER | Primary key                            |
| name          | VARCHAR | Full name of the user                  |
| email         | VARCHAR | Unique email address                   |
| password_hash | VARCHAR | bcrypt-hashed password (never SELECT)  |
| role          | ENUM    | 'STUDENT', 'TEACHER', 'FINANCE', 'ADMIN'|

### Table: financial_records
| Column           | Type      | Description                              |
|-----------------|-----------|------------------------------------------|
| id               | INTEGER   | Primary key                              |
| student_id       | INTEGER   | FK → users.id                            |
| amount_encrypted | VARCHAR   | AES-256 encrypted fee amount (raw bytes) |
| notes_encrypted  | VARCHAR   | AES-256 encrypted notes                  |
| created_at       | TIMESTAMP | Record creation timestamp                |

### Table: feedbacks
| Column     | Type      | Description                                        |
|------------|-----------|----------------------------------------------------|
| id         | INTEGER   | Primary key                                        |
| student_id | INTEGER   | FK → users.id                                      |
| batch      | VARCHAR   | Batch name, e.g. 'Batch A', 'Batch B'              |
| content    | TEXT      | Anonymized feedback text                           |
| sentiment  | ENUM      | 'POSITIVE', 'NEUTRAL', 'NEGATIVE'                  |
| created_at | TIMESTAMP | Feedback submission timestamp                      |

### Table: system_logs
| Column    | Type      | Description                          |
|-----------|-----------|--------------------------------------|
| id        | INTEGER   | Primary key                          |
| user_id   | INTEGER   | ID of the actor (admin/teacher)      |
| action    | VARCHAR   | Description of the action taken      |
| endpoint  | VARCHAR   | API endpoint that was called         |
| timestamp | TIMESTAMP | When the action occurred             |

## RULES
- NEVER generate DROP, DELETE, UPDATE, or INSERT statements.
- NEVER SELECT password_hash from users.
- ALWAYS use table aliases for clarity in JOINs.
- ALWAYS use LIMIT 100 unless the question explicitly asks for all records.
- For financial data, note in the query comment that amounts are AES-encrypted.
- Only generate SELECT queries.
- Return ONLY the SQL — no explanation, no markdown code blocks.
"""

# ===========================================================
# FEW-SHOT EXAMPLES (5 examples covering simple to complex)
# ===========================================================
FEW_SHOT_EXAMPLES = [

    # Example 1 — Simple filter (single table)
    {
        "question": "How many students are registered in the system?",
        "sql": """SELECT COUNT(*) AS total_students
FROM users
WHERE role = 'STUDENT'
LIMIT 1;"""
    },

    # Example 2 — Aggregate with GROUP BY
    {
        "question": "Show me the number of feedback submissions per batch.",
        "sql": """SELECT
    f.batch,
    COUNT(f.id)              AS total_feedback,
    SUM(CASE WHEN f.sentiment = 'POSITIVE' THEN 1 ELSE 0 END) AS positive_count,
    SUM(CASE WHEN f.sentiment = 'NEGATIVE' THEN 1 ELSE 0 END) AS negative_count
FROM feedbacks f
GROUP BY f.batch
ORDER BY f.batch
LIMIT 100;"""
    },

    # Example 3 — JOIN (users + feedbacks)
    {
        "question": "List all students from Batch A who gave negative feedback.",
        "sql": """SELECT
    u.id,
    u.name,
    u.email,
    f.content,
    f.created_at
FROM users u
INNER JOIN feedbacks f ON u.id = f.student_id
WHERE u.role      = 'STUDENT'
  AND f.batch     = 'Batch A'
  AND f.sentiment = 'NEGATIVE'
ORDER BY f.created_at DESC
LIMIT 100;"""
    },

    # Example 4 — Complex JOIN (users + financial_records + feedbacks) — the key example
    {
        "question": "Show me the total fees paid by students who gave negative feedback in Batch A.",
        "sql": """-- NOTE: amount_encrypted values are AES-256 encrypted and must be decrypted by the application layer.
-- This query returns the raw encrypted records for students with negative feedback in Batch A.
SELECT
    u.id              AS student_id,
    u.name            AS student_name,
    u.email,
    COUNT(fr.id)      AS total_fee_records,
    -- Decryption happens in Python via encryption.py, not in SQL
    ARRAY_AGG(fr.amount_encrypted) AS encrypted_amounts
FROM users u
INNER JOIN feedbacks f        ON u.id = f.student_id
INNER JOIN financial_records fr ON u.id = fr.student_id
WHERE u.role      = 'STUDENT'
  AND f.batch     = 'Batch A'
  AND f.sentiment = 'NEGATIVE'
GROUP BY u.id, u.name, u.email
ORDER BY total_fee_records DESC
LIMIT 100;"""
    },

    # Example 5 — Time-based + audit log query
    {
        "question": "Which admin accounts made changes to the system in the last 7 days?",
        "sql": """SELECT
    u.id,
    u.name            AS admin_name,
    u.email,
    COUNT(sl.id)      AS total_actions,
    MAX(sl.timestamp) AS last_action_at
FROM users u
INNER JOIN system_logs sl ON u.id = sl.user_id
WHERE u.role        = 'ADMIN'
  AND sl.timestamp >= NOW() - INTERVAL '7 days'
GROUP BY u.id, u.name, u.email
ORDER BY total_actions DESC
LIMIT 100;"""
    },
]


# ===========================================================
# SYSTEM PROMPT BUILDER
# ===========================================================
def build_system_prompt() -> str:
    """
    Assembles the full system prompt:
      1. Schema context
      2. Few-shot examples
      3. Final instruction
    """
    examples_text = "\n\n".join([
        f"### Example {i + 1}\n"
        f"**Question:** {ex['question']}\n"
        f"**SQL:**\n```sql\n{ex['sql']}\n```"
        for i, ex in enumerate(FEW_SHOT_EXAMPLES)
    ])

    return f"""{DB_SCHEMA_CONTEXT}

## FEW-SHOT EXAMPLES
{examples_text}

## YOUR TASK
Using the schema above and the examples as guidance, convert the following
natural language question into a valid, safe PostgreSQL SELECT query.
Return ONLY the SQL query — no explanations, no markdown fences.
"""


def find_best_example(user_query: str) -> dict | None:
    """
    Naive keyword-based example matcher.
    In production, replace with semantic similarity (e.g., OpenAI embeddings).
    Returns the most relevant example, or None if no strong match.
    """
    query_lower = user_query.lower()
    
    scores = []
    for example in FEW_SHOT_EXAMPLES:
        keywords = example["question"].lower().split()
        score = sum(1 for word in keywords if word in query_lower and len(word) > 3)
        scores.append((score, example))
    
    scores.sort(key=lambda x: x[0], reverse=True)
    best_score, best_example = scores[0]
    
    return best_example if best_score >= 2 else None
