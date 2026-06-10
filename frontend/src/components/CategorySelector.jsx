import React from 'react';

const CATEGORIES = [
  'Indian Polity',
  'History',
  'Geography',
  'Science',
  'Economics',
  'Current Affairs',
  'Mixed Practice',
];

const CategorySelector = ({ selected, setSelected, multi = true }) => {
  const toggle = (cat) => {
    if (!multi) return setSelected([cat]);
    if (selected.includes(cat)) setSelected(selected.filter((s) => s !== cat));
    else setSelected([...selected, cat]);
  };

  return (
    <div className="category-selector">
      <div className="category-label">Choose categories</div>
      <div className="category-list">
        {CATEGORIES.map((cat) => (
          <button
            key={cat}
            onClick={() => toggle(cat)}
            className={`chip ${selected.includes(cat) ? 'active' : ''}`}
            aria-pressed={selected.includes(cat)}
          >
            {cat}
          </button>
        ))}
      </div>
    </div>
  );
};

export default CategorySelector;
