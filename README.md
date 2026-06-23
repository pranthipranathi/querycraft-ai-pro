# 🔍 QueryCraft AI Pro

An AI-powered Natural Language to SQL Assistant that converts plain English questions into SQL queries, executes them, and displays results in a beautiful UI.

## ✨ Features

- 🤖 AI-powered SQL Generation using Google Gemini
- 💡 Plain English SQL Explanation
- 🛡️ SQL Safety Validation
- ⚡ Real-time SQL Execution
- 📜 Query History Tracking
- 🗄️ Database Schema Viewer
- 🔐 JWT Authentication

## 🛠️ Tech Stack

**Backend:** Java 21, Spring Boot, Spring Security, JWT, Hibernate, MySQL, Gemini API

**Frontend:** React.js, Axios, CSS-in-JS

## ⚙️ Setup

### Backend
```bash
git clone https://github.com/pranthipranathi/querycraft-ai-pro.git
cd querycraft-ai-pro
# Add application.properties with your MySQL and Gemini credentials
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register user |
| POST | `/api/auth/login` | Login |
| POST | `/api/query/generate` | NL to SQL |
| GET | `/api/schema/tables` | Get tables |
| GET | `/api/history` | Query history |

## 💡 Example

**Question:** Show all users

**Generated SQL:**
```sql
SELECT * FROM users;
```

**Explanation:** This query retrieves all records from the users table.

## 👩‍💻 Developer

**Pranathi** — Final Year B.Tech Student

- GitHub: [@pranthipranathi](https://github.com/pranthipranathi)
- Portfolio: [pranthipranathi.github.io](https://pranthipranathi.github.io/personal-website-Portfolio)