import React, { useState, useEffect } from 'react';
import { queryAPI, historyAPI, schemaAPI } from '../services/api';

function Dashboard({ user, onLogout }) {
  const [question, setQuestion] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [history, setHistory] = useState([]);
  const [tables, setTables] = useState([]);
  const [activeTab, setActiveTab] = useState('query');

  useEffect(() => {
    loadHistory();
    loadTables();
  }, []);

  const loadHistory = async () => {
    try {
      const response = await historyAPI.getHistory();
      setHistory(response.data);
    } catch (err) {
      console.error('Error loading history:', err);
    }
  };

  const loadTables = async () => {
    try {
      const response = await schemaAPI.getTables();
      setTables(response.data);
    } catch (err) {
      console.error('Error loading tables:', err);
    }
  };

  const handleQuery = async () => {
    if (!question.trim()) return;
    setLoading(true);
    setResult(null);
    try {
      const response = await queryAPI.generateQuery({
        question: question,
        executeQuery: true,
      });
      setResult(response.data);
      loadHistory();
    } catch (err) {
      setResult({
        status: 'FAILED',
        errorMessage: 'Something went wrong!',
      });
    }
    setLoading(false);
  };

  const handleDeleteHistory = async (id) => {
    try {
      await historyAPI.deleteHistory(id);
      loadHistory();
    } catch (err) {
      console.error('Error deleting history:', err);
    }
  };

  return (
    <div style={styles.container}>
      {/* Header */}
      <div style={styles.header}>
        <h1 style={styles.headerTitle}>🔍 QueryCraft AI Pro</h1>
        <div style={styles.headerRight}>
          <span style={styles.username}>👤 {user.username}</span>
          <button style={styles.logoutBtn} onClick={onLogout}>
            Logout
          </button>
        </div>
      </div>

      {/* Navigation Tabs */}
      <div style={styles.tabs}>
        <button
          style={activeTab === 'query' ? styles.activeTab : styles.tab}
          onClick={() => setActiveTab('query')}
        >
          🤖 AI Query
        </button>
        <button
          style={activeTab === 'history' ? styles.activeTab : styles.tab}
          onClick={() => setActiveTab('history')}
        >
          📜 History
        </button>
        <button
          style={activeTab === 'schema' ? styles.activeTab : styles.tab}
          onClick={() => setActiveTab('schema')}
        >
          🗄️ Schema
        </button>
      </div>

      {/* Query Tab */}
      {activeTab === 'query' && (
        <div style={styles.content}>
          <div style={styles.queryCard}>
            <h2 style={styles.cardTitle}>Ask in Plain English</h2>
            <p style={styles.cardSubtitle}>
              Type your question and AI will convert it to SQL!
            </p>

            <textarea
              style={styles.textarea}
              placeholder="e.g. Show all users, Show top 5 students by CGPA..."
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              rows={4}
            />

            <button
              style={styles.queryBtn}
              onClick={handleQuery}
              disabled={loading}
            >
              {loading ? '⏳ Generating SQL...' : '🚀 Generate & Execute SQL'}
            </button>
          </div>

          {/* Results */}
          {result && (
            <div style={styles.resultCard}>
              {/* Status */}
              <div style={{
                ...styles.statusBadge,
                background: result.status === 'SUCCESS' ? '#e8f5e9' : '#ffebee',
                color: result.status === 'SUCCESS' ? '#2e7d32' : '#c62828',
              }}>
                {result.status === 'SUCCESS' ? '✅ SUCCESS' : '❌ ' + result.status}
              </div>

              {/* Generated SQL */}
              {result.generatedSql && (
                <div style={styles.section}>
                  <h3 style={styles.sectionTitle}>Generated SQL</h3>
                  <pre style={styles.sqlBox}>{result.generatedSql}</pre>
                </div>
              )}

              {/* Explanation */}
              {result.explanation && (
                <div style={styles.section}>
                  <h3 style={styles.sectionTitle}>💡 Explanation</h3>
                  <p style={styles.explanation}>{result.explanation}</p>
                </div>
              )}

              {/* Error */}
              {result.errorMessage && (
                <div style={styles.section}>
                  <p style={styles.errorText}>⚠️ {result.errorMessage}</p>
                </div>
              )}

              {/* Results Table */}
              {result.results && result.results.length > 0 && (
                <div style={styles.section}>
                  <h3 style={styles.sectionTitle}>
                    📊 Results ({result.rowCount} rows)
                  </h3>
                  <div style={styles.tableContainer}>
                    <table style={styles.table}>
                      <thead>
                        <tr>
                          {Object.keys(result.results[0]).map((key) => (
                            <th key={key} style={styles.th}>{key}</th>
                          ))}
                        </tr>
                      </thead>
                      <tbody>
                        {result.results.map((row, index) => (
                          <tr key={index} style={
                            index % 2 === 0 ? styles.trEven : styles.trOdd
                          }>
                            {Object.values(row).map((val, i) => (
                              <td key={i} style={styles.td}>
                                {String(val)}
                              </td>
                            ))}
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}

              {/* Execution Time */}
              {result.executionTimeMs && (
                <p style={styles.execTime}>
                  ⚡ Executed in {result.executionTimeMs}ms
                </p>
              )}
            </div>
          )}
        </div>
      )}

      {/* History Tab */}
      {activeTab === 'history' && (
        <div style={styles.content}>
          <h2 style={styles.cardTitle}>Query History</h2>
          {history.length === 0 ? (
            <p style={styles.emptyText}>No queries yet!</p>
          ) : (
            history.map((item) => (
              <div key={item.id} style={styles.historyCard}>
                <div style={styles.historyHeader}>
                  <span style={styles.historyQuestion}>
                    💬 {item.naturalLanguageQuery}
                  </span>
                  <div style={styles.historyActions}>
                    <span style={{
                      ...styles.historyStatus,
                      color: item.executionStatus === 'SUCCESS'
                        ? 'green' : 'red',
                    }}>
                      {item.executionStatus}
                    </span>
                    <button
                      style={styles.deleteBtn}
                      onClick={() => handleDeleteHistory(item.id)}
                    >
                      🗑️
                    </button>
                  </div>
                </div>
                <pre style={styles.historySql}>{item.generatedSql}</pre>
                <small style={styles.historyTime}>
                  {new Date(item.createdAt).toLocaleString()}
                </small>
              </div>
            ))
          )}
        </div>
      )}

      {/* Schema Tab */}
      {activeTab === 'schema' && (
        <div style={styles.content}>
          <h2 style={styles.cardTitle}>Database Schema</h2>
          <div style={styles.tablesGrid}>
            {tables.map((table) => (
              <div key={table} style={styles.tableCard}>
                <h3 style={styles.tableName}>🗄️ {table}</h3>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    background: '#f5f5f5',
    fontFamily: 'Arial, sans-serif',
  },
  header: {
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    padding: '16px 32px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  headerTitle: {
    color: 'white',
    margin: 0,
    fontSize: '24px',
  },
  headerRight: {
    display: 'flex',
    alignItems: 'center',
    gap: '16px',
  },
  username: {
    color: 'white',
    fontSize: '16px',
  },
  logoutBtn: {
    background: 'rgba(255,255,255,0.2)',
    color: 'white',
    border: '1px solid white',
    padding: '8px 16px',
    borderRadius: '8px',
    cursor: 'pointer',
  },
  tabs: {
    background: 'white',
    padding: '0 32px',
    display: 'flex',
    gap: '8px',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
  },
  tab: {
    padding: '16px 24px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: '16px',
    color: '#888',
    borderBottom: '3px solid transparent',
  },
  activeTab: {
    padding: '16px 24px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: '16px',
    color: '#667eea',
    borderBottom: '3px solid #667eea',
    fontWeight: 'bold',
  },
  content: {
    padding: '32px',
    maxWidth: '1000px',
    margin: '0 auto',
  },
  queryCard: {
    background: 'white',
    padding: '32px',
    borderRadius: '16px',
    boxShadow: '0 4px 16px rgba(0,0,0,0.1)',
    marginBottom: '24px',
  },
  cardTitle: {
    color: '#333',
    marginBottom: '8px',
  },
  cardSubtitle: {
    color: '#888',
    marginBottom: '24px',
  },
  textarea: {
    width: '100%',
    padding: '16px',
    borderRadius: '8px',
    border: '1px solid #ddd',
    fontSize: '16px',
    resize: 'vertical',
    boxSizing: 'border-box',
    marginBottom: '16px',
  },
  queryBtn: {
    padding: '14px 32px',
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    color: 'white',
    border: 'none',
    borderRadius: '8px',
    fontSize: '16px',
    cursor: 'pointer',
    width: '100%',
  },
  resultCard: {
    background: 'white',
    padding: '32px',
    borderRadius: '16px',
    boxShadow: '0 4px 16px rgba(0,0,0,0.1)',
  },
  statusBadge: {
    display: 'inline-block',
    padding: '8px 16px',
    borderRadius: '8px',
    fontWeight: 'bold',
    marginBottom: '16px',
  },
  section: {
    marginBottom: '24px',
  },
  sectionTitle: {
    color: '#333',
    marginBottom: '12px',
  },
  sqlBox: {
    background: '#1e1e1e',
    color: '#4fc3f7',
    padding: '16px',
    borderRadius: '8px',
    overflow: 'auto',
    fontSize: '14px',
  },
  explanation: {
    background: '#f3f4f6',
    padding: '16px',
    borderRadius: '8px',
    color: '#555',
    lineHeight: '1.6',
  },
  errorText: {
    color: '#c62828',
    background: '#ffebee',
    padding: '16px',
    borderRadius: '8px',
  },
  tableContainer: {
    overflowX: 'auto',
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
  },
  th: {
    background: '#667eea',
    color: 'white',
    padding: '12px 16px',
    textAlign: 'left',
  },
  trEven: {
    background: '#f9f9f9',
  },
  trOdd: {
    background: 'white',
  },
  td: {
    padding: '12px 16px',
    borderBottom: '1px solid #eee',
    color: '#333',
  },
  execTime: {
    color: '#888',
    fontSize: '12px',
    textAlign: 'right',
  },
  historyCard: {
    background: 'white',
    padding: '20px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
    marginBottom: '16px',
  },
  historyHeader: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '8px',
  },
  historyQuestion: {
    fontWeight: 'bold',
    color: '#333',
    flex: 1,
  },
  historyActions: {
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
  },
  historyStatus: {
    fontWeight: 'bold',
    fontSize: '12px',
  },
  deleteBtn: {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: '16px',
  },
  historySql: {
    background: '#1e1e1e',
    color: '#4fc3f7',
    padding: '12px',
    borderRadius: '8px',
    fontSize: '12px',
    overflow: 'auto',
    marginBottom: '8px',
  },
  historyTime: {
    color: '#888',
    fontSize: '12px',
  },
  emptyText: {
    color: '#888',
    textAlign: 'center',
    padding: '40px',
  },
  tablesGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
    gap: '16px',
  },
  tableCard: {
    background: 'white',
    padding: '20px',
    borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
    textAlign: 'center',
  },
  tableName: {
    color: '#667eea',
    margin: 0,
  },
};

export default Dashboard;