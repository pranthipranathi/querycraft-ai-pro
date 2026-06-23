import React, { useState } from 'react';
import { authAPI } from '../services/api';

function Register({ onRegister, switchToLogin }) {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    setLoading(true);
    setError('');
    setSuccess('');
    try {
      await authAPI.register({ username, email, password });
      setSuccess('Registration successful! Please login.');
      setTimeout(() => switchToLogin(), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed!');
    }
    setLoading(false);
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>🔍 QueryCraft AI Pro</h1>
        <p style={styles.subtitle}>Natural Language to SQL Assistant</p>

        <h2 style={styles.formTitle}>Register</h2>

        {error && <p style={styles.error}>{error}</p>}
        {success && <p style={styles.success}>{success}</p>}

        <input
          style={styles.input}
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          style={styles.input}
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          style={styles.input}
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button
          style={styles.button}
          onClick={handleRegister}
          disabled={loading}
        >
          {loading ? 'Registering...' : 'Register'}
        </button>

        <p style={styles.switchText}>
          Already have an account?{' '}
          <span style={styles.link} onClick={switchToLogin}>
            Login here
          </span>
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
  card: {
    background: 'white',
    padding: '40px',
    borderRadius: '16px',
    boxShadow: '0 20px 60px rgba(0,0,0,0.3)',
    width: '400px',
    textAlign: 'center',
  },
  title: {
    color: '#667eea',
    fontSize: '28px',
    marginBottom: '8px',
  },
  subtitle: {
    color: '#888',
    fontSize: '14px',
    marginBottom: '24px',
  },
  formTitle: {
    color: '#333',
    marginBottom: '20px',
  },
  input: {
    width: '100%',
    padding: '12px',
    marginBottom: '16px',
    borderRadius: '8px',
    border: '1px solid #ddd',
    fontSize: '16px',
    boxSizing: 'border-box',
  },
  button: {
    width: '100%',
    padding: '12px',
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    color: 'white',
    border: 'none',
    borderRadius: '8px',
    fontSize: '16px',
    cursor: 'pointer',
    marginBottom: '16px',
  },
  error: {
    color: 'red',
    marginBottom: '12px',
  },
  success: {
    color: 'green',
    marginBottom: '12px',
  },
  switchText: {
    color: '#888',
    fontSize: '14px',
  },
  link: {
    color: '#667eea',
    cursor: 'pointer',
    fontWeight: 'bold',
  },
};

export default Register;