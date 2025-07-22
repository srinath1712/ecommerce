import React, { useState } from 'react';
import './Login.css';
import axios from 'axios';

const Login = () => {
  const [view, setView] = useState('login'); // 'login', 'register', 'forgot'
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [showOtpForm, setShowOtpForm] = useState(false);

  // Inside Login component
const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    if (view === 'login') {
      const response = await axios.post('http://localhost:8080/api/login', {
        mailId: email,
        password,
      });
      alert(`Login Success: ${response.data.message}`);
      setEmail('');
      setPassword('');
      setView('login');
    }

    if (view === 'register') {
      const response = await axios.post('http://localhost:8080/api/register', {
        username: name,
        mailId: email,
        password,
      });
      alert(`Registered Successfully: ${response.data.message}`);
      setEmail('');
      setPassword('');
      setName('');
      setView('login');
    }

    if (view === 'forgot') {
      const response = await axios.post('http://localhost:8080/api/otp/send', {
        mailId: email,
        purpose: 'password_reset',
      });
      alert(`OTP sent to ${email}`);
      setShowOtpForm(true); // don't reset view
      // ✅ Do NOT call setView('login') here!
    }
  } catch (error) {
    if (error.response) {
      alert(`Error: ${error.response.data.message}`);
    } else {
      alert('Network error or server not reachable');
    }
  }
};

const handleResetPassword = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/api/otp/verify', {
        mailId: email,
        otp,
        newPassword,
        purpose: "password_reset"
      });

      alert(response.data.message || 'Password reset successful');
      setShowOtpForm(false);
      setOtp('');
      setNewPassword('');
      setView('login');
    } catch (error) {
      alert(error.response?.data?.message || 'Password reset failed');
    }
  };

  return (
    <div className="login-wrapper">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>
          {view === 'login' && 'Login'}
          {view === 'register' && 'Register'}
          {view === 'forgot' && 'Forgot Password'}
        </h2>

        {view === 'register' && (
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
        )}

        {(view !== 'forgot' || !showOtpForm) && (
  <div className="form-group">
    <label>Email</label>
    <input
      type="email"
      value={email}
      onChange={(e) => setEmail(e.target.value)}
      required
    />
  </div>
)}


        {view !== 'forgot' && (
          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
        )}

        {view !== 'forgot' || !showOtpForm ? (
        <button type="submit">
          {view === 'login' && 'Login'}
          {view === 'register' && 'Register'}
          {view === 'forgot' && 'Send Reset Link'}
        </button>
      ) : null}

        {view === 'forgot' && showOtpForm && (
          <>
            <div className="form-group">
              <label>OTP</label>
              <input
                type="text"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label>New Password</label>
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
            </div>
            <button onClick={handleResetPassword}>Verify & Reset Password</button>
          </>
        )}

        <div className="form-footer">
          {view === 'login' && (
            <>
              <p>
                <span className="link" onClick={() => setView('forgot')}>
                  Forgot Password?
                </span>
              </p>
              <p>
                Don't have an account?{' '}
                <span className="link" onClick={() => setView('register')}>
                  Register
                </span>
              </p>
            </>
          )}

          {view === 'register' && (
            <p>
              Already have an account?{' '}
              <span className="link" onClick={() => setView('login')}>
                Login
              </span>
            </p>
          )}

          {view === 'forgot' && (
            <p>
              Remember your password?{' '}
              <span className="link" onClick={() => setView('login')}>
                Login
              </span>
            </p>
          )}
        </div>
      </form>
    </div>
  );
};

export default Login;