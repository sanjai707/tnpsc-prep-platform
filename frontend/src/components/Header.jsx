import React, { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import LanguageToggle from './LanguageToggle';
import { useAuth } from '../contexts/AuthContext';

const Header = () => {
  const [open, setOpen] = useState(false);
  const profileRef = useRef(null);
  const { user, logout } = useAuth();

  const displayName = (user && (user.name || user.email)) || window.localStorage.getItem('tnpsc_email') || 'Guest';

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (profileRef.current && !profileRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    if (open) {
      document.addEventListener('mousedown', handleClickOutside);
      document.addEventListener('touchstart', handleClickOutside);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('touchstart', handleClickOutside);
    };
  }, [open]);

  return (
    <header className="topbar header-shell">
      <div className="brand"><Link to="/home">TNPSC Pulse</Link></div>
      <div className="top-actions">
        <LanguageToggle />
        <div className="profile" ref={profileRef}>
          <button
            type="button"
            className="profile-button"
            onClick={() => setOpen((s) => !s)}
            aria-expanded={open}
            aria-haspopup="menu"
          >
            <span className="avatar" aria-hidden> {displayName.charAt(0).toUpperCase()}</span>
            <span className="profile-name">{displayName}</span>
          </button>
          {open && (
            <div className="profile-menu" role="menu">
              <Link to="/profile" className="menu-item" onClick={() => setOpen(false)}>Profile</Link>
              <button className="menu-item logout" type="button" onClick={() => { setOpen(false); logout(); }}>Logout</button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
