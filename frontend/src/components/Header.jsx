import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import LanguageToggle from './LanguageToggle';
import { useAuth } from '../contexts/AuthContext';

const Header = () => {
  const [open, setOpen] = useState(false);
  const { user, logout } = useAuth();

  const displayName = (user && (user.name || user.email)) || window.localStorage.getItem('tnpsc_email') || 'Guest';

  return (
    <header className="topbar header-shell">
      <div className="brand"><Link to="/home">TNPSC Pulse</Link></div>
      <div className="top-actions">
        <LanguageToggle />
        <div className="profile" onMouseLeave={() => setOpen(false)}>
          <button className="profile-button" onClick={() => setOpen((s) => !s)}>
            <span className="avatar" aria-hidden> {displayName.charAt(0).toUpperCase()}</span>
            <span className="profile-name">{displayName}</span>
          </button>
          {open && (
            <div className="profile-menu">
              <Link to="/profile" className="menu-item" onClick={() => setOpen(false)}>Profile</Link>
              <button className="menu-item logout" onClick={() => { setOpen(false); logout(); }}>Logout</button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
